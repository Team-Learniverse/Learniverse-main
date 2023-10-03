package learniverse.learniversemain.entity;

import jakarta.persistence.*;
import learniverse.learniversemain.dto.IssueDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity(name = "issues")
public class IssueEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "issue_id")
    private long issueId;
    @Column(name = "member_id", nullable = false)
    private long memberId;
    @Column(name = "room_id", nullable = false)
    private long roomId;
    @Column(name = "issue_title", nullable = false)
    private String issueTitle;
    @Column(name = "issue_description", nullable = false)
    private String issueDescription;
    @Column(name = "issue_git_url", nullable = false)
    private String issueGitUrl;
    @Column(name = "git_file_name", nullable = false)
    private String gitFileName;
    @Column(name = "git_code", nullable = true)
    private String gitCode;
    @CreatedDate
    private LocalDateTime createdDate;




    public IssueEntity(IssueDTO issueDTO) {
        LocalDateTime now = LocalDateTime.now();

        this.memberId=issueDTO.getMemberId();
        this.roomId=issueDTO.getRoomId();
        this.issueTitle=issueDTO.getIssueTitle();
        this.issueDescription=issueDTO.getIssueDescription();
        this.issueGitUrl=issueDTO.getIssueGitUrl();
        this.gitFileName=issueDTO.getGitFileName();
        this.createdDate = now;

    }
}

