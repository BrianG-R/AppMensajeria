package com.example.appmensajeria;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ContactsFragment extends Fragment {

    private ContactsViewModel viewModel;
    private ContactsAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_contacts, container, false);

        ChatApp app = (ChatApp) requireActivity().getApplication();

        viewModel = new ViewModelProvider(
                this,
                new ContactsViewModelFactory(app.repo)
        ).get(ContactsViewModel.class);

        RecyclerView rv = v.findViewById(R.id.recyclerContacts);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ContactsAdapter(this::openChat);
        rv.setAdapter(adapter);

        viewModel.getContacts().observe(getViewLifecycleOwner(), users ->
                adapter.submitList(users)
        );

        viewModel.syncContacts();

        return v;
    }

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
}
