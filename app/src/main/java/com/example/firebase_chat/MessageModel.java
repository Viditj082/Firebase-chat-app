package com.example.firebase_chat;

public class MessageModel {

    private String message;
    private boolean Issentbyme;

    public  MessageModel()
    {

    }
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isIssentbyme() {
        return Issentbyme;
    }

    public void setIssentbyme(boolean issentbyme) {
        Issentbyme = issentbyme;
    }

    public MessageModel(String message, boolean issentbyme) {
        this.message = message;
        Issentbyme = issentbyme;
    }
}
