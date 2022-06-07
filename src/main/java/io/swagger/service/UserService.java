package io.swagger.service;

import io.swagger.entity.UserEntity;
import io.swagger.model.User;
import io.swagger.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    
    public Optional<User> findById(Long userId) {
        Optional<User> entity = userRepository.findById(userId);
//        return entity.map(this::toUserModel);
        return entity;
    }

    public void SaveUser(User u) {
        this.userRepository.save(u);
    }

    private User toUserModel(UserEntity userEntity) {
//        User user = new User();
//        user.setId(userEntity.getId());
//        user.setUsername(userEntity.getUsername());
//        user.setFullname(userEntity.getFullname());
//        user.setEmail(userEntity.getEmail());
//        user.setPassword(userEntity.getPassword());
//        user.setPhone(userEntity.getPhone());
//        user.setDateOfBirth(userEntity.getDateOfBirth());
//        user.setUserRole(User.UserRoleEnum.fromValue(userEntity.getUserRole()));
//        user.setTransactionLimit(userEntity.getTransactionLimit());
//        user.setDayLimit(userEntity.getDayLimit());
//        return user;
        return null;
    }

}