package com.example.demo.chat;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatMessageService {
    private final MessageDao messageDao;

    public ChatMessage create(ChatMessage chatMessage) {
        return messageDao.save(chatMessage);
    }

    public List<ChatMessage> getByRoomId(int roomId) {
        return messageDao.findByRoom_Id(roomId, Sort.by(Sort.Direction.ASC, "id"));
    }
}
