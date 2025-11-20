package model;

public class Msg {
    private String content;
    private String senderID;

    public Msg(){
        content = "Hello";
        senderID = "1";
    }

    public String getSenderID(){
        return this.senderID;
    }

    public String getContent(){
        return this.content;
    }
}
