package com.example.demo.chat;

import com.example.demo.member.MemberDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatRoomService {
    private final ChatRoomDao chatRoomDao;

    public ChatRoomDto create(ChatRoomDto chatRoomDto) {
        ChatRoom chatRoom = chatRoomDao.save(new ChatRoom().toEntity(chatRoomDto));
        return new ChatRoomDto().toDto(chatRoom);
    }
    public ChatRoomDto participate(ChatRoomDto chatRoomDto, MemberDto memberDto) {
        chatRoomDto.getParticipants().add(memberDto);
        ChatRoom chatRoom = chatRoomDao.save(new ChatRoom().toEntity(chatRoomDto));
        return new ChatRoomDto().toDto(chatRoom);
    }

    public List<ChatRoomDto> getByParticipantId(long participantId) {
        List<ChatRoom> list = chatRoomDao.findByParticipants_Id(participantId, Sort.by(Sort.Direction.DESC, "id"));
        return list.stream().map(ChatRoomDto::of).collect(Collectors.toList());
    }

    public ChatRoomDto getById(int id) {
        ChatRoom chatRoom = chatRoomDao.getById(id);
        return new ChatRoomDto().toDto(chatRoom);
    }

    public ChatRoomDto edit(ChatRoomDto chatRoomDto) {
        ChatRoom chatRoom = chatRoomDao.save(new ChatRoom().toEntity(chatRoomDto));
        return new ChatRoomDto().toDto(chatRoom);
    }
}
