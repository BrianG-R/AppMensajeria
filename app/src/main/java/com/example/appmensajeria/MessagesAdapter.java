package com.example.appmensajeria;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Path;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.FirebaseDatabase;

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

    /** VIEW HOLDERS **/
    static class MeVH extends RecyclerView.ViewHolder{
        TextView tvText;
        ImageView imgUser;
        MeVH(View v){
            super(v);
            tvText = v.findViewById(R.id.tvText);
            imgUser = v.findViewById(R.id.imgUser);
        }
    }

    static class OtherVH extends RecyclerView.ViewHolder{
        TextView tvText;
        ImageView imgUser;
        OtherVH(View v){
            super(v);
            tvText = v.findViewById(R.id.tvText);
            imgUser = v.findViewById(R.id.imgUser);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return items.get(position).fromUid.equals(myUid) ? TYPE_ME : TYPE_OTHER;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if(viewType == TYPE_ME){
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
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int pos) {

        MessageEntity m = items.get(pos);

        if(holder instanceof MeVH){
            MeVH h = (MeVH) holder;
            h.tvText.setText(m.text);
            loadPhoto(m.fromUid, h.imgUser);

        } else {
            OtherVH h = (OtherVH) holder;
            h.tvText.setText(m.text);
            loadPhoto(m.fromUid, h.imgUser);
        }
    }

    /** 🔥 Cargar foto circular desde Firebase */
    private void loadPhoto(String uid, ImageView img){
        FirebaseDatabase.getInstance()
                .getReference("users").child(uid).child("fotoPerfil")
                .get()
                .addOnSuccessListener(s -> {
                    String b64 = String.valueOf(s.getValue());
                    if(b64 == null || b64.equals("null")) return;

                    byte[] decoded = Base64.decode(b64, Base64.DEFAULT);
                    Bitmap bmp = BitmapFactory.decodeByteArray(decoded,0,decoded.length);
                    img.setImageBitmap(circular(bmp));
                });
    }

    /** 🔄 Convertir a circular */
    private Bitmap circular(Bitmap src){
        int s = Math.min(src.getWidth(), src.getHeight());
        Bitmap output = Bitmap.createBitmap(s,s,Bitmap.Config.ARGB_8888);

        Canvas c = new Canvas(output);
        Path p = new Path();
        p.addCircle(s/2f,s/2f,s/2f,Path.Direction.CW);
        c.clipPath(p);
        c.drawBitmap(src,0,0,null);

        return output;
    }

    @Override
    public int getItemCount() { return items.size(); }

    private OnMessageClick listener;
    public interface OnMessageClick { void onClick(MessageEntity m); }

    public void setListener(OnMessageClick l){ this.listener = l; }
}
