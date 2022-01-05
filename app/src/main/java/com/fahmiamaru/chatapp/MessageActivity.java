package com.fahmiamaru.chatapp;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.fahmiamaru.chatapp.Adapter.MessageAdapter;
import com.fahmiamaru.chatapp.Model.Chat;
import com.fahmiamaru.chatapp.Model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageActivity extends AppCompatActivity {

    CircleImageView profile_img;
    TextView username;

    FirebaseUser fuser;
    DatabaseReference reference;

    ImageButton btn_Send;
    EditText text_Send;
    User user;

    Intent intent;

    MessageAdapter messageAdapter;
    List<Chat> mchat;

    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        profile_img = findViewById(R.id.profile_img);
        username = findViewById(R.id.username);
        btn_Send = findViewById(R.id.btn_send);
        text_Send = findViewById(R.id.text_send);

        intent = getIntent();

        String userid = intent.getStringExtra("userid");
        fuser = FirebaseAuth.getInstance().getCurrentUser();

        btn_Send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = text_Send.getText().toString();
                if (!msg.equals("")){
                    sendMessage(fuser.getUid(), userid, msg);
                }else {
                    Toast.makeText(MessageActivity.this, "Type something", Toast.LENGTH_SHORT).show();
                }
                text_Send.setText("");
            }
        });

        reference = FirebaseDatabase.getInstance().getReference().child("Users").child(userid);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot item : snapshot.getChildren()){
                    User user = item.getValue(User.class);
                    user.setKey(item.getKey());
                    username.setText(user.getUsername());
                    if (user.getImageURL().equals("default")){
                        profile_img.setImageResource(R.mipmap.ic_launcher);
                    } else {
                        Glide.with(MessageActivity.this).load(user.getImageURL()).into(profile_img);
                    }

                    readMessage(fuser.getUid(), userid , user.getImageURL());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w(TAG, "loadPost:onCancelled", error.toException());
            }
        });

    }

    private void sendMessage (String sender, String receiver, String message){
        DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference();

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("sender", sender);
        hashMap.put("receiver", receiver);
        hashMap.put("message", message);

        reference1.child("Chats").push().setValue(hashMap);

    }

    private void readMessage(String myid, String userid, String imageurl){
        mchat = new ArrayList<>();
        reference = FirebaseDatabase.getInstance().getReference().child("Chats");
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mchat.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                        Chat chat = dataSnapshot.getValue(Chat.class);
                        if (chat.getReceiver().equals(myid) && chat.getSender().equals(userid)||
                                chat.getReceiver().equals(userid)&& chat.getSender().equals(myid)){
                            mchat.add(chat);
                        }

                        messageAdapter = new MessageAdapter(MessageActivity.this, mchat, imageurl);
                        recyclerView.setAdapter(messageAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        reference.addValueEventListener(valueEventListener);
    }
}