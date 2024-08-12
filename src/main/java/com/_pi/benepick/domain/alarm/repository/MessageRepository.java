package com._pi.benepick.domain.alarm.repository;

import com._pi.benepick.domain.alarm.domain.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findAllByIsDeleted(boolean isDeleted);
}
