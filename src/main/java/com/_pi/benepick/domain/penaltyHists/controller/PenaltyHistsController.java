package com._pi.benepick.domain.penaltyHists.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "PenaltyHists", description = "패널티 히스토리 API")
@RequestMapping("/penalty-hists")
public class PenaltyHistsController {
    
}