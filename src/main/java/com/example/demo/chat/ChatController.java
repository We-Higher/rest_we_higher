package com.example.demo.chat;

import com.example.demo.member.Member;
import com.example.demo.member.MemberDto;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Objects;
import java.util.Set;

@RequiredArgsConstructor
@Controller
public class ChatController {

    private final SimpMessageSendingOperations messagingTemplate;
    private final ChatMessageService chatMessageService;
    private final ChatRoomService chatRoomService;

    @MessageMapping("/chat/message")
    public void message(ChatMessageDto chatMessageDto) {
        System.out.println("chatMessageDto = " + chatMessageDto);
//        messagingTemplate.convertAndSend("/sub/chat/room/"+chatMessageDto.getRoomId(), chatMessageDto);
    }

    @PostMapping("/chat/message/add")
    @ResponseBody
    public ChatMessage addMessage(ChatMessage chatMessage) {
        ChatMessage cm = chatMessageService.create(chatMessage);
        messagingTemplate.convertAndSend("/sub/chat/room/"+cm.getRoom().getId(), cm);
        Set<Member> members = cm.getRoom().getParticipants();
        members.forEach(m -> {
            if (!Objects.equals(m.getId(), cm.getSender().getId())) {
                messagingTemplate.convertAndSend("/sub/alarm/" + m.getId(), cm);
            }
        });
        return cm;
    }
}