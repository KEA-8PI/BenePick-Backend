package com._pi.benepick.domain.alarm.service;

public interface AlarmService {

    Object sendAlarm(String message);

    String getCongratulationsMessage(String email, String name);
}
