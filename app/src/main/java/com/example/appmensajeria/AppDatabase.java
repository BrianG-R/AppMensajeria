package com.example.appmensajeria;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(
        entities = {MessageEntity.class, UserEntity.class},
        version = 2,
        exportSchema = false
)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase INSTANCE;

    public abstract MessageDao messageDao();
    public abstract UserDao userDao();

    public static synchronized AppDatabase getDatabase(Context context) {
        if (INSTANCE == null) {

            INSTANCE = Room.databaseBuilder(
                            context.getApplicationContext(),
                            AppDatabase.class,
                            "appmensajeria.db"
                    )
                    .fallbackToDestructiveMigration()   // <-- AGREGA ESTO
                    .build();
        }
        return INSTANCE;
    }
}
