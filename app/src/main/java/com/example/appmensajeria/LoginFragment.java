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

public class LoginFragment extends Fragment {

    private AuthViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_login, container, false);

        ChatApp app = (ChatApp) requireActivity().getApplication();

        viewModel = new ViewModelProvider(
                this,
                new AuthViewModelFactory(app.firebase)
        ).get(AuthViewModel.class);

        EditText etEmail = v.findViewById(R.id.etEmail);
        EditText etPassword = v.findViewById(R.id.etPassword);
        Button btnLogin = v.findViewById(R.id.btnLogin);
        Button btnGotoRegister = v.findViewById(R.id.btnGotoRegister);

        btnLogin.setOnClickListener(view -> {
            String email = etEmail.getText().toString().trim();
            String pass = etPassword.getText().toString().trim();
            viewModel.login(email, pass);
        });

        btnGotoRegister.setOnClickListener(view ->
                NavHostFragment.findNavController(this)
                        .navigate(R.id.action_loginFragment_to_registerFragment));

        viewModel.authResult.observe(getViewLifecycleOwner(), result -> {
            if ("OK".equals(result)) {
                NavHostFragment.findNavController(this)
                        .navigate(R.id.action_loginFragment_to_contactsFragment);
            } else {
                Toast.makeText(getContext(), result, Toast.LENGTH_SHORT).show();
            }
        });

        return v;
    }
}
