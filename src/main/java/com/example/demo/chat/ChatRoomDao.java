package com.example.demo.chat;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatRoomDao extends JpaRepository<ChatRoom, Integer> {
    List<ChatRoom> findByParticipants_Id(long participantId, Sort sort);
}
