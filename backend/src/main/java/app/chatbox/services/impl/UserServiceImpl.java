package app.chatbox.services.impl;

import app.chatbox.dto.UserDTO;
import app.chatbox.mapper.UserMapper;
import app.chatbox.model.AppUser;
import app.chatbox.repository.UserRepository;
import app.chatbox.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepo;
    private final UserMapper mapper;

    @Override
    public UserDTO findById(Long id) {
        AppUser user = userRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("AppUser not found"));

        return mapper.toDTO(user);
    }
}