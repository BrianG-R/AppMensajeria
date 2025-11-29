package com.example.appmensajeria;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

public class ContactsViewModel extends ViewModel {

    private final ChatRepository repo;

    public ContactsViewModel(ChatRepository repo) {
        this.repo = repo;
    }

    public LiveData<List<UserEntity>> getContacts() {
        return repo.users();
    }

    public void syncContacts() {
        repo.syncUsersFromCloud();
    }
}
