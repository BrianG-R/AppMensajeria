package com.example.appmensajeria;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(
        entities = {MessageEntity.class, UserEntity.class},
        version = 1
)
public abstract class AppDatabase extends RoomDatabase {

    public abstract MessageDao messageDao();
    public abstract UserDao userDao();
}
