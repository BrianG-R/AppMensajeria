package com.example.appmensajeria;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MessagesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_ME = 1;
    private static final int TYPE_OTHER = 2;

    private final String myUid;
    private final List<MessageEntity> items = new ArrayList<>();

    public MessagesAdapter(String myUid) {
        this.myUid = myUid;
    }

    public void submitList(List<MessageEntity> list) {
        items.clear();
        if (list != null) items.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        MessageEntity m = items.get(position);
        if (m.fromUid != null && m.fromUid.equals(myUid))
            return TYPE_ME;
        return TYPE_OTHER;
    }

    static class MeVH extends RecyclerView.ViewHolder {
        TextView tvText;
        MeVH(View v) {
            super(v);
            tvText = v.findViewById(R.id.tvText);
        }
    }

    static class OtherVH extends RecyclerView.ViewHolder {
        TextView tvText;
        OtherVH(View v) {
            super(v);
            tvText = v.findViewById(R.id.tvText);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent, int viewType) {

        if (viewType == TYPE_ME) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.fragment_item_message_me, parent, false);
            return new MeVH(v);

        } else {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.fragment_item_message_other, parent, false);
            return new OtherVH(v);
        }
    }


    @Override
    public void onBindViewHolder(
            @NonNull RecyclerView.ViewHolder holder, int position) {

        MessageEntity m = items.get(position);

        TextView tv;

        if (holder instanceof MeVH)
            tv = ((MeVH) holder).tvText;
        else
            tv = ((OtherVH) holder).tvText;

        tv.setText(m.text);

        // CLICK EVENT
        tv.setOnClickListener(v -> {
            if (listener != null) listener.onClick(m);
        });

        // LONG CLICK EVENT
        tv.setOnLongClickListener(v -> {
            if (listener != null) listener.onClick(m);
            return true;
        });
    }

    public interface OnMessageClick {
        void onClick(MessageEntity m);
    }

    private OnMessageClick listener;

    public void setListener(OnMessageClick l){
        this.listener = l;
    }


    @Override
    public int getItemCount() {
        return items.size();
    }
}
