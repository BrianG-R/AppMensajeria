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

    public interface OnContactClick {
        void onClick(UserEntity user);
    }

    private final List<UserEntity> items = new ArrayList<>();
    private final OnContactClick clickListener;

    public ContactsAdapter(OnContactClick clickListener) {
        this.clickListener = clickListener;
    }

    public void submitList(List<UserEntity> list) {
        items.clear();
        items.addAll(list);
        notifyDataSetChanged();
    }

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

        VH holder = new VH(v);
        v.setOnClickListener(view -> {
            int pos = holder.getAdapterPosition();
            if (pos != RecyclerView.NO_POSITION)
                clickListener.onClick(items.get(pos));
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        holder.tvName.setText(items.get(position).name);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
