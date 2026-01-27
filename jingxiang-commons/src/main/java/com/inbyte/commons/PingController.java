package com.inbyte.commons;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * ping
 * 测试用
 */
@Slf4j
@RestController
public class PingController {

    @GetMapping("ping")
    public String ping() {
        log.info("pong");
        return "pong";
    }

    @GetMapping("/")
    public String hi() {
        log.info("hi");
        return "hi there";
    }
}
