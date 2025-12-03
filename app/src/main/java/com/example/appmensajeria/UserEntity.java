package com.example.appmensajeria;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;


@Entity(tableName = "users")
public class UserEntity {

    @PrimaryKey
    @NonNull
    public String uid;

    public String name;
    public String email;
    public String apellido;
    public String bio;
    public String fotoPerfil;
    public String telefono;

    public UserEntity() {}
}
