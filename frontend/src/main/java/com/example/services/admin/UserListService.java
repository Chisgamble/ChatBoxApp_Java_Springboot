package com.example.services.admin;

import com.example.api.admin.UserListApi;
import com.example.dto.NewUserDTO;
import com.example.dto.NewUserListDTO;
import com.example.dto.UserDTO;
import com.example.dto.YearlyGraphDTO;
import com.example.dto.request.AdminCreateOrUpdateUserReqDTO;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class UserListService {
    private final UserListApi api = new UserListApi();

    public List<UserDTO> getAll (List<String> username, List<String> name, String status, String role, String sort, String order){
        return api.getAllUsers(username, name, status, role, sort, order).users();
    }

    public List<UserDTO> getAll (){
        List<String> a = new ArrayList<>();
        List<String> b = new ArrayList<>();
        String c = "";
        String d = "";
        String e = "";
        String f = "";
        return api.getAllUsers(a,b,c,d,e,f).users();
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

    public List<NewUserDTO> getNewUsers(List<String> username, List<String> email, LocalDate start, LocalDate end, String order) {
        // 1. Call API
        NewUserListDTO response = api.getNewUserList(username, email, start, end, order);

        // 2. Unwrap and Safely Return
        if (response != null && response.users() != null) {
            return response.users();
        }

        return Collections.emptyList();
    }

    public YearlyGraphDTO getNewUserGraph(Integer year) {
        return api.getNewUserGraph(year);
    }
}
