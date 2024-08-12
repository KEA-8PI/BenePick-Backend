package com._pi.benepick.config;

import com._pi.benepick.domain.alarm.service.AlarmService;
import com._pi.benepick.domain.draws.service.DrawsCommandService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
@Slf4j
public class SchedulerConfiguration {

    private final DrawsCommandService drawsCommandService;
    private final AlarmService alarmService;

    // 매일 밤 자정에 실행
    @Scheduled(cron = "01 00 00 * * ?")
    public void cronTask() {
        LocalDateTime now = LocalDateTime.now();
        drawsCommandService.updateGoodsStatus(now);
        drawsCommandService.drawStart(now);
    }

    // 매일 오전 9시에 실행
    @Scheduled(cron = "01 00 09 * * ?")
    public void messageCronTask() {
        alarmService.sendAlarmStart();
    }

}