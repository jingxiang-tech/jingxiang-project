package com.inbyte.commons.util.convert;

import org.locationtech.jts.geom.*;
import org.locationtech.jts.geom.impl.CoordinateArraySequence;
import org.locationtech.jts.geom.impl.CoordinateArraySequenceFactory;
import org.locationtech.jts.io.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * 地理空间转化工具
 */
public class PointConverter {

    /**
     * Little endian or Big endian
     */
    private int byteOrder = ByteOrderValues.LITTLE_ENDIAN;
    /**
     * Precision model
     */
    private PrecisionModel precisionModel = new PrecisionModel();
    /**
     * Coordinate sequence factory
     */
    private CoordinateSequenceFactory coordinateSequenceFactory = CoordinateArraySequenceFactory.instance();
    /**
     * Output dimension
     */
    private int outputDimension = 2;

    /**
     * Convert byte array containing SRID + WKB Geometry into Geometry object
     */
    public com.inbyte.commons.model.dto.Point from(byte[] bytes) {
        if (bytes == null) {
            return null;
        }
        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes)) {
            // Read SRID
            byte[] sridBytes = new byte[4];
            inputStream.read(sridBytes);
            int srid = ByteOrderValues.getInt(sridBytes, byteOrder);

            // Prepare Geometry factory
            GeometryFactory geometryFactory = new GeometryFactory(precisionModel, srid, coordinateSequenceFactory);

            // Read Geometry
            WKBReader wkbReader = new WKBReader(geometryFactory);
            Geometry geometry = wkbReader.read(new InputStreamInStream(inputStream));
            org.locationtech.jts.geom.Point point = (org.locationtech.jts.geom.Point) geometry;
            // convert to GeoPoint
            com.inbyte.commons.model.dto.Point geoPoint = new com.inbyte.commons.model.dto.Point(point.getX(), point.getY());
            return geoPoint;
        } catch (IOException | ParseException e) {
            throw new IllegalArgumentException(e);
        }
    }

    /**
     * Convert Geometry object into byte array containing SRID + WKB Geometry
     */
    public byte[] to(com.inbyte.commons.model.dto.Point geoPoint) {
        if (geoPoint == null) {
            return null;
        }
        Coordinate coordinate = new Coordinate(geoPoint.getLongitude(), geoPoint.getLatitude());
        CoordinateArraySequence coordinateArraySequence = new CoordinateArraySequence(new Coordinate[]{coordinate}, 2);
        org.locationtech.jts.geom.Point point = new org.locationtech.jts.geom.Point(coordinateArraySequence, new GeometryFactory());
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            // Write SRID
            byte[] sridBytes = new byte[4];
            ByteOrderValues.putInt(point.getSRID(), sridBytes, byteOrder);
            outputStream.write(sridBytes);
            // Write Geometry
            WKBWriter wkbWriter = new WKBWriter(outputDimension, byteOrder);
            wkbWriter.write(point, new OutputStreamOutStream(outputStream));
            return outputStream.toByteArray();
        } catch (IOException ioe) {
            throw new IllegalArgumentException(ioe);
        }
    }
}