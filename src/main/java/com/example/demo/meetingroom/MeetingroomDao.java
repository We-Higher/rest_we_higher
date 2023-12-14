package com.example.demo.meetingroom;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MeetingroomDao extends JpaRepository<Meetingroom, Integer> {
    List<Meetingroom> findByRoomId(int roomId);
}