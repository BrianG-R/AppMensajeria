package com.example.appmensajeria;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AuthViewModel extends ViewModel {

    private final FirebaseDataSource firebase;
    public MutableLiveData<String> authResult = new MutableLiveData<>();

    public AuthViewModel(FirebaseDataSource firebase) {
        this.firebase = firebase;
    }

    public void register(String name, String email, String pass) {
        firebase.register(name, email, pass, new FirebaseDataSource.Callback() {
            @Override
            public void onSuccess() {
                authResult.postValue("OK");
            }

            @Override
            public void onFailure(String error) {
                authResult.postValue(error);
            }
        });
    }

    public void login(String email, String pass) {
        firebase.login(email, pass, new FirebaseDataSource.Callback() {
            @Override
            public void onSuccess() {
                authResult.postValue("OK");
            }

            @Override
            public void onFailure(String error) {
                authResult.postValue(error);
            }
        });
    }
}
