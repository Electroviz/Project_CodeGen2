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

    //melle
    public boolean TestLoginAttempt(String username, String password) {
        User usernameUser = userRepository.findByUsername(username);

        if(username != null) return password.equals(usernameUser.getPassword());
        else return false;
    }

    //melle
    public User getUserById(Long id) {
        Optional<User> optionalUser = userRepository.findById(id);
        if(optionalUser.isPresent()) return optionalUser.get();
        else return null;
    }

    public List<User> getAll(){
        return userRepository.findAll();
    }

    public User findById(Long id){

        Optional<User> test = userRepository.findById(id);
        User test2 = test.get();

        System.out.println(test2);

        return test2;
    }
}
