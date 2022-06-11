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
    public User getUserById(Long id) {
        Optional<User> optionalUser = userRepository.findById(id);
        if(optionalUser.isPresent()) return optionalUser.get();
        else return null;
    }

    public List<User> getAll(){
        return userRepository.findAll();
    }

    //Murat
    public Optional<User> findById(Long userId) {
        Optional<User> entity = userRepository.findById(userId);
        return entity.map(this::toUserModel);
    }
    //Murat
    private User toUserModel(User userEntity) {
        User user = new User();
        user.setId(userEntity.getId());
        user.setUsername(userEntity.getUsername());
        user.setFullname(userEntity.getFullname());
        user.setEmail(userEntity.getEmail());
        user.setPassword(userEntity.getPassword());
        user.setPhone(userEntity.getPhone());
        user.setDateOfBirth(userEntity.getDateOfBirth());
        user.setUserRole(User.UserRoleEnum.fromValue(String.valueOf(userEntity.getUserRole())));
        user.setTransactionLimit(userEntity.getTransactionLimit());
        user.setDayLimit(userEntity.getDayLimit());
        return user;
    }
}
