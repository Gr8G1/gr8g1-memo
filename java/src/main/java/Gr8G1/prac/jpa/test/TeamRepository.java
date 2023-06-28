package Gr8G1.prac.jpa.test;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TeamRepository extends JpaRepository<Team, Long> {
    @Query("select t from Team t join fetch t.users")
    List<Team> findAllFetchJoin();

    @Query("select t from Team t join fetch t.users where t.teamId = :teamId")
    Team findByTeamIdFetchJoin(Long teamId);
}
