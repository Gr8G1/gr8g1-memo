package Gr8G1.prac.jpa.test;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

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
    public void createTest() {
        Team newTeam = Team.builder()
                           .teamName("Team" + Math.random() * 10)
                           .users(new ArrayList<>())
                           .build();

        User user1 = User.builder()
                         .userName("User1" + Math.random() * 10)
                         .build();

        User user2 = User.builder()
                        .userName("User2" + Math.random() * 10)
                        .build();

        User user3 = User.builder()
                         .userName("User3" + Math.random() * 10)
                         .build();

        user1.addTeam(newTeam);
        user2.addTeam(newTeam);
        user3.addTeam(newTeam);

        teamRepository.save(newTeam);

        // userRepository.save(user);

        // Team newTeam = Team.builder()
        //                    .teamName("new Team")
        //                    .build();
        //
        // User newUser = User.builder()
        //                    .userName("new User" + Math.random() * 10)
        //                    .team(newTeam)
        //                    .build();
        //
        // userRepository.save(newUser);
    }

    @Test
    @Transactional
    @Rollback(value = false)
    public void findTest() {
        // * N + 1 : 사이즈 제어
        // application.yml : spring.jpa.properties.hibernate.default_batch_fetch_size: {size}
        // in ( ?, ? ...) 이용해 조회된다.
        List<Team> allTeam = teamRepository.findAll();

        allTeam.forEach(v -> {
            System.out.println("team = ID: " + v.getTeamId() + " / name: " + v.getTeamName());

            for (User u : v.getUsers()) {
                System.out.println(" - User = ID: " + u.getUserId() + " / name: " + u.getUserName());
            }
        });

        // * N + 1 : join fetch (페치 조인)
        // @Query("select t from Team t join fetch t.users") 쿼리를 직접 작성해 조회한다.
        // List<Team> allTeamFetchJoin = teamRepository.findAllFetchJoin();
        //
        // allTeamFetchJoin.forEach(v -> {
        //     System.out.println("team = ID: " + v.getTeamId() + " / name: " + v.getTeamName());
        //
        //     for (User u : v.getUsers()) {
        //         System.out.println("User = ID: " + u.getUserId() + " / name: " + u.getUserName());
        //     }
        // });

        // List<User> allUser =  userRepository.findAll();
        //
        // allUser.forEach(v -> {
        //     System.out.println("User = ID: " + v.getUserId() + " / name: " + v.getUserName());
        //     System.out.println("Team = ID: " + v.getTeam().getTeamId() + " / name: " + v.getTeam().getTeamName());
        // });

        // List<User> allUserFetchJoin =  userRepository.findAllFetchJoin();
        //
        // allUserFetchJoin.forEach(v -> {
        //     System.out.println("User = ID: " + v.getUserId() + " / name: " + v.getUserName());
        //     System.out.println("Team = ID: " + v.getTeam().getTeamId() + " / name: " + v.getTeam().getTeamName());
        // });
    }

    @Test
    @Transactional
    @Rollback(value = false)
    public void updateTest() {
        Team team = teamRepository.findByTeamIdFetchJoin(4L);

        // * ConcurrentModificationException 처리를 위한 new ArrayList 생성
        ArrayList<User> users = new ArrayList<>(team.getUsers());
        users.forEach(User::removeTeam);

        // ! 연관관계 추가 (신규 사용자) 연결시 아이디 생성 정상은 정상이지만 team_id, user_name 은 null로 정보가 저장된다
        // User newUser = User.builder()
        //                    .userName("newUser" + Math.random() * 10)
        //                    .build();
        //
        // newUser.addTeam(team);

        // * 연관관계 제거 Cascade 전파 이슈로 팀 정보 선행 저장
        // teamRepository.save(team);
        // userRepository.deleteAll(users);

        // * 연관관계가 정리된 기존 팀 객체에 새로운 연관관계 맵핑 (정상)
        User newUser = User.builder()
                           .userName("newUser" + Math.random() * 10)
                           .build();

        newUser.updateTeam(team);

        userRepository.save(newUser);
    }

    @Test
    @Transactional
    @Rollback(value = false)
    public void deleteTest() {
        User user = userRepository.findById(1L).get();

        userRepository.delete(user);
    }
}
