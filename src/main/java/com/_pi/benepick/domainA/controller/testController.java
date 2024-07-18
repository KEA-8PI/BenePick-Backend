package com._pi.benepick.domainA.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Test", description = "test API")
@RequestMapping("/api/tests")
public class testController {

    @Operation(summary = "test api", description = "test api")
    @GetMapping("/hello")
    public String hello() {
        return "Hello World";
    }

}