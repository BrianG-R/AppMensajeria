package com.example.appmensajeria;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.RectF;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.auth.FirebaseAuth;

import android.widget.Button;
import androidx.navigation.fragment.NavHostFragment;
import androidx.appcompat.app.AppCompatActivity;

public class ProfileFragment extends Fragment {

    private TextView tvName, tvApellido, tvEmail, tvBio, tvTelefono;
    private ImageView imgFoto;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_profile, container, false);

        tvName = v.findViewById(R.id.tvName);
        tvApellido = v.findViewById(R.id.tvApellido);
        tvEmail = v.findViewById(R.id.tvEmail);
        tvBio = v.findViewById(R.id.tvBio);
        tvTelefono = v.findViewById(R.id.tvTelefono);
        imgFoto = v.findViewById(R.id.imgFoto);

        Button btnEditar = v.findViewById(R.id.btnEditar);
        btnEditar.setOnClickListener(view ->
                NavHostFragment.findNavController(this)
                        .navigate(R.id.action_profileFragment_to_editProfileFragment)
        );

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

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
                            byte[] decoded = Base64.decode(user.fotoPerfil, Base64.DEFAULT);
                            Bitmap bitmap = BitmapFactory.decodeByteArray(decoded, 0, decoded.length);
                            imgFoto.setImageBitmap(makeCircular(bitmap));
                        }
                    }
                });

        return v;
    }

    // --- 🔥 Convertir foto a circular ---
    private Bitmap makeCircular(Bitmap src){
        int size = Math.min(src.getWidth(), src.getHeight());
        Bitmap output = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(output);
        Path path = new Path();
        path.addOval(new RectF(0,0,size,size), Path.Direction.CW);
        canvas.clipPath(path);
        canvas.drawBitmap(src, -(src.getWidth()-size)/2f, -(src.getHeight()-size)/2f, null);

        return output;
    }

    @Override
    public void onResume() {
        super.onResume();
        AppCompatActivity act = (AppCompatActivity) requireActivity();
        if (act.getSupportActionBar() != null) {
            act.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            act.getSupportActionBar().setTitle("Mi perfil");
        }
    }
}
