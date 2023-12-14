package com.example.demo.chat;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageDao extends JpaRepository<ChatMessage, Long> {
    public List<ChatMessage> findByRoom_Id(int id, Sort sort);
}
