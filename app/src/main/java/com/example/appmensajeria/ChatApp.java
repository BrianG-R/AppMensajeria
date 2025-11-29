package com.example.appmensajeria;

import android.app.Application;

import androidx.room.Room;

import com.google.firebase.database.FirebaseDatabase;

public class ChatApp extends Application {

    public AppDatabase database;
    public FirebaseDataSource firebase;
    public ChatRepository repo;

    @Override
    public void onCreate() {
        super.onCreate();

        // habilitar modo offline y cache necesario
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        database = Room.databaseBuilder(
                this,
                AppDatabase.class,
                "chat-db"
        ).build();

        firebase = new FirebaseDataSource();

        repo = new ChatRepository(this, database, firebase);
    }
}
