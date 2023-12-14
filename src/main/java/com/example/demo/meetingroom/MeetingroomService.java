package com.example.demo.meetingroom;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class MeetingroomService {
    private final MeetingroomDao dao;

    //추가,수정
    public MeetingroomDto save(MeetingroomDto dto) {
        Meetingroom entity = dao.save(new Meetingroom(dto.getId(),dto.getMember(), dto.getTitle(), dto.getStartDate(), dto.getEndDate(), dto.getRoomId()));
        return new MeetingroomDto(entity.getId(),entity.getMember(), entity.getTitle(), entity.getStartDate(), entity.getEndDate(),entity.getRoomId());
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
