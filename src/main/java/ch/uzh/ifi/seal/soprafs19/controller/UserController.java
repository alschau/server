package ch.uzh.ifi.seal.soprafs19.controller;

import ch.uzh.ifi.seal.soprafs19.entity.User;
import ch.uzh.ifi.seal.soprafs19.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;


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
    ResponseEntity<User> me(@RequestHeader("Access-Token") String token) {
        try{
            return ResponseEntity.status(HttpStatus.OK).body(service.getUserByToken(token));
        } catch (Exception ex){
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "user not found", ex
            );
        }
    }

    @GetMapping("/users/{userId}")
    ResponseEntity<User> one(@PathVariable("userId") long id) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(service.getUser(id));
        } catch (Exception ex){
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "user not found", ex);
        }
    }

    @PostMapping("/login")
    ResponseEntity <User> login(@RequestBody User user) {
        System.out.println("Logging in!");
            return ResponseEntity.status(HttpStatus.OK).body(service.login(user));
    }


    @PostMapping("/logout/{userId}")
    User logout(@PathVariable("userId") long id) {
        System.out.println("Logging out!");
        return this.service.logoutUser(id);
    }


    @PostMapping("/users")
    ResponseEntity <User> createUser(@RequestBody User newUser) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(service.createUser(newUser));
        } catch (Exception ex){
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, "Username already exists", ex);
        }
    }

    @CrossOrigin
    @PutMapping("/users/{userId}")
    ResponseEntity<User> replaceUser(@RequestBody User newUser, @PathVariable("userId") Long userId) {

        User dbUser = this.service.getUser(userId);
        if (dbUser != null) {
            return ResponseEntity.status(HttpStatus.OK).body(service.replaceUser(userId, newUser));
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");

        }

    }
}

