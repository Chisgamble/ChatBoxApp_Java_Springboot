package com.example.services.admin;

import com.example.api.admin.UserListApi;
import com.example.dto.UserDTO;

import java.util.List;


public class UserListService {
    private final UserListApi api = new UserListApi();

    public List<UserDTO> getAll (){
        return api.getAllUsers().users();
    }
}
