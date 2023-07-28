package learniverse.learniversemain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(name = "issue_opinions")
public class IssueOpinionEntity {
    @Id
    @Column(name = "issue_id")
    private long issueId;
    @Column(name = "member_id", nullable = false)
    private long memberId;
    @Column(name = "issue_opinion", nullable = false)
    private String issueOpinion;
}
