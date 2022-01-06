package com.fahmiamaru.chatapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.fahmiamaru.chatapp.MessageActivity;
import com.fahmiamaru.chatapp.Model.Chat;
import com.fahmiamaru.chatapp.Model.User;
import com.fahmiamaru.chatapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    public static final int MSG_TYPE_LEFT = 0;
    public static final int MSG_TYPE_RIGHT = 1;
    private Context mContext;
    private List<Chat> mChat;
    private String imgurl;

    FirebaseUser firebaseUser;

    public MessageAdapter(Context mContext, List<Chat> mChat, String imgurl){
        this.mChat = mChat;
        this.mContext = mContext;
        this.imgurl = imgurl;
    }

    @NonNull
    @Override
    public MessageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if(viewType == MSG_TYPE_RIGHT){
            view = LayoutInflater.from(mContext).inflate(R.layout.chat_item_right, parent, false);
        }else {
            view = LayoutInflater.from(mContext).inflate(R.layout.chat_item_left, parent, false);
        }
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageAdapter.ViewHolder holder, int position) {
        Chat chat = mChat.get(position);
        holder.show_msg.setText(chat.getMessage());
        if (holder.profile_img != null){
            if (imgurl.equals("default")){
                holder.profile_img.setImageResource(R.mipmap.ic_launcher);
            }else {
                Glide.with(mContext).load(imgurl).into(holder.profile_img);
            }
        }
    }

    @Override
    public int getItemCount() {
        return mChat.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView show_msg;
        public ImageView profile_img;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            show_msg = itemView.findViewById(R.id.show_msg);
            profile_img = itemView.findViewById(R.id.profile_img);
        }
    }

    @Override
    public int getItemViewType(int position) {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (mChat.get(position).getSender().equals(firebaseUser.getUid())){
            return MSG_TYPE_RIGHT;
        } else {
            return MSG_TYPE_LEFT;
        }
    }
}
