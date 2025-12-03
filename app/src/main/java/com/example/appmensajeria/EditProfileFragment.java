package com.example.appmensajeria;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import androidx.appcompat.app.AppCompatActivity;




public class EditProfileFragment extends Fragment {

    private EditText etNombre, etApellido, etBio, etTelefono;
    private Button btnGuardar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_edit_profile, container, false);

        etNombre = v.findViewById(R.id.etNombre);
        etApellido = v.findViewById(R.id.etApellido);
        etBio = v.findViewById(R.id.etBio);
        etTelefono = v.findViewById(R.id.etTelefono);
        btnGuardar = v.findViewById(R.id.btnGuardar);

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // Cargar datos actuales
        FirebaseDatabase.getInstance()
                .getReference("users")
                .child(uid)
                .get()
                .addOnSuccessListener(snapshot -> {
                    UserEntity u = snapshot.getValue(UserEntity.class);
                    if (u != null) {
                        etNombre.setText(u.name);
                        etApellido.setText(u.apellido);
                        etBio.setText(u.bio);
                        etTelefono.setText(u.telefono);
                    }
                });

        // Guardar cambios
        btnGuardar.setOnClickListener(v2 -> {

            FirebaseDatabase.getInstance()
                    .getReference("users")
                    .child(uid)
                    .child("name").setValue(etNombre.getText().toString());

            FirebaseDatabase.getInstance()
                    .getReference("users")
                    .child(uid)
                    .child("apellido").setValue(etApellido.getText().toString());

            FirebaseDatabase.getInstance()
                    .getReference("users")
                    .child(uid)
                    .child("bio").setValue(etBio.getText().toString());

            FirebaseDatabase.getInstance()
                    .getReference("users")
                    .child(uid)
                    .child("telefono").setValue(etTelefono.getText().toString());

            // Volver atrás
            NavHostFragment.findNavController(EditProfileFragment.this)
                    .navigateUp();
        });

        return v;
    }
    @Override
    public void onResume() {
        super.onResume();
        AppCompatActivity act = (AppCompatActivity) requireActivity();
        act.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        act.getSupportActionBar().setTitle("Perfil");
    }


}
