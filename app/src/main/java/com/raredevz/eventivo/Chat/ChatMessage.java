package com.raredevz.eventivo.Chat;



public class ChatMessage {
    public String text,timestamp,friendId,friendName,friendPhoto,
    senderId,senderName,senderPhoto;
    public Boolean read;


    public ChatMessage() {
    }

    public ChatMessage(String text, String timestamp, String friendId, String friendName, String friendPhoto, String senderId, String senderName, String senderPhoto, Boolean isRead) {
        this.text = text;
        this.timestamp = timestamp;
        this.friendId = friendId;
        this.friendName = friendName;
        this.friendPhoto = friendPhoto;
        this.senderId = senderId;
        this.senderName = senderName;
        this.senderPhoto = senderPhoto;
        this.read = isRead;

    }

    public Boolean getRead() {
        return read;
    }

    public void setRead(Boolean read) {
        this.read = read;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getFriendId() {
        return friendId;
    }

    public void setFriendId(String friendId) {
        this.friendId = friendId;
    }

    public String getFriendName() {
        return friendName;
    }

    public void setFriendName(String friendName) {
        this.friendName = friendName;
    }

    public String getFriendPhoto() {
        return friendPhoto;
    }

    public void setFriendPhoto(String friendPhoto) {
        this.friendPhoto = friendPhoto;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getSenderPhoto() {
        return senderPhoto;
    }

    public void setSenderPhoto(String senderPhoto) {
        this.senderPhoto = senderPhoto;
    }

}
