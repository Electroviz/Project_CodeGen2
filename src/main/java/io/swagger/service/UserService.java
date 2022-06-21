package io.swagger.service;

import io.swagger.enums.UserRoleEnum;
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
import org.threeten.bp.OffsetDateTime;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    public ResponseEntity addUser(User user){

//        if (userRepository.findByusername(user.getUsername()).getRole().equals(UserRoleEnum.ROLE_CUSTOMER)){
//            return ResponseEntity.status(403).body("Unauthorized");
//        }
        String test = "";
        if (test == ":"){
            return ResponseEntity.status(400).body("Bad request1");
        }
        else{
            if(checkUserInputAddUser(user)){

                user.setPassword(passwordEncoder.encode(user.getPassword()));

                user = userRepository.save(user);

                return ResponseEntity.status(201).body(user);
            }
            else{
                return ResponseEntity.status(400).body("User Input incorrect");
            }
        }
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
    public boolean checkUserInputAddUser(User user){
        if(user.getFullname().length() < 50 && user.getFullname().length() > 1 && checkIfUserInputIsWord(user.getFullname())){
            if(user.getUsername().length() < 20 && user.getUsername().length() > 3){
                if(user.getEmail().length() < 50 && user.getEmail().length() > 5 && checkIfStringIsEmail(user.getEmail())){
                    if(user.getDayLimit().doubleValue() >= 0 && user.getDayLimit().doubleValue() <= 10000){
                        if(user.getTransactionLimit().doubleValue() >= 0 && user.getTransactionLimit().doubleValue() <= 10000){
                            if(user.getRole().equals(UserRoleEnum.ROLE_BANK) || user.getRole().equals(UserRoleEnum.ROLE_CUSTOMER) || user.getRole().equals(UserRoleEnum.ROLE_EMPLOYEE)){
                                if(user.getPassword().length() >= 6 && user.getPassword().length() <= 32){
                                    return true;
                                }
                            }
                        }
                    }
                }
            }
        }
        return false;
    }
    public boolean checkIfUserInputIsWord(String userInput){
        String noSpaceStr = userInput.replaceAll("\\s", "");
        char[] chars = noSpaceStr.toCharArray();

        for (char c : chars) {
            if(!Character.isLetter(c)) {
                return false;
            }
        }

        return true;
    }

    public boolean checkIfStringIsEmail(String userInput){
        Pattern emailPattern = Pattern.compile("[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}");
        Matcher mat = emailPattern.matcher(userInput);
        return mat.matches();
    }
}
