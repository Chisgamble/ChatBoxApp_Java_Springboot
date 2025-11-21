package model;

import java.util.Random;

public class User {
    String initials;
    String name;
    String gender;
    String role;
    String address;
    String birthday;
    String email;
    boolean is_active;
    String last_msg;
    Random rand = new Random();

    public User(){
        this.initials = "A";
        this.name = "Alice";
        this.gender = "female";
        this.role = "user";
        this.birthday = "1/1/2000";
        this.address = "Here";
        this.email = "alice@gmail.com";
        this.is_active = rand.nextInt(2) % 2 == 0;
        this.last_msg = "Hello";
    }

    public User(String name){
        this.initials = name.substring(0,1).toUpperCase();
        this.name = name;
        this.role = "user";
        this.gender = rand.nextInt(2) % 2 == 0 ? "male" : "female";
        this.address = "There";
        this.birthday = rand.nextInt(30) + "//" + rand.nextInt(12) + "//" + rand.nextInt(2025);
        this.email = name + "@gmail.com";
        this.is_active = rand.nextInt(2) % 2 == 0;
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

    public boolean isActive() {
        return is_active;
    }

    public String getBirthDay() {
        return this.birthday;
    }

    public String getAddress() {
        return address;
    }

    public String getEmail() {
        return email;
    }

    public String getGender() {
        return gender;
    }
}
