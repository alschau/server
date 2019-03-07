package ch.uzh.ifi.seal.soprafs19.controller;

import ch.uzh.ifi.seal.soprafs19.entity.User;
import ch.uzh.ifi.seal.soprafs19.repository.UserRepository;
import ch.uzh.ifi.seal.soprafs19.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

@RestController
public class UserController {

    private final UserService service;

    UserController(UserService service) {
        this.service = service;
    }

    @GetMapping("/users")
    Iterable<User> all() {
        System.out.println("User werden gesucht!");
        System.out.println(service.getUsers());
        return service.getUsers();
    }

    @GetMapping("/users/{id}")
    //System.out.println("User mit dieser ID wird mit GetMapping gesucht");
    User one(
            @PathVariable("id") Long id) {
        try {
            return service.getUser(id);
        } catch(Exception ex) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "User Not Found", ex);
        }
    }

    @PostMapping("/users")
            //System.out.println("User werden mit PostMapping gesucht");
    User createUser(@RequestBody User newUser) {
        try {
            return this.service.createUser(newUser);
        } catch (Exception ex) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, "Username exists already", ex);
        }
    }

    @PutMapping("/users/{id}")
            //System.out.println("User mit der ID wird geupdated");
    User updateUser(@RequestBody User newUser, @PathVariable Long id) {
        try {
            if (service.getUser(id).getUsername() == newUser.getUsername()) {}
            else {
                service.getUser(id).setUsername(newUser.getUsername());
            }
            service.getUser(id).setStatus(newUser.getStatus());
            service.getUser(id).setBirthday(newUser.getBirthday());
            return service.getUser(id);
        } catch (Exception ex) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "User Not Found", ex);
        }
    }
}

