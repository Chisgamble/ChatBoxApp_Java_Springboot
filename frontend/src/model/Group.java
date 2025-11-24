package model;

import java.util.List;

public class Group {
    String name;
    List<User> members;

    public Group(String name, List<User> members) {
        this.name = name;
        this.members = members;
    }

    public String getName() { return name; }
    public List<User> getMembers() { return members; }
}
