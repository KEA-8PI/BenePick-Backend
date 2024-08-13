package com._pi.benepick.config;

import com._pi.benepick.domain.draws.service.DrawsComposeService;
import com._pi.benepick.domain.goods.service.GoodsCommandService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
@Slf4j
public class SchedulerConfiguration {

    private final DrawsComposeService drawsComposeService;
    private final GoodsCommandService goodsCommandService;

    // 매일 밤 자정에 실행
    @Scheduled(cron = "01 00 00 * * ?")
    public void cronTask() {
        LocalDateTime now = LocalDateTime.now();
        goodsCommandService.updateGoodsStatus(now);
        drawsComposeService.drawStart(now);
    }

}