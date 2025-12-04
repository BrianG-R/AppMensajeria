package com.example.appmensajeria;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import androidx.appcompat.app.AppCompatActivity;

public class EditProfileFragment extends Fragment {

    private static final int PICK_IMAGE = 100;

    private ImageView imgFotoPerfil;
    private String base64Image = null;

    private EditText etNombre, etApellido, etBio, etTelefono;
    private Button btnGuardar, btnCambiarFoto;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_edit_profile, container, false);

        imgFotoPerfil = v.findViewById(R.id.imgFotoPerfil);
        btnCambiarFoto = v.findViewById(R.id.btnCambiarFoto);

        etNombre = v.findViewById(R.id.etNombre);
        etApellido = v.findViewById(R.id.etApellido);
        etBio = v.findViewById(R.id.etBio);
        etTelefono = v.findViewById(R.id.etTelefono);
        btnGuardar = v.findViewById(R.id.btnGuardar);

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // Cargar datos del perfil
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

                        if (u.fotoPerfil != null && !u.fotoPerfil.isEmpty()) {
                            byte[] decoded = Base64.decode(u.fotoPerfil, Base64.DEFAULT);
                            Bitmap bitmap = BitmapFactory.decodeByteArray(decoded, 0, decoded.length);
                            imgFotoPerfil.setImageBitmap(bitmap);
                        }
                    }
                });

        // Botón de elegir imagen
        btnCambiarFoto.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent, PICK_IMAGE);
        });

        // Botón guardar
        btnGuardar.setOnClickListener(v2 -> {
            FirebaseDatabase db = FirebaseDatabase.getInstance();

            db.getReference("users").child(uid).child("name")
                    .setValue(etNombre.getText().toString());

            db.getReference("users").child(uid).child("apellido")
                    .setValue(etApellido.getText().toString());

            db.getReference("users").child(uid).child("bio")
                    .setValue(etBio.getText().toString());

            db.getReference("users").child(uid).child("telefono")
                    .setValue(etTelefono.getText().toString());

            if (base64Image != null) {
                db.getReference("users").child(uid).child("fotoPerfil")
                        .setValue(base64Image);
            }

            NavHostFragment.findNavController(this).navigateUp();
        });

        return v;
    }

    // Convertir imagen seleccionada a Base64
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK && data != null) {
            Uri uri = data.getData();
            try {
                InputStream is = requireActivity().getContentResolver().openInputStream(uri);
                Bitmap bitmap = BitmapFactory.decodeStream(is);
                imgFotoPerfil.setImageBitmap(bitmap);

                // Convertir a base64
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 60, baos);
                byte[] bytes = baos.toByteArray();
                base64Image = Base64.encodeToString(bytes, Base64.DEFAULT);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        AppCompatActivity act = (AppCompatActivity) requireActivity();
        act.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        act.getSupportActionBar().setTitle("Editar Perfil");
    }
}
