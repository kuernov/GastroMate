package com.pwr.gastromate.service;

import com.pwr.gastromate.config.JwtService;
import com.pwr.gastromate.data.Address;
import com.pwr.gastromate.data.Role;
import com.pwr.gastromate.data.User;
import com.pwr.gastromate.dto.UserDTO;
import com.pwr.gastromate.controller.UserController;
import com.pwr.gastromate.exception.ResourceNotFoundException;
import com.pwr.gastromate.repository.AddressRepository;
import com.pwr.gastromate.repository.UserRepository;
import com.pwr.gastromate.service.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import static java.util.Collections.emptyList;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final AddressRepository addressRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public UserDetails authenticate(String email, String password) {
        // Autentykacja użytkownika
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));

        // Pobranie szczegółów użytkownika po weryfikacji
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Invalid email or password"));
    }

    public UserDetails loadUserByUsername(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
    }


    public UserDTO getUserById(Integer id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found for this id: " + id));
        return userMapper.toDTO(user);
    }
    public List<UserDTO> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(userMapper::toDTO)
                .collect(Collectors.toList());
    }
    public UserDTO save(UserDTO userDTO) {
        User user = userMapper.toEntity(userDTO);
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        User savedUser = userRepository.save(user);
        return userMapper.toDTO(savedUser);
    }


    public UserDTO updateUser(Integer id, UserDTO userDTO) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found for this id: " + id));

        user.setEmail(userDTO.getEmail());
        user.setUsername(userDTO.getUsername());
        user.setAddress(userDTO.getAddress());


        User updatedUser = userRepository.save(user);
        return userMapper.toDTO(updatedUser);
    }


    public void register(UserController.RegistrationRequest request){
        var user = User.builder()
                .email(request.email())
                .username(request.username())
                .password(passwordEncoder.encode(request.password()))
                .roles(List.of(Role.USER.name()))
                .orders(emptyList())
                .categories(emptyList())
                .supplyOrders(emptyList())
                .build();
        Address address = new Address();
        address.setCity(request.city());
        address.setStreet(request.street());
        address.setPostalCode(request.postalCode());

        user.setAddress(address);
        userRepository.save(user);
        address.setUser(user);
        addressRepository.save(address);
    }

    public void deleteUser(Integer id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found for this id: " + id));
        userRepository.delete(user);
    }
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public Optional<User> findByEmailAndPassword(String email, String password) {
        return userRepository.findByEmailAndPassword(email, password);
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}
