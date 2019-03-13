package ch.uzh.ifi.seal.soprafs19.service;

import ch.uzh.ifi.seal.soprafs19.constant.UserStatus;
import ch.uzh.ifi.seal.soprafs19.entity.User;
import ch.uzh.ifi.seal.soprafs19.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import ch.uzh.ifi.seal.soprafs19.exceptions.ExceptionLogin;

import java.util.UUID;

@Service
@Transactional
public class UserService {

    private final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;


    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Iterable<User> getUsers() {
        return this.userRepository.findAll();
    }

    public User getUser(Long id) {
        return this.userRepository.findById(id).get();
    }

    public User createUser(User newUser) {
        newUser.setToken(UUID.randomUUID().toString());
        newUser.setStatus(UserStatus.OFFLINE);
        newUser.setCreationDate();
        userRepository.save(newUser);
        log.debug("Created Information for User: {}", newUser);
        return newUser;
    }

    public User login(User user) {
        User dbUser = userRepository.findByUsername(user.getUsername());
        if (dbUser.getPassword().equals(user.getPassword())) {
            dbUser.setStatus(UserStatus.ONLINE);
            System.out.println("logged in");
            dbUser = userRepository.save(dbUser);
            return dbUser;
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,"Password does not match user password");
        }
    }


    public User logoutUser(long id) {
        User dbUser = userRepository.findById(id);
        dbUser.setStatus(UserStatus.OFFLINE);
        System.out.println("logged out");
        userRepository.save(dbUser);
        return dbUser;
    }

    public User replaceUser(User newUser, long userId){
        User checkUser = this.userRepository.findByUsername(newUser.getUsername());
        User anUser = getUser(userId);
        if(checkUser!= null){
            throw new ExceptionLogin();
        }
        if(anUser.getUsername() != newUser.getUsername() && newUser.getUsername() != null){
            anUser.setUsername(newUser.getUsername());
        }
        if(anUser.getBirthday() != newUser.getBirthday() && newUser.getBirthday() != null){
            anUser.setBirthday(newUser.getBirthday());
        }
        userRepository.save(anUser); //what if not?
        return anUser;

    }

    public User getUserByToken(String token) {
        User dbUser = userRepository.findByToken(token);
        if (dbUser == null) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,"You are not logged in");
        }
        return dbUser;
    }
}
