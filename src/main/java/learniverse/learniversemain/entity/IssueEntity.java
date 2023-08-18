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
    @Column(name = "issue_id")
    private long issueId;
    @Column(name = "member_id", nullable = false)
    private int memberId;
    @Column(name = "room_id", nullable = false)
    private int roomId;
    @Column(name = "issue_title", nullable = false)
    private String issueTitle;
    @Column(name = "issue_description", nullable = false)
    private String issueDescription;
    @Column(name = "issue_git", nullable = false)
    private String issueGit;
    @Column(name = "created_time", nullable = false)
    private String createdTime;
}
