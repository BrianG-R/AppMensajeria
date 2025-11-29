package com.example.appmensajeria;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class FirebaseDataSource {

    private final FirebaseAuth auth = FirebaseAuth.getInstance();

    private final DatabaseReference db =
            FirebaseDatabase.getInstance("https://appmensajes-51ec2-default-rtdb.firebaseio.com/")
                    .getReference();

    public String currentUid() {
        return auth.getCurrentUser() != null ? auth.getCurrentUser().getUid() : null;
    }

    public interface Callback {
        void onSuccess();
        void onFailure(String error);
    }

    public interface UsersCallback {
        void onUsers(List<UserEntity> users);
    }

    //====================================================
    // REGISTER USER
    //====================================================
    public void register(String name, String email, String password, Callback callback) {

        auth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener(a -> {

                    UserEntity user = new UserEntity();
                    user.uid = a.getUser().getUid();
                    user.name = name;
                    user.email = email;

                    db.child("users")
                            .child(user.uid)
                            .setValue(user)
                            .addOnSuccessListener(unused -> callback.onSuccess())
                            .addOnFailureListener(e -> callback.onFailure(e.getMessage()));

                })
                .addOnFailureListener(e -> callback.onFailure(e.getMessage()));
    }

    public void login(String email, String password, Callback callback) {
        auth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener(unused -> callback.onSuccess())
                .addOnFailureListener(e -> callback.onFailure(e.getMessage()));
    }

    //====================================================
    // GET USERS
    //====================================================
    public void loadUsers(UsersCallback callback) {

        db.child("users").get()
                .addOnSuccessListener(snapshot -> {
                    List<UserEntity> list = new ArrayList<>();

                    snapshot.getChildren().forEach(doc -> {
                        UserEntity u = doc.getValue(UserEntity.class);
                        list.add(u);
                    });

                    callback.onUsers(list);
                });
    }

    //====================================================
    // SAVE MESSAGE IN THE RIGHT PATH
    //====================================================
    public void saveMessage(String chatId, MessageEntity msg) {
        db.child("messages")
                .child(chatId)
                .push()
                .setValue(msg);
    }
}
