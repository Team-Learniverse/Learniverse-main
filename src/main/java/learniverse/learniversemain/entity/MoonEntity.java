package learniverse.learniversemain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity(name = "member_moon")
public class MoonEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "moon_id")
    private Long moonId;
    @Column(nullable = false, name = "member_id")
    private Long memberId;
    @Column(nullable = false, name = "moon_date")
    private LocalDateTime moonDate;
    @Column(nullable = false, name = "moon_socre")
    private Integer moonScore;
}
