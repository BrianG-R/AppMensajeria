package com.example.appmensajeria;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.firebase.database.FirebaseDatabase;
import androidx.appcompat.app.AppCompatActivity;


public class OtherProfileFragment extends Fragment {

    private TextView tvName, tvApellido, tvEmail, tvBio, tvTelefono;
    private ImageView imgFoto;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_other_profile, container, false);

        tvName = v.findViewById(R.id.tvName);
        tvApellido = v.findViewById(R.id.tvApellido);
        tvEmail = v.findViewById(R.id.tvEmail);
        tvBio = v.findViewById(R.id.tvBio);
        tvTelefono = v.findViewById(R.id.tvTelefono);
        imgFoto = v.findViewById(R.id.imgFoto);

        // Recibir UID del usuario clickeado
        String uid = getArguments().getString("uid");

        FirebaseDatabase.getInstance()
                .getReference("users")
                .child(uid)
                .get()
                .addOnSuccessListener(snapshot -> {
                    UserEntity user = snapshot.getValue(UserEntity.class);
                    if (user != null) {

                        tvName.setText(user.name);
                        tvApellido.setText(user.apellido);
                        tvEmail.setText(user.email);
                        tvBio.setText(user.bio);
                        tvTelefono.setText(user.telefono);

                        if (user.fotoPerfil != null && !user.fotoPerfil.isEmpty()) {
                            Glide.with(this).load(user.fotoPerfil).into(imgFoto);
                        }
                    }
                });

        return v;
    }
    @Override
    public void onResume() {
        super.onResume();
        AppCompatActivity act = (AppCompatActivity) requireActivity();
        if (act.getSupportActionBar() != null) {
            act.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            act.getSupportActionBar().setTitle("Perfil");
        }
    }



}
