package com.example.appmensajeria;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

public class ChatViewModel extends ViewModel {

    private final ChatRepository repo;
    private final String chatId;
    private final String toUid;

    private final LiveData<List<MessageEntity>> messages;

    public ChatViewModel(ChatRepository repo, String chatId, String toUid) {
        this.repo = repo;
        this.chatId = chatId;
        this.toUid = toUid;
        this.messages = repo.messages(chatId);
    }

    public LiveData<List<MessageEntity>> getMessages() {
        return messages;
    }

    public void send(String text) {
        if (text == null || text.trim().isEmpty()) return;
        repo.send(chatId, toUid, text);
    }
    public void delete(MessageEntity m) {
        repo.deleteMessage(m);
    }

}
