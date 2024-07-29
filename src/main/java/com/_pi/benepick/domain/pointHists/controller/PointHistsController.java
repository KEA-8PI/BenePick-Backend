package com._pi.benepick.domain.pointHists.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "PointHists", description = "포인트 히스토리 API")
@RequestMapping("/point-hists")
public class PointHistsController {

}