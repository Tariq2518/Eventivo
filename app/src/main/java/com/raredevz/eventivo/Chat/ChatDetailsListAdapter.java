package com.raredevz.eventivo.Chat;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import com.raredevz.eventivo.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;


public class ChatDetailsListAdapter extends BaseAdapter {
    Context mContext;
    ArrayList<ChatMessage> mMessages;
    SettingsAPI set;
    String usrId;

    public ChatDetailsListAdapter(Context mContext, ArrayList<ChatMessage> mMessages) {
        this.mContext = mContext;
        this.mMessages = mMessages;
        set=new SettingsAPI(mContext);
        SharedPreferences preferences=mContext.getSharedPreferences(Constants.UserPref.Pref_Name,MODE_PRIVATE);

        usrId = FirebaseAuth.getInstance().getUid();

    }

    @Override
    public Object getItem(int i) {
        return mMessages.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public int getCount() {
        return mMessages.size();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ChatMessage msg=(ChatMessage) getItem(i);
        ViewHolder viewHolder;
        if (view==null){
            viewHolder=new ViewHolder();
            view= LayoutInflater.from(mContext).inflate(R.layout.row_chat_details,viewGroup,false);
            viewHolder.time=(TextView) view.findViewById(R.id.text_time);
            viewHolder.message=(TextView) view.findViewById(R.id.text_content);
            viewHolder.lyt_thread=(CardView) view.findViewById(R.id.lyt_thread);
            viewHolder.lyt_parent=(LinearLayout)view.findViewById(R.id.lyt_parent);
            viewHolder.image_status=(ImageView)view.findViewById(R.id.image_status);
            view.setTag(viewHolder);
        }else {
            viewHolder=(ViewHolder) view.getTag();
        }

        viewHolder.message.setText(msg.getText());
        viewHolder.time.setText(Tools.formatTime(Long.valueOf(msg.getTimestamp())));

        if (msg.getFriendId().equals(usrId)){
            viewHolder.lyt_parent.setPadding(15,10,100,10);
            viewHolder.lyt_parent.setGravity(Gravity.LEFT);
            viewHolder.lyt_thread.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
            viewHolder.image_status.setImageResource(android.R.color.transparent);
        }else{
            viewHolder.lyt_parent.setPadding(100,10,15,10);
            viewHolder.lyt_parent.setGravity(Gravity.RIGHT);
            viewHolder.lyt_thread.setCardBackgroundColor(mContext.getResources().getColor(R.color.me_chat_bg));
            viewHolder.image_status.setImageResource(R.drawable.baseline_done_24);
            viewHolder.image_status.setColorFilter(ContextCompat.getColor(mContext,android.R.color.darker_gray), PorterDuff.Mode.MULTIPLY);

            if (msg.getRead()){
                viewHolder.image_status.setImageResource(R.drawable.baseline_done_all_24);
                viewHolder.image_status.setColorFilter(ContextCompat.getColor(mContext,android.R.color.holo_blue_dark), PorterDuff.Mode.MULTIPLY);

            }

        }
        return view;
    }

    void add(ChatMessage msg){
        mMessages.add(msg);
    }

    private class ViewHolder{
        TextView time,message;
        LinearLayout lyt_parent;
        CardView lyt_thread;
        ImageView image_status;
    }
}
