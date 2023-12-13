package com.example.demo.schedule;

import com.example.demo.member.Member;
import com.example.demo.member.MemberService;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@PreAuthorize("isAuthenticated()")
@RequiredArgsConstructor
@Controller
@RequestMapping("/schedule")
public class ScheduleController {
    private final ScheduleService service;
    private final MemberService memberService;
    private final ScheduleDao dao;

    @RequestMapping("")
    public String schedule() {
        return "schedule/calendar";
    }

    // 목록
    @RequestMapping("/list")
    @ResponseBody
    public ArrayList<Map<String, Object>> list(ModelMap map) {// map은 자동으로 뷰페이지로 전달됨
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Member member = (Member) authentication.getPrincipal();
        if (member.getIsMaster() == 1) {

            ArrayList<ScheduleDto> listAll3 = service.getAll();

            JSONObject jsonObj = new JSONObject();
            JSONArray jsonArr = new JSONArray();

            HashMap<String, Object> hash = new HashMap<>();
            for (int i = 0; i < listAll3.size(); i++) {
                hash.put("cal_Id", listAll3.get(i).getId());
                hash.put("title", listAll3.get(i).getTitle());
                hash.put("start", listAll3.get(i).getStartDate());
                hash.put("end", listAll3.get(i).getEndDate());
                hash.put("check", listAll3.get(i).getCnt());
                jsonObj = new JSONObject(hash);
                jsonArr.add(jsonObj);
            }
            return jsonArr;
        } else {
            ArrayList<ScheduleDto> listAll = service.getByMemberOrCnt(member, 1);
            //ArrayList<ScheduleDto> listAll2 = service.getByIsNotice(1);

            JSONObject jsonObj = new JSONObject();
            JSONArray jsonArr = new JSONArray();

            HashMap<String, Object> hash = new HashMap<>();
            //HashMap<String, Object> hash2 = new HashMap<>();

            for (int i = 0; i < listAll.size(); i++) {
                hash.put("cal_Id", listAll.get(i).getId());
                hash.put("title", listAll.get(i).getTitle());
                hash.put("start", listAll.get(i).getStartDate());
                hash.put("end", listAll.get(i).getEndDate());
                hash.put("check", listAll.get(i).getCnt());
                jsonObj = new JSONObject(hash);
                jsonArr.add(jsonObj);
            }

            /*for (int i = 0; i < listAll2.size(); i++) {
                hash2.put("cal_Id", listAll2.get(i).getId());
                hash2.put("title", listAll2.get(i).getTitle());
                hash2.put("start", listAll2.get(i).getStartDate());
                hash2.put("end", listAll2.get(i).getEndDate());
                hash2.put("check", listAll2.get(i).getCnt());
                jsonObj = new JSONObject(hash2);
                jsonArr.add(jsonObj);
            }*/

            return jsonArr;
        }
    }

    // 일정추가
    @PostMapping("/add")
    @ResponseBody
    public Map addEvent(@RequestBody List<Map<String, Object>> param) throws Exception {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.KOREA);

        String title = (String) param.get(0).get("title");
        String startDateString = (String) param.get(0).get("start");
        String endDateString = (String) param.get(0).get("end");

        LocalDateTime startDate = LocalDateTime.parse(startDateString, dateTimeFormatter);
        LocalDateTime endDate = LocalDateTime.parse(endDateString, dateTimeFormatter);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Member member = (Member) authentication.getPrincipal();

        ScheduleDto dto = ScheduleDto.builder().member(member).title(title).startDate(startDate).endDate(endDate)
                .build();
        if (member.getIsMaster() == 1) {
            dto.setCnt(1);
        }
        ScheduleDto s = service.save(dto);

        Map<String, String> map = new HashMap<>();
        map.put("id", s.getId() + "");
        return map;
    }

    // 일정삭제
    @GetMapping("/del/{id}")
    @ResponseBody
    public ModelMap del(@PathVariable("id") int id) {

        Schedule entity = dao.findById(id).orElse(null);
        ScheduleDto origin = service.get(id);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Member loginMember = (Member) authentication.getPrincipal();
        ModelMap map = new ModelMap();

        boolean flag = true;

        if (origin.getCnt() == 0) {
            service.del(id);
        } else {
            if (loginMember.getIsMaster() == 0) {
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

    // 수정
    @PatchMapping("/edit/{id}")
    @ResponseBody
    public ModelMap modifyEvent(@PathVariable("id") int id, @RequestBody List<Map<String, Object>> param) {

        ModelMap map = new ModelMap();
        boolean flag = true;

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.KOREA);

        ScheduleDto origin = service.get(id);

        String startDateString = (String) param.get(0).get("start");
        String endDateString = (String) param.get(0).get("end");

        LocalDateTime startDate = LocalDateTime.parse(startDateString, dateTimeFormatter);
        LocalDateTime endDate = LocalDateTime.parse(endDateString, dateTimeFormatter);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Member member = (Member) authentication.getPrincipal();

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
            }
        }

            map.put("flag", flag);
            return map;
        }
    }