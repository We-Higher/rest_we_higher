package com.example.demo.chat;

import com.example.demo.auth.SecurityMember;
import com.example.demo.member.Member;
import com.example.demo.member.MemberDto;
import com.example.demo.member.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.*;

//@PreAuthorize("isAuthenticated()")
@RequiredArgsConstructor
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/chat")
public class ChatRoomController {
    private final MemberService memberService;
    private final ChatRoomService chatRoomService;
    private final ChatRoomDao chatRoomDao;
    private final ChatMessageService chatMessageService;

    // 초대할 사람 목록
    @GetMapping("/invitation")
    public Map rooms() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        SecurityMember loginMember = (SecurityMember) authentication.getPrincipal();
        MemberDto mdto = memberService.getMember(loginMember.getUsername());

        List<MemberDto> mlist = memberService.getByIdNot(mdto.getId());

        Map hmap = new HashMap();

        hmap.put("mlist", mlist);

        return hmap;
    }

    // 참여한 채팅방 목록 반환
    @GetMapping("/room")
    @ResponseBody
    public Map getByParticipantId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        SecurityMember loginMember = (SecurityMember) authentication.getPrincipal();
        MemberDto mdto = memberService.getMember(loginMember.getUsername());

        Map map = new HashMap();
        map.put("rooms", chatRoomService.getByParticipantId(mdto.getId()));
        return map;
    }

    // 모든 채팅방 목록 반환
//    @GetMapping("/rooms")
//    @ResponseBody
//    public List<ChatRoomDto> room() {
//        return chatRoomRepository.findAllRoom();
//    }

    // 채팅방 생성
    @PostMapping("/room")
    public Map createRoom(@RequestBody ChatRoomDto request) {
        System.out.println("participants = " + request.getParticipants());
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        SecurityMember loginMember = (SecurityMember) authentication.getPrincipal();
        MemberDto mdto = memberService.getMember(loginMember.getUsername());

        request.getParticipants().add(mdto);

        ChatRoomDto chatRoomDto = ChatRoomDto.builder()
                .roomId(UUID.randomUUID().toString())
                .roomName(request.getRoomName())
                .participants(request.getParticipants())
                .build();

        ChatRoomDto room = chatRoomService.create(chatRoomDto);

        Map map = new HashMap();
        map.put("room", room);

        return map;
    }


    // 채팅방 detail
    @GetMapping("/room/{roomId}")
    public Map roomDetail(@PathVariable int roomId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        SecurityMember loginMember = (SecurityMember) authentication.getPrincipal();
        MemberDto mdto = memberService.getMember(loginMember.getUsername());

        ChatRoomDto chatRoomDto = chatRoomService.getById(roomId);
        List<ChatMessage> clist = chatMessageService.getByRoomId(roomId);
        List<MemberDto> nlist = memberService.getNonParticipantsMembers(roomId);

        Map map = new HashMap();

        map.put("user", mdto);
        map.put("chatRoom", chatRoomDto);
        map.put("clist", clist);
        map.put("nlist", nlist);
        return map;
    }

//    // 특정 채팅방 조회
//    @GetMapping("/room/{roomId}")
//    @ResponseBody
//    public ChatRoom roomInfo(@PathVariable int roomId) {
//        return chatRoomDao.getById(roomId);
//    }

    // 채팅방 나가기
    @PostMapping("/room/out/{roomId}")
    public Map roomOut(@PathVariable int roomId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        SecurityMember loginMember = (SecurityMember) authentication.getPrincipal();
        MemberDto mdto = memberService.getMember(loginMember.getUsername());
        boolean flag = true;

        ChatRoomDto chatRoomDto = chatRoomService.getById(roomId);
        chatRoomDto.getParticipants().removeIf(member -> member.getId().equals(mdto.getId()));

        Map map = new HashMap();
        map.put("room", chatRoomService.edit(chatRoomDto));

        return map;
    }

    // 채팅방 초대
    @PostMapping("/room/invite/{roomId}")
    public Map roomOut(@PathVariable int roomId, @RequestBody Set<MemberDto> invitation) {
        System.out.println("invitation = " + invitation);
        ChatRoomDto chatRoomDto = chatRoomService.getById(roomId);

        Set<MemberDto> s = new HashSet<>();
        s.addAll(chatRoomDto.getParticipants());
        s.addAll(invitation);
        chatRoomDto.setParticipants(s);

        Map map = new HashMap();
        map.put("room", chatRoomService.edit(chatRoomDto));

        return map;
    }
}