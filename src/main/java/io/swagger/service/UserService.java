package io.swagger.service;

import io.swagger.jwt.JwtTokenProvider;
import io.swagger.model.entity.User;
import io.swagger.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BankAccountService bankAccountService;

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    PasswordEncoder passwordEncoder;

    private List<User> userList = new ArrayList<>();

    //Nick
    public User addUser(User user){

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        user = userRepository.save(user);
        return user;
    }

    //melle
    public Long getUserIdByUsername(String username) {
        User u = userRepository.findByusername(username);

        if(u == null) return Long.valueOf(-1);
        else return u.getId();
    }

    //melle
    public List<User> getUsersWithoutBankAccount() {
        List<User> allUsers = userRepository.findAll();
        List<User> usersWithoutBankAccount = new ArrayList<>();
        for(User u : allUsers)
            if(!bankAccountService.UserAlreadyHasBankAccounts(u.getId())) usersWithoutBankAccount.add(u);

        return usersWithoutBankAccount;
    }

    //melle
    public User TestLoginAttempt(String username, String password) {
        User usernameUser = userRepository.findByusername(username);

        if(username != null)
            if(password.equals(usernameUser.getPassword())) return usernameUser;
            else return null;
        else return null;
    }

    //melle
    public User getUserById(Long id) {
        Optional<User> optionalUser = userRepository.findById(id);
        if(optionalUser.isPresent()) return optionalUser.get();
        else return null;
    }

    //Nick
    public List<User> getAll(){
        return userRepository.findAll();
    }

    //Nick
    public User findById(Long id){

        Optional<User> test = userRepository.findById(id);
        User test2 = test.get();

        System.out.println(test2);

        return test2;
    }

    //Nick
    public String login(String username, String password){

        String token = "";

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
            User user = userRepository.findByusername(username);
            token = jwtTokenProvider.createToken(username, user.getRoles());

        } catch(AuthenticationException ex){
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Invalid Username/Password");
        }

        return token;
    }
}
