package com.example.appmensajeria;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.app.AppCompatActivity;


public class ChatFragment extends Fragment {

    private ChatViewModel viewModel;
    private MessagesAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        AppCompatActivity act = (AppCompatActivity) requireActivity();
        if (act.getSupportActionBar() != null) {
            act.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }


        setHasOptionsMenu(true);

        View v = inflater.inflate(R.layout.fragment_chat, container, false);

        Bundle args = getArguments();
        String chatId = args.getString("chatId");
        String toUid = args.getString("toUid");
        String name = args.getString("name");

        requireActivity().setTitle(name);

        ChatApp app = (ChatApp) requireActivity().getApplication();
        String myUid = app.firebase.currentUid();

        viewModel = new ViewModelProvider(
                this,
                new ChatViewModelFactory(app.repo, chatId, toUid)
        ).get(ChatViewModel.class);

        RecyclerView rv = v.findViewById(R.id.recyclerMessages);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new MessagesAdapter(myUid);
        rv.setAdapter(adapter);

        EditText etMessage = v.findViewById(R.id.etMessage);
        Button btnSend = v.findViewById(R.id.btnSend);

        btnSend.setOnClickListener(view -> {
            String text = etMessage.getText().toString();
            viewModel.send(text);
            etMessage.setText("");
        });

        viewModel.getMessages().observe(getViewLifecycleOwner(), list -> {
            adapter.submitList(list);
            if (list != null && !list.isEmpty())
                rv.scrollToPosition(list.size() - 1);
        });

        // <<--- ESTA PARTE DEBE IR DENTRO DEL onCreateView
        adapter.setListener(msg -> {
            new AlertDialog.Builder(getContext())
                    .setTitle("Borrar mensaje?")
                    .setMessage(msg.text)
                    .setPositiveButton("Eliminar", (d, w) -> viewModel.delete(msg))
                    .setNegativeButton("Cancelar", null)
                    .show();
        });

        return v;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            requireActivity().onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

