package com._pi.benepick.domain.wishlists.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "Wishlists", description = "위시리스트 API")
@RequestMapping("/wishlists")
public class WishlistsController {

}
