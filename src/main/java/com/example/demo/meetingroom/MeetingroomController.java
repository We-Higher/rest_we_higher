package com.example.demo.meetingroom;

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
@RequestMapping("/auth/meetingroom")
public class MeetingroomController {
    private final MeetingroomService service;

    //목록
    @GetMapping("")
    public ArrayList<Map<String, Object>> list() {
        ArrayList<MeetingroomDto> listAll = service.getAll();

        ArrayList<Map<String, Object>> result = new ArrayList<>();
        for (MeetingroomDto meetingroom : listAll) {
            Map<String, Object> map = new HashMap<>();
            map.put("cal_Id", meetingroom.getId());
            map.put("title", meetingroom.getRoomId()+"회의실-"+meetingroom.getTitle());
            map.put("start", meetingroom.getStartDate());
            map.put("end", meetingroom.getEndDate());
            map.put("roomId", meetingroom.getRoomId());
            result.add(map);
        }
        return result;
    }

    //일정추가
    @PostMapping("/{roomId}")
    @ResponseBody
    public Map addEvent(@RequestBody List<Map<String, Object>> param, @PathVariable("roomId") int roomId) throws Exception {
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
        Member user = (Member) authentication.getPrincipal();

        MeetingroomDto dto = MeetingroomDto.builder()
                .member(user).title(title).startDate(startDate).endDate(endDate).roomId(roomId)
                .build();
        MeetingroomDto s = service.save(dto);
        Map map = new HashMap();

        Map<String, Object> newEvnet = new HashMap<>();
        newEvnet.put("cal_Id", s.getId());
        newEvnet.put("title", s.getRoomId() + "회의실-" + s.getTitle());
        newEvnet.put("start", s.getStartDate());
        newEvnet.put("end", s.getEndDate());
        newEvnet.put("roomId", s.getRoomId());
        return map;
    }


    //일정삭제
    @DeleteMapping("/{id}")
    @ResponseBody
    public Map del(@PathVariable("id") int id) {
        MeetingroomDto origin = service.get(id);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Member user = (Member) authentication.getPrincipal();
        MemberDto mdto = new MemberDto().toDto(user);

        Map map = new HashMap();

        boolean flag = true;

        if(mdto.getId().equals(origin.getMember().getId())){
            service.del(id);
        }
        else if(mdto.getIsMaster()==1){
            service.del(id);
        }
        else{
            String msg = "본인 예약만 삭제할 수 있습니다.";
            map.put("msg",msg);
            flag = false;
        }
        map.put("flag",flag);

        return map;
    }

    //수정
    @PutMapping("/{id}/{roomId}")
    @ResponseBody
    public ModelMap modifyEvent(@PathVariable("id") int id, @PathVariable("roomId") int roomId, @RequestBody List<Map<String, Object>> param) {

        ModelMap map = new ModelMap();
        boolean flag = true;

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.KOREA);

        MeetingroomDto origin = service.get(id);

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
        Member user = (Member) authentication.getPrincipal();

        MeetingroomDto dto = MeetingroomDto.builder()
                .id(id).member(user).title(origin.getTitle()).startDate(startDate).endDate(endDate).roomId(roomId)
                .build();

        if (user.getId().equals(origin.getMember().getId())) {
            MeetingroomDto s = service.save(dto);

            map.put("cal_Id", s.getId());
            map.put("title", s.getRoomId()+"회의실-"+s.getTitle());
            map.put("start", s.getStartDate());
            map.put("end", s.getEndDate());

        } else if (user.getIsMaster() == 1) {
            MeetingroomDto s = service.save(dto);

            map.put("cal_Id", s.getId());
            map.put("title", s.getRoomId()+"회의실-"+s.getTitle());
            map.put("start", s.getStartDate());
            map.put("end", s.getEndDate());
            map.put("roomId", s.getRoomId());
        } else {
            String msg = "본인 예약만 수정할 수 있습니다.";
            map.put("msg", msg);
            flag = false;
        }
        map.put("flag", flag);

        return map;
    }

    //방 아이디
    @GetMapping("/{roomId}")
    @ResponseBody
    public ArrayList<Map<String, Object>> findbyroomId( @PathVariable("roomId") int roomId) {

        ArrayList<MeetingroomDto> listAll=null;
        if(roomId == 0){
            listAll = service.getAll();
        } else{
            listAll = service.getByRoomId(roomId);
        }

        ArrayList<Map<String, Object>> result = new ArrayList<>();
        for (MeetingroomDto meetingroom : listAll) {
            Map<String, Object> map = new HashMap<>();
            map.put("cal_Id", meetingroom.getId());
            map.put("title", meetingroom.getRoomId()+"회의실-"+meetingroom.getTitle());
            map.put("start", meetingroom.getStartDate());
            map.put("end", meetingroom.getEndDate());
            map.put("roomId", meetingroom.getRoomId());
            result.add(map);
        }
        System.out.println("result = " + result);
        return result;
    }
}