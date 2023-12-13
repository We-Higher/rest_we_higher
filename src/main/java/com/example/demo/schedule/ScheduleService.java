package com.example.demo.schedule;

import com.example.demo.member.Member;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpServletResponse;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ScheduleService {
	
    private final ScheduleDao dao;
    
    //자바에서 script 사용하기
    public static void init(HttpServletResponse response) {
        response.setContentType("text/html; charset=utf-8");
        response.setCharacterEncoding("utf-8");
    }

    //추가,수정
    public ScheduleDto save(ScheduleDto dto) {
        Schedule entity = dao.save(new Schedule(dto.getId(), dto.getMember(), dto.getTitle(), dto.getStartDate(), dto.getEndDate(), dto.getCnt()));
        return new ScheduleDto(entity.getId(), entity.getMember(), entity.getTitle(), entity.getStartDate(), entity.getEndDate(), entity.getCnt());
    }

    public ScheduleDto get(int id) {
        Schedule entity = dao.findById(id).orElse(null);
        return new ScheduleDto(entity.getId(), entity.getMember(), entity.getTitle(), entity.getStartDate(), entity.getEndDate(), entity.getCnt());
    }

    // 전체 리스트
    public ArrayList<ScheduleDto> getAll(){
        List<Schedule> s = dao.findAll();
        ArrayList<ScheduleDto> list = new ArrayList<>();
        for(Schedule entity:s) {
            list.add(new ScheduleDto(entity.getId(), entity.getMember(), entity.getTitle(), entity.getStartDate(), entity.getEndDate(), entity.getCnt()));
        }
        return list;
    }

    //member 리스트
    public ArrayList<ScheduleDto> getByMemberOrCnt(Member member, int cnt){
        List<Schedule> ms=dao.findByMemberOrCnt(member, cnt);
        ArrayList<ScheduleDto> list =new ArrayList<>();
        for(Schedule entity:ms) {
            list.add(new ScheduleDto(entity.getId(), entity.getMember(), entity.getTitle(), entity.getStartDate(), entity.getEndDate(), entity.getCnt()));
        }
        return list;
    }

    //삭제
    public void del(int id) {
    		
    	dao.deleteById(id);
    }

    // cnt(isNotice)만 찾기
    public ArrayList<ScheduleDto> getByIsNotice(int cnt){
        List<Schedule> ms = dao.findByCnt(cnt);
        ArrayList<ScheduleDto> list =new ArrayList<>();
        for(Schedule entity:ms) {
            list.add(new ScheduleDto(entity.getId(), entity.getMember(), entity.getTitle(), entity.getStartDate(), entity.getEndDate(), entity.getCnt()));
        }
        return list;
    }
}
