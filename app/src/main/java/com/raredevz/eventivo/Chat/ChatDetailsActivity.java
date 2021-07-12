package com.raredevz.eventivo.Chat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;

import com.raredevz.eventivo.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static com.raredevz.eventivo.Chat.Constants.NODE_IS_READ;
import static com.raredevz.eventivo.Chat.Constants.NODE_RECEIVER_ID;


public class ChatDetailsActivity extends AppCompatActivity {
    public static String KEY_FRIEND = "FRIEND";

    // give preparation animation activity transition
    public static void navigate(AppCompatActivity activity, View transitionImage, Friend obj) {
        Intent intent = new Intent(activity, ChatDetailsActivity.class);
        intent.putExtra(KEY_FRIEND, obj);

        activity.startActivity(intent);
    }

    private Button btn_send;
    private EditText et_content;
    public static ChatDetailsListAdapter mAdapter;

    private ListView listview;
    private ActionBar actionBar;
    private Friend friend;
    private ArrayList<ChatMessage> items = new ArrayList<>();
    private View parent_view;
    ParseFirebaseData pfbd;
    SettingsAPI set;

    String chatNode, chatNode_1, chatNode_2;

    DatabaseReference ref;
    ValueEventListener valueEventListener;

    FirebaseAuth firebaseAuth;


    boolean fromHome =false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_details);

        fromHome =getIntent().getBooleanExtra("fromHome",false);
        parent_view = findViewById(android.R.id.content);
        pfbd = new ParseFirebaseData(this);
        set = new SettingsAPI(this);
        firebaseAuth= FirebaseAuth.getInstance();
        // animation transition
        ViewCompat.setTransitionName(parent_view, KEY_FRIEND);

        // initialize conversation data
        Intent intent = getIntent();
        friend = (Friend) intent.getExtras().getSerializable(KEY_FRIEND);
       // Toast.makeText(this, ""+friend.getId(), Toast.LENGTH_SHORT).show();
        initToolbar();

        iniComponen();//set.readSetting(Constants.PREF_MY_ID)
        chatNode_1 = firebaseAuth.getUid()+ "-" + friend.getId();
        chatNode_2 = friend.getId() + "-" +  firebaseAuth.getUid();

        valueEventListener=new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d(Constants.LOG_TAG,"Data changed from activity");
              //  Toast.makeText(ChatDetailsActivity.this, "Refreshing Chat!", Toast.LENGTH_SHORT).show();
                if (dataSnapshot.hasChild(chatNode_1)) {
                    chatNode = chatNode_1;
                } else if (dataSnapshot.hasChild(chatNode_2)) {
                    chatNode = chatNode_2;
                } else {
                    chatNode = chatNode_1;
                }
                items.clear();
                items.addAll(pfbd.getMessagesForSingleUser(dataSnapshot.child(chatNode)));

                //Here we are traversing all the messages and mark all received messages read

                for (DataSnapshot data : dataSnapshot.child(chatNode).getChildren()) {
                    if (data.child(NODE_RECEIVER_ID).getValue().toString().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                        data.child(NODE_IS_READ).getRef().runTransaction(new Transaction.Handler() {
                            @NonNull
                            @Override
                            public Transaction.Result doTransaction(@NonNull MutableData mutableData) {
                                mutableData.setValue(true);
                                return Transaction.success(mutableData);
                            }

                            @Override
                            public void onComplete(@Nullable DatabaseError databaseError, boolean b, @Nullable DataSnapshot dataSnapshot) {

                            }
                        });
                    }
                }

                // TODO: 12/09/18 Change it to recyclerview
                mAdapter = new ChatDetailsListAdapter(ChatDetailsActivity.this, items);
                listview.setAdapter(mAdapter);
                listview.requestFocus();
                registerForContextMenu(listview);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        ref = FirebaseDatabase.getInstance().getReference(Constants.MESSAGE_CHILD);
        ref.addValueEventListener(valueEventListener);


    }

    public void initToolbar() {
        actionBar = getSupportActionBar();
        actionBar.setTitle(friend.getName());
    }

    public void iniComponen() {
        listview = (ListView) findViewById(R.id.listview);
        btn_send = (Button) findViewById(R.id.btn_send);
        et_content = (EditText) findViewById(R.id.text_content);
        btn_send.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                ChatMessage message=new ChatMessage();
                message.setText(et_content.getText().toString());
                message.setTimestamp(String.valueOf(System.currentTimeMillis()));
                message.setFriendId(friend.getId());
                message.setFriendName(friend.getName());
                message.setFriendPhoto(friend.getPhoto());
                message.setSenderId(set.readSetting(Constants.PREF_MY_ID));
                message.setSenderName(set.readSetting(Constants.PREF_MY_NAME));
                message.setSenderPhoto(set.readSetting(Constants.PREF_MY_DP));
                message.setRead(false);

                if (chatNode!=null)
                ref.child(chatNode).push().setValue(message);
                et_content.setText("");
                hideKeyboard();
            }
        });
        et_content.addTextChangedListener(contentWatcher);
        if (et_content.length() == 0) {
            btn_send.setVisibility(View.GONE);
        }
        hideKeyboard();
    }


    private void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private TextWatcher contentWatcher = new TextWatcher() {
        @Override
        public void afterTextChanged(Editable etd) {
            if (etd.toString().trim().length() == 0) {
                btn_send.setVisibility(View.GONE);
            } else {
                btn_send.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
        }

        @Override
        public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
        }
    };


    @Override
    protected void onDestroy() {
        ref.removeEventListener(valueEventListener);
        super.onDestroy();
    }
}
