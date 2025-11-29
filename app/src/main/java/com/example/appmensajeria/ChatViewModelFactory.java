package com.example.appmensajeria;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class ChatViewModelFactory implements ViewModelProvider.Factory {

    private final ChatRepository repo;
    private final String chatId;
    private final String toUid;

    public ChatViewModelFactory(ChatRepository repo, String chatId, String toUid) {
        this.repo = repo;
        this.chatId = chatId;
        this.toUid = toUid;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new ChatViewModel(repo, chatId, toUid);
    }
}
