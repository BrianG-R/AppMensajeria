package com.example.appmensajeria;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.VH> {

    public interface OnChatClick {
        void onChat(UserEntity user);
    }

    private final List<UserEntity> items = new ArrayList<>();
    private final OnChatClick chatListener;

    public ContactsAdapter(OnChatClick chatListener) {
        this.chatListener = chatListener;
    }

    public void submitList(List<UserEntity> list) {
        items.clear();
        items.addAll(list);
        notifyDataSetChanged();
    }

    // 🔹 ViewHolder
    static class VH extends RecyclerView.ViewHolder {
        TextView tvName;

        public VH(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
        }
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_item_contact, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {

        UserEntity user = items.get(position);
        holder.tvName.setText(user.name);

        // 👉 Tocando TODO el item se abre el chat
        holder.itemView.setOnClickListener(v -> chatListener.onChat(user));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
