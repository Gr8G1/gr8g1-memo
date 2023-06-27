package Gr8G1.prac.jpa.test;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    @Query("select u from User u join fetch u.team")
    List<User> findAllFetchJoin();
}
