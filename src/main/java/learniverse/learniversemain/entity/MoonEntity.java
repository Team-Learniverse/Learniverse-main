package learniverse.learniversemain.entity;

import jakarta.persistence.*;
import learniverse.learniversemain.dto.MoonDTO;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

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
    private LocalDate moonDate;
    @Column(nullable = false, name = "moon_score")
    private Integer moonScore;

    public MoonEntity(){}

    public MoonEntity(MoonDTO moonDTO) {
        this.memberId = moonDTO.getMemberId();
        this.moonDate = moonDTO.getMoonDate();
        this.moonScore = 1;
    }
}
