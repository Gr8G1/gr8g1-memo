package Gr8G1.prac.jpa.test;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
public class UserRepositoryTest {
    @Autowired
    UserRepository userRepository;

    @Autowired
    TeamRepository teamRepository;

    @Test
    @Transactional
    @Rollback(value = false)
    public void userTest() {
        Team newTeam = Team.builder()
                           .teamName("new Team")
                           .build();

        // Team savedTeam = teamRepository.save(newTeam);

        User newUser = User.builder()
                        .userName("new User")
                        .team(newTeam)
                        .build();

        User savedUser = userRepository.save(newUser);
    }

    @Test
    @Transactional
    @Rollback(value = false)
    public void deleteUser() {

        Optional<User> byId = userRepository.findById(1L);
        User user = byId.get();

        userRepository.delete(user);

    }
}
