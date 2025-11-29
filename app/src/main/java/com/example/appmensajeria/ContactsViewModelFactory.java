package com.example.appmensajeria;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class ContactsViewModelFactory implements ViewModelProvider.Factory {

    private final ChatRepository repo;

    public ContactsViewModelFactory(ChatRepository repo) {
        this.repo = repo;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new ContactsViewModel(repo);
    }
}
