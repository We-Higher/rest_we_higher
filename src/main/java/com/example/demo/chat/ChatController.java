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
    public void message(ChatMessageDto chatMessageDto, ModelMap map) {
//        if (ChatMessage.MessageType.ENTER.equals(chatMessageDto.getType()))
//            chatMessageDto.setMessage(chatMessageDto.getSender() + "님이 입장하셨습니다.");
        messagingTemplate.convertAndSend("/sub/chat/room/" + chatMessageDto.getRoomId(), chatMessageDto);
    }

    @PostMapping("/chat/message/add")
    @ResponseBody
    public ChatMessage addMessage(ChatMessage chatMessage, ModelMap map) {
        return chatMessageService.create(chatMessage);
    }
}