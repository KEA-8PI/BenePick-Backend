package com._pi.benepick.domain.alarm.service;

import com._pi.benepick.domain.alarm.domain.MessageType;
import com._pi.benepick.domain.members.entity.Members;

public interface AlarmService {

    Object sendAlarm(String message);

    String getMessageFactory(Members members, String url, MessageType type);

    String getCongratulationsMessage(String email, String name, String url);

    String getAdditionalCongratulationsMessage(String email, String name, String url);

    void sendAlarmStart();

    void saveMessage(String email, String name, String url);
}
