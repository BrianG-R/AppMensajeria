package com.example.appmensajeria;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

public class RegisterFragment extends Fragment {

    private AuthViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_register, container, false);

        ChatApp app = (ChatApp) requireActivity().getApplication();

        viewModel = new ViewModelProvider(
                this,
                new AuthViewModelFactory(app.firebase)
        ).get(AuthViewModel.class);

        EditText etName = v.findViewById(R.id.etName);
        EditText etEmail = v.findViewById(R.id.etEmail);
        EditText etPassword = v.findViewById(R.id.etPassword);
        Button btnRegister = v.findViewById(R.id.btnRegister);

        btnRegister.setOnClickListener(view ->
                viewModel.register(
                        etName.getText().toString().trim(),
                        etEmail.getText().toString().trim(),
                        etPassword.getText().toString().trim()
                )
        );

        viewModel.authResult.observe(getViewLifecycleOwner(), result -> {
            if ("OK".equals(result)) {
                Toast.makeText(getContext(), "Cuenta creada", Toast.LENGTH_SHORT).show();
                NavHostFragment.findNavController(this)
                        .navigate(R.id.action_registerFragment_to_loginFragment);
            } else {
                Toast.makeText(getContext(), result, Toast.LENGTH_SHORT).show();
            }
        });

        return v;
    }
}
