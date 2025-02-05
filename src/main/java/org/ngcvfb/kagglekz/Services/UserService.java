package org.ngcvfb.kagglekz.Services;


import org.ngcvfb.kagglekz.DTO.UserDTO;
import org.ngcvfb.kagglekz.Models.UserModel;
import org.ngcvfb.kagglekz.Repository.UserRepository;
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
        if (userModel.isPresent()){
            return userModel.get();
        }
        else {
            return null;
        }

    }
    public UserModel createUser(UserModel user) {
        return userRepository.save(user);
    }
    public UserModel updateUser(UserModel user) {
        return userRepository.save(user);
    }
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(username));
    }
}
