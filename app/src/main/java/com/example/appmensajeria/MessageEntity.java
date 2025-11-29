package com.example.appmensajeria;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "messages")
public class MessageEntity {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public String chatId;
    public String fromUid;
    public String toUid;
    public String text;
    public long timestamp;
}
