package com.example.demo.meetingroom;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class MeetingroomService {
    private final MeetingroomDao dao;

    //추가,수정
    public MeetingroomDto save(MeetingroomDto dto) {
        if (isOverlap(dto)) {
            throw new RuntimeException("이미 예약된 시간대입니다.");
        }

        Meetingroom entity = dao.save(new Meetingroom(dto.getId(),dto.getMember(), dto.getTitle(), dto.getStartDate(), dto.getEndDate(), dto.getRoomId()));
        return new MeetingroomDto(entity.getId(),entity.getMember(), entity.getTitle(), entity.getStartDate(), entity.getEndDate(),entity.getRoomId());
    }

    // 중복 체크
    private boolean isOverlap(MeetingroomDto dto) {
        int roomId = dto.getRoomId();
        LocalDateTime startDate = dto.getStartDate();
        LocalDateTime endDate = dto.getEndDate();

        List<Meetingroom> overlappingEvents = dao.findByRoomIdAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
                roomId, endDate, startDate);

        return !overlappingEvents.isEmpty();
    }
    //중복 체크를 위한 메서드
    public boolean isOverlap(int roomId, LocalDateTime startDate, LocalDateTime endDate) {
        List<Meetingroom> overlappingEvents = dao.findByRoomIdAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
                roomId, endDate, startDate);

        return !overlappingEvents.isEmpty();
    }

    //아이디로 찾기
    public MeetingroomDto get(int id) {
        Meetingroom entity = dao.findById(id).orElse(null);
        return new MeetingroomDto(entity.getId(), entity.getMember(),entity.getTitle(), entity.getStartDate(), entity.getEndDate(),entity.getRoomId());
    }
    public ArrayList<MeetingroomDto> getAll(){
        List<Meetingroom> s = dao.findAll();
        ArrayList<MeetingroomDto> list = new ArrayList<>();
        for(Meetingroom entity:s) {
            list.add(new MeetingroomDto(entity.getId(), entity.getMember(),entity.getTitle(), entity.getStartDate(), entity.getEndDate(),entity.getRoomId()));
        }
        return list;
    }

    //삭제
    public void del(int id) {
        dao.deleteById(id);
    }

    //룸아이디로 찾기
    public ArrayList<MeetingroomDto> getByRoomId(int roomId){
        List<Meetingroom> s = dao.findByRoomId(roomId);
        ArrayList<MeetingroomDto> list = new ArrayList<>();
        for(Meetingroom entity:s) {
            list.add(new MeetingroomDto().toDto(entity));
        }
        return list;
    }
}
