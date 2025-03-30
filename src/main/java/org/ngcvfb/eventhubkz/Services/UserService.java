package org.ngcvfb.eventhubkz.Services;


import jakarta.transaction.Transactional;
import org.hibernate.Hibernate;
import org.ngcvfb.eventhubkz.DTO.UserDTO;
import org.ngcvfb.eventhubkz.Models.Role;
import org.ngcvfb.eventhubkz.Models.UserModel;
import org.ngcvfb.eventhubkz.Repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<UserModel> getAllUsers() {
        return userRepository.findAll();
    }

    public UserModel getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public UserModel getUserByEmail(String email) {
        Optional<UserModel> userModel = userRepository.findByEmail(email);
        return userModel.orElse(null);

    }
    public UserModel createUser(UserModel user) {
        return userRepository.save(user);
    }
    public UserModel updateUser(Long id, UserDTO userDTO) {
        UserModel existingUser = getUserById(id);
        if (existingUser == null) {
            return null;
        }

        if (userDTO.getUsername() != null) {
            existingUser.setUsername(userDTO.getUsername());
        }
        if (userDTO.getEmail() != null) {
            existingUser.setEmail(userDTO.getEmail());
        }
        if (userDTO.getPassword() != null) {
            existingUser.setPassword(userDTO.getPassword());
        }
        if (userDTO.getRole() != null) {
            if (userDTO.getRole().equals("ADMIN")) {
                existingUser.setRole(Role.ADMIN);
            } else if (userDTO.getRole().equals("USER")) {
                existingUser.setRole(Role.USER);
            }
        }
        if (userDTO.getContacts() != null) {
            existingUser.setContacts(userDTO.getContacts());
        }
        if (userDTO.getDescription() != null) {
            existingUser.setDescription(userDTO.getDescription());
        }

        return userRepository.save(existingUser);
    }
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserModel user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(username));
        Hibernate.initialize(user.getLikes());
        return user;
    }
}
