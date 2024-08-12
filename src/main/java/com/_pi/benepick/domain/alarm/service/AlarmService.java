package com._pi.benepick.domain.alarm.service;

import java.time.LocalDateTime;

public interface AlarmService {

    Object sendAlarm(String message);

    String getCongratulationsMessage(String email, String name, String url);

    void sendAlarmStart(LocalDateTime now);

    void saveMessage(String email, String name, String url);
}
