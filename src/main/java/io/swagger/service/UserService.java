package io.swagger.service;

import io.swagger.model.entity.User;
import io.swagger.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    private List<User> userList = new ArrayList<>();

    public User addUser(User user){
        user = userRepository.save(user);
        return user;
    }

    public List<User> getAll(){
        return userList;
    }
}
