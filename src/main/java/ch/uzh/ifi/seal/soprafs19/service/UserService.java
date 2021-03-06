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
        User dbUser = userRepository.findByUsername(newUser.getUsername());
        System.out.println(dbUser);
        if (dbUser != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Username taken!");
        } else {
            newUser.setToken(UUID.randomUUID().toString());
            newUser.setStatus(UserStatus.OFFLINE);
            newUser.setCreationDate();
            userRepository.save(newUser);
            log.debug("Created Information for User: {}", newUser);
            return newUser;
        }
    }

    public User login(User user) {
        User dbUser = userRepository.findByUsername(user.getUsername());
        if (dbUser == null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,"Username doesn't exist!");
        }
        else if (dbUser.getPassword().equals(user.getPassword())) {
            dbUser.setStatus(UserStatus.ONLINE);
            System.out.println("logged in");
            dbUser = userRepository.save(dbUser);
            return dbUser;
        } else {
            throw new ResponseStatusException(HttpStatus.CONFLICT,"Password does not match user password");
        }
    }


    public User logoutUser(long id) {
        User dbUser = userRepository.findById(id);
        dbUser.setStatus(UserStatus.OFFLINE);
        System.out.println("logged out");
        userRepository.save(dbUser);
        return dbUser;
    }


    public User replaceUser (long userId, User user){
        User isUser = this.userRepository.findByUsername(user.getUsername());
        if (isUser != null){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "User already exists");
        } else {
            User dbUser = getUser(userId);
            if(dbUser.getUsername() != user.getUsername() && user.getUsername()!=null){
                dbUser.setUsername(user.getUsername());
            }
            if (dbUser.getBirthday() != user.getBirthday() && user.getBirthday() != null){
                dbUser.setBirthday(user.getBirthday());
            }
            userRepository.save(dbUser);
            return dbUser;
        }

    }

    public User getUserByToken(String token) {
        User dbUser = userRepository.findByToken(token);
        if (dbUser == null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,"You are not logged in");
        }
        return dbUser;
    }
}
