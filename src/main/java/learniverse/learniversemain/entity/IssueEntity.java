package learniverse.learniversemain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(name = "issues")
public class IssueEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long issue_id;
    @Column(nullable = false)
    private int member_id;
    @Column(nullable = false)
    private int room_id;
    @Column(nullable = false)
    private String issue_title;
    @Column(nullable = false)
    private String issue_description;
    @Column(nullable = false)
    private String issue_git;
    @Column(nullable = false)
    private String created_time;
}
