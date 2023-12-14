package com.example.demo.approval.vacation;

import com.example.demo.member.MemberDto;
import com.example.demo.member.MemberService;

import jakarta.servlet.http.HttpServletResponse;

import com.example.demo.schedule.Schedule;
import com.example.demo.schedule.ScheduleDao;

import com.example.demo.schedule.ScheduleDto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Locale;

@Service
public class VacationService {
    @Autowired
    private VactaionDao dao;
    
    @Autowired
    private ScheduleDao sdao;
    
    @Autowired
    private MemberService mservice;
    
    public static void init(HttpServletResponse response) {
        response.setContentType("text/html; charset=utf-8");
        response.setCharacterEncoding("utf-8");
    }

    public VacationDto saveVacation(VacationDto dto) {
        Vacation v = dao.save(new Vacation(dto.getVacationNum(), dto.getMember(), dto.getType(), dto.getStartDate(), dto.getEndDate(), dto.getReason(), dto.getWdate(), dto.getStatus(), dto.getRstatus(), dto.getApproval1(), dto.getApproval2(), dto.getApproval1rank(), dto.getApproval2rank(), dto.getApp1username(), dto.getApp2username()));
        return new VacationDto(v.getVacationNum(), v.getMember(), v.getType(), v.getStartDate(), v.getEndDate(), v.getReason(), v.getWdate(), v.getStatus(), v.getRstatus(), v.getApproval1(), v.getApproval2(), v.getApproval1rank(), v.getApproval2rank(), v.getApp1username(), v.getApp2username());
    }

    public ArrayList<VacationDto> getAll() {
        ArrayList<Vacation> list = (ArrayList<Vacation>) dao.findAll(Sort.by(Sort.Direction.DESC, "vacationNum"));
        ArrayList<VacationDto> list2 = new ArrayList<>();
        for (Vacation v : list) {
            list2.add(new VacationDto(v.getVacationNum(), v.getMember(), v.getType(), v.getStartDate(), v.getEndDate(), v.getReason(), v.getWdate(), v.getStatus(), v.getRstatus(), v.getApproval1(), v.getApproval2(), v.getApproval1rank(), v.getApproval2rank(), v.getApp1username(), v.getApp2username()));
        }
        return list2;
    }

    public VacationDto getById(int num) {
        Vacation v = dao.getById(num);
        return new VacationDto(v.getVacationNum(), v.getMember(), v.getType(), v.getStartDate(), v.getEndDate(), v.getReason(), v.getWdate(), v.getStatus(), v.getRstatus(), v.getApproval1(), v.getApproval2(), v.getApproval1rank(), v.getApproval2rank(), v.getApp1username(), v.getApp2username());
    }
    
    public VacationDto approveVacation(HttpServletResponse response, VacationDto dto, MemberDto mdto) throws IOException {
    	
    	if(dto.getRstatus()==0 && dto.getStatus()==0 && dto.getApp1username().equals(mdto.getUsername())){
    		dto.setStatus(1);
        	Vacation v = dao.save(new Vacation(dto.getVacationNum(), dto.getMember(), dto.getType(), dto.getStartDate(), dto.getEndDate(), dto.getReason(), dto.getWdate(), dto.getStatus(), dto.getRstatus(), dto.getApproval1(), dto.getApproval2(), dto.getApproval1rank(), dto.getApproval2rank(), dto.getApp1username(), dto.getApp2username()));
            return new VacationDto(v.getVacationNum(), v.getMember(), v.getType(), v.getStartDate(), v.getEndDate(), v.getReason(), v.getWdate(), v.getStatus(), v.getRstatus(), v.getApproval1(), v.getApproval2(), v.getApproval1rank(), v.getApproval2rank(), v.getApp1username(), v.getApp2username());
    	}
    	else if(dto.getRstatus()==0 &&dto.getStatus()==1 && dto.getApp2username().equals(mdto.getUsername())){
    		dto.setStatus(2);
    		
    		Vacation v = dao.save(new Vacation(dto.getVacationNum(), dto.getMember(), dto.getType(), dto.getStartDate(), dto.getEndDate(), dto.getReason(), dto.getWdate(), dto.getStatus(), dto.getRstatus(), dto.getApproval1(), dto.getApproval2(), dto.getApproval1rank(), dto.getApproval2rank(), dto.getApp1username(), dto.getApp2username()));
    		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.KOREA);
    		//DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.KOREA);
            String startDateString = v.getStartDate() + "T09:00:00.000Z";
            String endDateString = v.getEndDate() + "T18:00:00.000Z";
    		LocalDateTime startDate = LocalDateTime.parse(startDateString, dateTimeFormatter);
            LocalDateTime endDate = LocalDateTime.parse(endDateString, dateTimeFormatter);
            
            //기안자 연차일 감소
            MemberDto mdto2 = mservice.getMember(dto.getMember().getUsername());
            long daysBetween = ChronoUnit.DAYS.between(startDate, endDate);
            int remain = (int) daysBetween;
            System.out.println("remain = " + remain);
            mdto2.setRemain(mdto2.getRemain() - remain - 1);
            mservice.save(mdto2);
            /*ScheduleDto dto = ScheduleDto.builder().member(member).title(title).startDate(startDate).endDate(endDate)
                    .build();*/
            //캘린더에 추가
            ScheduleDto sdto = ScheduleDto.builder()
            		.member(dto.getMember()).title(dto.getMember().getName() + "" + dto.getMember().getCompanyRankName() + "휴가").startDate(startDate).endDate(endDate)
                    .build();
            sdao.save(new Schedule(sdto.getId(), sdto.getMember(), sdto.getTitle(), sdto.getStartDate(), sdto.getEndDate(), 1));
            return new VacationDto(v.getVacationNum(), v.getMember(), v.getType(), v.getStartDate(), v.getEndDate(), v.getReason(), v.getWdate(), v.getStatus(), v.getRstatus(), v.getApproval1(), v.getApproval2(), v.getApproval1rank(), v.getApproval2rank(), v.getApp1username(), v.getApp2username());
    	}
    	else {
    		
            PrintWriter out = response.getWriter();
            out.write("<script>alert('"+"결재할 수 없습니다."+"');location.href='"+"/approval/process"+"';</script>");
            out.flush();
    		return null;
    	}
    }
    
    public VacationDto refuseVacation(HttpServletResponse response, VacationDto dto, MemberDto mdto) throws IOException {
    	
    	if(dto.getRstatus()==0 && dto.getStatus()==0 && dto.getApp1username().equals(mdto.getUsername())){
    		
    		dto.setRstatus(-1);
    		Vacation v = dao.save(new Vacation(dto.getVacationNum(), dto.getMember(), dto.getType(), dto.getStartDate(), dto.getEndDate(), dto.getReason(), dto.getWdate(), dto.getStatus(), dto.getRstatus(), dto.getApproval1(), dto.getApproval2(), dto.getApproval1rank(), dto.getApproval2rank(), dto.getApp1username(), dto.getApp2username()));
            return new VacationDto(v.getVacationNum(), v.getMember(), v.getType(), v.getStartDate(), v.getEndDate(), v.getReason(), v.getWdate(), v.getStatus(), v.getRstatus(), v.getApproval1(), v.getApproval2(), v.getApproval1rank(), v.getApproval2rank(), v.getApp1username(), v.getApp2username());
    	}
    	else if(dto.getRstatus()==0 && dto.getStatus()==1 && dto.getApp2username().equals(mdto.getUsername())){
    		dto.setRstatus(-1);		
    		Vacation v = dao.save(new Vacation(dto.getVacationNum(), dto.getMember(), dto.getType(), dto.getStartDate(), dto.getEndDate(), dto.getReason(), dto.getWdate(), dto.getStatus(), dto.getRstatus(), dto.getApproval1(), dto.getApproval2(), dto.getApproval1rank(), dto.getApproval2rank(), dto.getApp1username(), dto.getApp2username()));
    		return new VacationDto(v.getVacationNum(), v.getMember(), v.getType(), v.getStartDate(), v.getEndDate(), v.getReason(), v.getWdate(), v.getStatus(), v.getRstatus(), v.getApproval1(), v.getApproval2(), v.getApproval1rank(), v.getApproval2rank(), v.getApp1username(), v.getApp2username());
    	}
    	else {
    		
            PrintWriter out = response.getWriter();
            out.write("<script>alert('"+"반려할 수 없습니다."+"');location.href='"+"/approval/process"+"';</script>");
            out.flush();
    		return null;
    	}
    	
    }
}
