package ch.uzh.ifi.seal.soprafs19.controller;

import ch.uzh.ifi.seal.soprafs19.entity.User;
import ch.uzh.ifi.seal.soprafs19.service.UserService;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    private final UserService service;

    UserController(UserService service) {
        this.service = service;
    }

    @GetMapping("/users")
    Iterable<User> all() {
        System.out.println("User werden gesucht!");
        return service.getUsers();
    }

    @GetMapping("/users/me")
    User me(@RequestHeader("Access-Token") String token) {
        return service.getUserByToken(token);
    }

    @GetMapping("/users/{userId}")
    User one(@PathVariable("userId") Long id) {
            return service.getUser(id);
    }

    @PostMapping("/login")
    User login(@RequestBody User user) {
        System.out.println("Logging in!");
        return this.service.login(user);
    }


    @PostMapping("/logout/{userId}")
    User logout(@PathVariable("userId") long id) {
        System.out.println("Logging out!");
        return this.service.logoutUser(id);
    }


    @PostMapping("/users")
    User createUser(@RequestBody User newUser) {
            return this.service.createUser(newUser);
    }

    @CrossOrigin
    @PutMapping("/users/{userId}")
    User replaceUser(@RequestBody User newUser, @PathVariable long userId) {
        User anUser = this.service.getUser(userId);
        if (anUser != null){
            return this.service.replaceUser(newUser, userId);
        } else {
            System.out.println("bitch lasagna");
        }
        if (service.getUser(userId).getUsername() == newUser.getUsername()) { }
        else {
            service.getUser(userId).setUsername(newUser.getUsername());
        }
        service.getUser(userId).setBirthday(newUser.getBirthday());
        return service.getUser(userId);
    }
}

