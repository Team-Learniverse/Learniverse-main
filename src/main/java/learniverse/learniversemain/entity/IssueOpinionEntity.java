package learniverse.learniversemain.entity;

import jakarta.persistence.*;
import learniverse.learniversemain.dto.IssueOpinionDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity(name = "issue_opinions")
public class IssueOpinionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "opinion_id")
    private long opinionId;
    @Column(name = "issue_id", nullable = false)
    private long issueId;
    @Column(name = "member_id", nullable = false)
    private long memberId;
    @Column(name = "issue_opinion", nullable = false)
    private String issueOpinion;
    @Column(name = "issue_opinion_start_line", nullable = true)
    private Integer issueOpinionStartLine;
    @Column(name = "issue_opinion_end_line", nullable = true)
    private Integer issueOpinionEndLine;
    @Column(name = "issue_opinion_code", nullable = true)
    private String issueOpinionCode;
    @CreatedDate
    private LocalDateTime createdDate;

    public IssueOpinionEntity(IssueOpinionDTO issueOpinionDTO) {
        LocalDateTime now = LocalDateTime.now();

        this.issueId = issueOpinionDTO.getIssueId();
        this.memberId = issueOpinionDTO.getMemberId();
        this.issueOpinion = issueOpinionDTO.getIssueOpinion();
        this.issueOpinionStartLine = issueOpinionDTO.getIssueOpinionStartLine();
        this.issueOpinionEndLine = issueOpinionDTO.getIssueOpinionEndLine();
        this.issueOpinionCode = issueOpinionDTO.getIssueOpinionCode();
        this.createdDate = now;

    }

}
