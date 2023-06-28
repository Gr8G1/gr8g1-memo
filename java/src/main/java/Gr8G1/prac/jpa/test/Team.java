package Gr8G1.prac.jpa.test;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder(toBuilder = true)
public class Team {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long teamId;

    private String teamName;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "team", cascade = { CascadeType.PERSIST })
    private List<User> users = new ArrayList<>();
}
