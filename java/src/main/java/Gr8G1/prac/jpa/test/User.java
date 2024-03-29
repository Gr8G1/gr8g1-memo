package Gr8G1.prac.jpa.test;

import lombok.*;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@Builder(toBuilder = true)
@Table(name = "USERS")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long userId;

    private String userName;

    // * 단방향 외래키 관리 시 CascadeType 에 따른 동작
    // CascadeType.PERSIST : Team 객체 참조된 상태면 Team 객체에 정보 또한 DB에 저장된다.
    // @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    // CascadeType.REMOVE : User 정보가 삭제될 때 Team 객체 또한 DB에서 제거된다.
    @ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST })
    @JoinColumn(name = "TEAM_ID")
    private Team team;

    public void addTeam(Team team) {
        this.team = team;

        if (!team.getUsers().contains(this))
            team.getUsers().add(this);
    }

    public void updateTeam(Team team) {
        if (this.team != null)
            this.team.getUsers().remove(this);

        this.team = team;
        team.getUsers().add(this);
    }

    public void removeTeam() {
        if (this.team != null) {
            this.team.getUsers().remove(this);
            this.team = null;
        }
    }

    // @Override
    // public boolean equals(Object o) {
    //     if (this == o) return true;
    //     if (o == null || getClass() != o.getClass()) return false;
    //     User user = (User) o;
    //     return Objects.equals(userId, user.userId);
    // }
}
