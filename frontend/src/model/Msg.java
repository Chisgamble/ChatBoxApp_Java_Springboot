package model;

public class Msg {
    private String content;
    private String senderInitials;
    private String senderName;

    public Msg(){
        senderName = "Alice";
        content = "Hello";
        senderInitials = "A";
    }

    public Msg(String name){
        senderName = name;
        content = "Hello";
        senderInitials = name.substring(0,1).toUpperCase();
    }

    public String getSenderName(){
        return this.senderName;
    }

    public String getSenderInitials(){
        return this.senderInitials;
    }

    public String getContent(){
        return this.content;
    }
}
