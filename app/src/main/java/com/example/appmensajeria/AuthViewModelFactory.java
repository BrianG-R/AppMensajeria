package com.example.appmensajeria;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class AuthViewModelFactory implements ViewModelProvider.Factory {

    private final FirebaseDataSource firebase;

    public AuthViewModelFactory(FirebaseDataSource firebase) {
        this.firebase = firebase;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new AuthViewModel(firebase);
    }
}
