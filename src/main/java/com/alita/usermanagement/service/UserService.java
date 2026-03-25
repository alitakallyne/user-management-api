package com.alita.usermanagement.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.alita.usermanagement.infrastructure.entity.User;
import com.alita.usermanagement.infrastructure.repository.UserRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;
   
    public List<User> getUserInfo() {
        return userRepository.findAll();
    }

    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado com id: " + id));
    }

    public User createUser( User user) {

        return userRepository.save(user);
      
    }

    public User updateUser(Long id, User user) {
        User existingUser = getUserById(id);

        existingUser.setName(user.getName());
        existingUser.setEmail(user.getEmail());
        existingUser.setPassword(user.getPassword());

        return userRepository.save(existingUser);
    }

   public void deleteUser(Long id) {
        User user = getUserById(id);
        userRepository.delete(user);
    }


}
