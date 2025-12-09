package services.admin;

import api.admin.UserListApi;
import dto.UserDTO;

import java.util.List;


public class UserListService {
    private final UserListApi api = new UserListApi();

    public List<UserDTO> getAll (){
        return api.getAllUsers().users();
    }
}
