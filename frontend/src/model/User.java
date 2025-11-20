package model;

public class User {
    String initials;
    String name;
    String role;
    boolean is_active;
    String last_msg;

    public User(){
        this.initials = "A";
        this.name = "Alice";
        this.role = "user";
        this.is_active = false;
        this.last_msg = "Hello";
    }

    public User(String name){
        this.initials = name.substring(0,1).toUpperCase();
        this.name = name;
        this.role = "user";
        this.is_active = false;
        this.last_msg = "Hello, my name is" + name;
    }

    public String getInitials(){
        return this.initials;
    }

    public String getName(){
        return this.name;
    }

    public String getLastMsg(){
        return this.last_msg;
    }
    // getters + setters
    // Optional: override toString for debugging
}
