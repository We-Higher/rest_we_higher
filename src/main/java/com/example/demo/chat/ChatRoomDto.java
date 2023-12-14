package com.example.demo.chat;

import com.example.demo.member.MemberDto;
import lombok.*;

import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatRoomDto {
    int id;
    private String roomId;
    private String roomName;
    Set<MemberDto> participants;

    public ChatRoomDto toDto(ChatRoom chatRoom) {
        return new ChatRoomDto(
                chatRoom.getId(),
                chatRoom.getRoomId(),
                chatRoom.getRoomName(),
                chatRoom.getParticipants().stream().map(MemberDto::of).collect(Collectors.toSet())
        );
    }

    public static ChatRoomDto of(ChatRoom chatRoom) {
        return new ChatRoomDto().toDto(chatRoom);
    }

//    public static ChatRoom create(int memberId) {
//        ChatRoom chatRoom = new ChatRoom();
//        chatRoom.roomId = UUID.randomUUID().toString();
//        return chatRoom;
//    }
}