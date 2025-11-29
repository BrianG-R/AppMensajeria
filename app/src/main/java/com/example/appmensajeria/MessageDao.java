package com.example.appmensajeria;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Delete;


import java.util.List;

@Dao
public interface MessageDao {

    @Insert
    void insert(MessageEntity m);

    @Query("SELECT COUNT(*) FROM messages WHERE timestamp = :time")
    int exists(long time);

    @Query("SELECT * FROM messages WHERE chatId = :chatId ORDER BY timestamp ASC")
    LiveData<List<MessageEntity>> getMessages(String chatId);

    @Delete
    void delete(MessageEntity m);

}
