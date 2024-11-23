package com.pwr.gastromate.service.mapper;

import com.pwr.gastromate.data.User;
import com.pwr.gastromate.dto.UserDTO;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserDTO toDTO(User user) {
        return new UserDTO(
                user.getEmail(),
                user.getUsername(),
                user.getPassword(),
                user.getAddress()
        );
    }

    public User toEntity(UserDTO userDTO) {
        User user = new User();
        user.setEmail(userDTO.getEmail());
        user.setUsername(userDTO.getUsername());
        user.setPassword(userDTO.getPassword());
        user.setAddress(userDTO.getAddress());
        return user;
    }
}
