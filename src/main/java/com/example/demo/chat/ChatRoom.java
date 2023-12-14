package com.example.demo.chat;

import com.example.demo.member.Member;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatRoom {
    @Id
    @SequenceGenerator(name = "seq_gen", sequenceName = "seq_chatroom1", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_gen")
    int id;
    private String roomId;
    private String roomName;

    @ManyToMany
    @JoinColumn(nullable=false)//member(id)에 조인. 널 허용 안함
    @OnDelete(action = OnDeleteAction.CASCADE)
    Set<Member> participants;

    public ChatRoom toEntity(ChatRoomDto chatRoomDto) {
        return new ChatRoom(chatRoomDto.getId(), chatRoomDto.getRoomId(), chatRoomDto.getRoomName(), chatRoomDto.getParticipants().stream().map(Member::of).collect(Collectors.toSet()));
    }

//    public static ChatRoom create(int memberId) {
//        ChatRoom chatRoom = new ChatRoom();
//        chatRoom.roomId = UUID.randomUUID().toString();
//        return chatRoom;
//    }
}