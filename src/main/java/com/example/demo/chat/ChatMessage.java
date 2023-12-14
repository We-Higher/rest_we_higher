package com.example.demo.chat;

import com.example.demo.member.Member;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class ChatMessage {
    public enum MessageType {
        ENTER, TALK,
    }
    @Id
    @SequenceGenerator(name = "seq_gen", sequenceName = "seq_chatmessage1", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_gen")
    private long id;
    // 메시지 타입 : 입장, 채팅
    private MessageType type; // 메시지 타입
    @ManyToOne
    private ChatRoom room; // 채팅방
    @ManyToOne
    @JoinColumn(nullable=false)//member(id)에 조인. 널 허용 안함
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Member sender; // 메시지 보낸사람
    private String message; // 메시지
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp; // 보낸시간
}