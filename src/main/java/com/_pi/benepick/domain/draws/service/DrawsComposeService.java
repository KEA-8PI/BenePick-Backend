package com._pi.benepick.domain.draws.service;

import com._pi.benepick.domain.draws.entity.Draws;

import java.time.LocalDateTime;

public interface DrawsComposeService {
    void drawStart(LocalDateTime now);
    void nonWinnerPointRefund(Draws waitDraw);
}
