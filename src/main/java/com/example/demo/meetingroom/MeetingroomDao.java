package com.example.demo.meetingroom;

import org.springframework.data.jpa.repository.JpaRepository;


import java.time.LocalDateTime;
import java.util.List;

public interface MeetingroomDao extends JpaRepository<Meetingroom, Integer> {
    List<Meetingroom> findByRoomId(int roomId);

    List<Meetingroom> findByRoomIdAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
            int roomId, LocalDateTime endDate, LocalDateTime startDate);
}