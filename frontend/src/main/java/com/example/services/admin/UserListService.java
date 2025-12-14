package com.example.services.admin;

import com.example.api.admin.UserListApi;
import com.example.dto.UserDTO;
import com.example.dto.request.AdminCreateOrUpdateUserReqDTO;

import java.util.ArrayList;
import java.util.List;


public class UserListService {
    private final UserListApi api = new UserListApi();

    public List<UserDTO> getAll (List<String> username, List<String> name, String status, String sort, String order){
        return api.getAllUsers(username, name, status, sort, order).users();
    }

    public List<UserDTO> getAll (){
        List<String> a = new ArrayList<>();
        List<String> b = new ArrayList<>();
        String c = "";
        String d = "";
        String e = "";
        return api.getAllUsers(a,b,c,d,e).users();
    }

    public void createUser(AdminCreateOrUpdateUserReqDTO req) {
        api.createUser(req);
    }

    public void updateUser(Long id, AdminCreateOrUpdateUserReqDTO req) {
        api.updateUser(id, req);
    }

    public void lockUser(Long id) {
        api.setLockStatus(id, true);
    }

    public void unlockUser(Long id) {
        api.setLockStatus(id, false);
    }

    public void deleteUser(Long id) {
        api.deleteUser(id);
    }
}
