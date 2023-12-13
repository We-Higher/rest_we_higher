package com.example.demo.schedule;

import com.example.demo.member.Member;
import com.example.demo.member.MemberDto;
import com.example.demo.member.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;


@RequiredArgsConstructor
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/auth/schedule")
public class ScheduleController {
    private final ScheduleService service;
    private final MemberService memberService;
    private final ScheduleDao dao;

    // 목록
    @GetMapping("")
    public ArrayList<Map<String, Object>> list() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String id = authentication.getName();
        MemberDto mdto = memberService.getMember(id);

        ArrayList<ScheduleDto> scheduleList = (mdto.getIsMaster() == 1) ? service.getAll() : service.getByMemberOrCnt(new Member().toEntity(mdto), 1);

        ArrayList<Map<String, Object>> result = new ArrayList<>();
        for (ScheduleDto schedule : scheduleList) {
            Map<String, Object> map = new HashMap<>();
            map.put("cal_Id", schedule.getId());
            map.put("title", schedule.getTitle());
            map.put("start", schedule.getStartDate());
            map.put("end", schedule.getEndDate());
            map.put("check", schedule.getCnt());
            result.add(map);
        }
        return result;
    }


    // 일정추가
    @PostMapping("")
    @ResponseBody
    public Map addEvent(@RequestBody List<Map<String, Object>> param) throws Exception {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.KOREA);

        String title = (String) param.get(0).get("title");

        Long startTimestamp = (Long) param.get(0).get("start");
        Instant sInstant = Instant.ofEpochMilli(startTimestamp);
        ZonedDateTime zonedDateTimeS = sInstant.atZone(ZoneId.systemDefault());
        String startDateString = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").format(zonedDateTimeS);

        Long endTimestamp = (Long) param.get(0).get("end");
        Instant eInstant = Instant.ofEpochMilli(endTimestamp);
        ZonedDateTime zonedDateTimeE = eInstant.atZone(ZoneId.systemDefault());
        String endDateString = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").format(zonedDateTimeE);

        LocalDateTime startDate = LocalDateTime.parse(startDateString, dateTimeFormatter);
        LocalDateTime endDate = LocalDateTime.parse(endDateString, dateTimeFormatter);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String id = authentication.getName();
        MemberDto mdto = memberService.getMember(id);
        Member member = new Member().toEntity(mdto);
        ScheduleDto dto = ScheduleDto.builder().member(member).title(title).startDate(startDate).endDate(endDate)
                .build();
        if (member.getIsMaster() == 1) {
            dto.setCnt(1);
        }
        ScheduleDto s = service.save(dto);
        Map map = new HashMap();

        Map<String, Object> newEvnet = new HashMap<>();
        newEvnet.put("cal_Id", s.getId());
        newEvnet.put("title", s.getTitle());
        newEvnet.put("start", s.getStartDate());
        newEvnet.put("end", s.getEndDate());
        newEvnet.put("check", s.getCnt());
        map.put("newEvent", newEvnet);
        return map;
    }

    // 수정
    @PutMapping("/{id}")
    @ResponseBody
    public Map modifyEvent(@PathVariable("id") int id, @RequestBody List<Map<String, Object>> param) {

        Map map = new HashMap();
        boolean flag = true;

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.KOREA);

        ScheduleDto origin = service.get(id);

        Long startTimestamp = (Long) param.get(0).get("start");
        Instant sInstant = Instant.ofEpochMilli(startTimestamp);
        ZonedDateTime zonedDateTimeS = sInstant.atZone(ZoneId.systemDefault());
        String startDateString = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").format(zonedDateTimeS);

        Long endTimestamp = (Long) param.get(0).get("end");
        Instant eInstant = Instant.ofEpochMilli(endTimestamp);
        ZonedDateTime zonedDateTimeE = eInstant.atZone(ZoneId.systemDefault());
        String endDateString = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").format(zonedDateTimeE);

        LocalDateTime startDate = LocalDateTime.parse(startDateString, dateTimeFormatter);
        LocalDateTime endDate = LocalDateTime.parse(endDateString, dateTimeFormatter);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        MemberDto mdto = memberService.getMember(username);
        Member member = new Member().toEntity(mdto);

        ScheduleDto dto = ScheduleDto.builder().member(member).id(id).title(origin.getTitle()).startDate(startDate)
                .endDate(endDate).cnt(origin.getCnt()).build();

        if (origin.getCnt() == 0) {
            origin = service.save(dto);
        } else {

            if (member.getIsMaster() == 0) {
                String msg = "관리자만 변경 가능합니다.";
                map.put("msg", msg);
                flag = false;
            } else {
                ScheduleDto s = service.save(dto);

                Map<String, Object> newEvnet = new HashMap<>();
                newEvnet.put("cal_Id", s.getId());
                newEvnet.put("title", s.getTitle());
                newEvnet.put("start", s.getStartDate());
                newEvnet.put("end", s.getEndDate());
                newEvnet.put("check", s.getCnt());
                map.put("newEvent", newEvnet);
            }
        }
        map.put("flag", flag);
        return map;
    }

    // 일정삭제
    @DeleteMapping("/{id}")
    @ResponseBody
    public Map del(@PathVariable("id") int id) {
        Schedule entity = dao.findById(id).orElse(null);
        ScheduleDto origin = service.get(id);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        MemberDto mdto = memberService.getMember(username);

        Map map = new HashMap();

        boolean flag = true;

        if (origin.getCnt() == 0) {
            service.del(id);
        } else {
            if (new Member().toEntity(mdto).getIsMaster() == 0) {
                String msg = "관리자만 삭제 가능합니다.";
                map.put("msg", msg);
                flag = false;
            } else {
                service.del(id);
            }
        }

        map.put("flag", flag);
        return map;
    }
}