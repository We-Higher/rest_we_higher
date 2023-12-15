package com.example.demo.chat;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@RequiredArgsConstructor
@Controller
public class ChatController {

    private final SimpMessageSendingOperations messagingTemplate;
    private final ChatMessageService chatMessageService;

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
        return cm;
    }
}