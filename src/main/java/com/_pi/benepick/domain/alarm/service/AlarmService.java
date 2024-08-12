package com._pi.benepick.domain.alarm.service;

public interface AlarmService {

    Object sendAlarm(String message);

    String getCongratulationsMessage(String email, String name, String url);

    String getAdditionalCongratulationsMessage(String email, String name, String url);

    void sendAlarmStart();

    void saveMessage(String email, String name, String url);
}
