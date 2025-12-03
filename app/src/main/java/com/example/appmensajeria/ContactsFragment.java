package com.example.appmensajeria;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.app.AppCompatActivity;


public class ContactsFragment extends Fragment {

    private ContactsViewModel viewModel;
    private ContactsAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        // NECESARIO PARA MOSTRAR EL MENÚ DEL PERFIL
        setHasOptionsMenu(true);

        View v = inflater.inflate(R.layout.fragment_contacts, container, false);

        ChatApp app = (ChatApp) requireActivity().getApplication();

        viewModel = new ViewModelProvider(
                this,
                new ContactsViewModelFactory(app.repo)
        ).get(ContactsViewModel.class);

        RecyclerView rv = v.findViewById(R.id.recyclerContacts);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));

        // 👇 Ahora el adapter recibe 2 callbacks: chat y ver perfil
        adapter = new ContactsAdapter(this::openChat);

        rv.setAdapter(adapter);

        viewModel.getContacts().observe(getViewLifecycleOwner(), users ->
                adapter.submitList(users)
        );

        viewModel.syncContacts();

        return v;
    }

    // -------------------------------------------
    // MENÚ SUPERIOR (Ícono de Perfil propio)
    // -------------------------------------------

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_profile, menu); // carga el menú
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_profile) {

            // Navegar al ProfileFragment (perfil propio)
            NavHostFragment.findNavController(this)
                    .navigate(R.id.action_contactsFragment_to_profileFragment);

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // -------------------------------------------
    // ABRIR CHAT AL TOCAR UN CONTACTO
    // -------------------------------------------

    private void openChat(UserEntity user) {

        ChatApp app = (ChatApp) requireActivity().getApplication();
        String myUid = app.firebase.currentUid();
        if (myUid == null) return;

        String chatId = (myUid.compareTo(user.uid) < 0)
                ? myUid + "_" + user.uid
                : user.uid + "_" + myUid;

        Bundle args = new Bundle();
        args.putString("chatId", chatId);
        args.putString("toUid", user.uid);
        args.putString("name", user.name);

        NavHostFragment.findNavController(this)
                .navigate(R.id.action_contactsFragment_to_chatFragment, args);
    }

    // -------------------------------------------
    // VER PERFIL DE OTRO USUARIO (BOTÓN "Ver perfil")
    // -------------------------------------------

    private void openUserProfile(UserEntity user) {
        Bundle args = new Bundle();
        args.putString("uid", user.uid);

        // Navega al fragmento de perfil ajeno
        NavHostFragment.findNavController(this)
                .navigate(R.id.otherProfileFragment, args);
    }
    @Override
    public void onResume() {
        super.onResume();
        AppCompatActivity act = (AppCompatActivity) requireActivity();
        if (act.getSupportActionBar() != null) {
            act.getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            act.getSupportActionBar().setTitle("Contactos");
        }
    }

}


