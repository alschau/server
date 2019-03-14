package ch.uzh.ifi.seal.soprafs19.controller;


import ch.uzh.ifi.seal.soprafs19.Application;
import ch.uzh.ifi.seal.soprafs19.entity.User;
import ch.uzh.ifi.seal.soprafs19.repository.UserRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;


/**
 * Test class for the UserResource REST resource.
 *
 * @see UserController
 */
@WebAppConfiguration
@RunWith(SpringRunner.class)
@SpringBootTest(classes= Application.class)
public class UserControllerTest {


    @Qualifier("userRepository")
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserController userController;

    @Test
    public void testCreateUser() {
        Assert.assertNull(userRepository.findByUsername("testUsername"));

        User testUser1 = new User();
        testUser1.setName("testName1");
        testUser1.setUsername("testUsername1");
        testUser1.setPassword("testPassword1");

        String returnString1 = userController.createUser(testUser1).toString();

        Assert.assertEquals("/users/" + userRepository.findByUsername("testUsername1").getId().toString(), returnString1);
    }


}
