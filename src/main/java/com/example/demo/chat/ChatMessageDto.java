package com.example.demo.chat;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class ChatMessageDto {
    private long id;
    // 메시지 타입 : 입장, 채팅
    private ChatMessage.MessageType type; // 메시지 타입
    private int roomId; // 채팅방
    private String sender; // 메시지 보낸사람
    private String message; // 메시지
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp; // 보낸시간
    private String senderProfile;
}
