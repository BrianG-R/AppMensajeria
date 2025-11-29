package com.example.appmensajeria;

import android.content.Context;
import androidx.lifecycle.LiveData;
import com.google.firebase.database.*;

import java.util.List;

public class ChatRepository {

    private final AppDatabase db;
    private final FirebaseDataSource firebase;

    public ChatRepository(Context ctx, AppDatabase db, FirebaseDataSource f) {
        this.db = db;
        this.firebase = f;

        // MQTT listener
        MqttManager.init((topic, msg) -> {

            MessageEntity m = new MessageEntity();
            m.chatId = topic.replace("chat/", "");
            m.text = msg;
            m.toUid = firebase.currentUid();
            m.timestamp = System.currentTimeMillis();

            new Thread(() -> db.messageDao().insert(m)).start();
        });
    }

    public LiveData<List<MessageEntity>> messages(String chatId) {

        FirebaseDatabase.getInstance(
                        "https://appmensajes-51ec2-default-rtdb.firebaseio.com/")
                .getReference()
                .child("messages")
                .child(chatId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {

                        new Thread(() -> {

                            for (DataSnapshot d : snapshot.getChildren()) {

                                MessageEntity m = d.getValue(MessageEntity.class);

                                if (m != null) {

                                    // evita duplicados
                                    if (db.messageDao().exists(m.timestamp) == 0) {
                                        db.messageDao().insert(m);
                                    }
                                }
                            }

                        }).start();
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {}
                });

        MqttManager.subscribe(chatId, (t, m) -> {});

        return db.messageDao().getMessages(chatId);
    }

    public void send(String chatId, String toUid, String text) {

        MessageEntity m = new MessageEntity();
        m.chatId = chatId;
        m.toUid = toUid;
        m.fromUid = firebase.currentUid();
        m.text = text;
        m.timestamp = System.currentTimeMillis();

        // Save local
        new Thread(() -> db.messageDao().insert(m)).start();

        // Save cloud
        firebase.saveMessage(chatId, m);

        // MQTT publish
        MqttManager.publish(chatId, text);
    }

    public LiveData<List<UserEntity>> users() {
        return db.userDao().getAllUsers();
    }

    public void syncUsersFromCloud() {
        firebase.loadUsers(users -> {
            new Thread(() -> {
                UserDao dao = db.userDao();
                for (UserEntity u : users) {
                    dao.insert(u);
                }
            }).start();
        });
    }
    public void deleteMessage(MessageEntity m) {

        // borrar local
        new Thread(() -> db.messageDao().delete(m)).start();

        // borrar en Realtime
        FirebaseDatabase.getInstance(
                        "https://appmensajes-51ec2-default-rtdb.firebaseio.com/")
                .getReference()
                .child("messages")
                .child(m.chatId)
                .orderByChild("timestamp")
                .equalTo(m.timestamp)
                .get()
                .addOnSuccessListener(snap -> {
                    snap.getChildren().forEach(d -> d.getRef().removeValue());
                });
    }

}
