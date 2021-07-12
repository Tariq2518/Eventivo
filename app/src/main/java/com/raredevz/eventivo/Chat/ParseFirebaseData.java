package com.raredevz.eventivo.Chat;

import android.content.Context;

import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;
import java.util.List;

import static com.raredevz.eventivo.Chat.Constants.NODE_TIMESTAMP;


public class ParseFirebaseData {

    private SettingsAPI set;

    public ParseFirebaseData(Context context) {
        set = new SettingsAPI(context);
    }



    public List<ChatMessage> getMessagesForSingleUser(DataSnapshot dataSnapshot) {
        List<ChatMessage> chats = new ArrayList<>();
        for (DataSnapshot data : dataSnapshot.getChildren()) {

            chats.add(data.getValue(ChatMessage.class));//new ChatMessage(text, msgTime, receiverId, receiverName, receiverPhoto, senderId, senderName, senderPhoto, read));
        }
        return chats;
    }
    public ArrayList<ChatMessage> getAllLastMessages(DataSnapshot dataSnapshot, Context context) {
        // TODO: 11/09/18 Return only last messages of every conversation current user is involved in
        ArrayList<ChatMessage> lastChats = new ArrayList<>();
        ArrayList<ChatMessage> tempMsgList;
        long lastTimeStamp;
        String text = null, msgTime = null, senderId = null, senderName = null, senderPhoto = null, receiverId = null, receiverName = null, receiverPhoto = null;
        Boolean read = Boolean.TRUE;
        for (DataSnapshot wholeChatData : dataSnapshot.getChildren()) {
            tempMsgList = new ArrayList<>();
            lastTimeStamp = 0;
            for (DataSnapshot data : wholeChatData.getChildren()) {
                msgTime = data.child(NODE_TIMESTAMP).getValue().toString();
                if (Long.parseLong(msgTime) > lastTimeStamp)
                    lastTimeStamp = Long.parseLong(msgTime);
                tempMsgList.add(data.getValue(ChatMessage.class));//new ChatMessage(text, msgTime, receiverId, receiverName, receiverPhoto, senderId, senderName, senderPhoto, read));
            }


            for (ChatMessage oneTemp : tempMsgList) {
                if ((set.readSetting(Constants.PREF_MY_ID).equals(oneTemp.getFriendId())) || (set.readSetting("myid").equals(oneTemp.getSenderId()))) {
                    if (oneTemp.getTimestamp().equals(String.valueOf(lastTimeStamp))) {
                        lastChats.add(oneTemp);
                    }
                }
            }
        }
        return lastChats;
    }
}
