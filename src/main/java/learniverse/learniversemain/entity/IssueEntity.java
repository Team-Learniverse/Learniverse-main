package learniverse.learniversemain.entity;

import jakarta.persistence.*;
import learniverse.learniversemain.dto.IssueDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity(name = "issues")
public class IssueEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "issue_id")
    private Long issueId;
    @Column(name = "member_id", nullable = false)
    private Long memberId;
    @Column(name = "room_id", nullable = false)
    private Long roomId;
    @Column(name = "issue_title", nullable = false)
    private String issueTitle;
    @Column(name = "issue_description", nullable = false)
    private String issueDescription;
    @Column(name = "issue_git_url", nullable = false)
    private String issueGitUrl;
    @Column(name = "git_file_name", nullable = true)
    private String gitFileName;
    @Column(name = "git_issue_number", nullable = true)
    private String gitIssueNumber;
    @Column(name = "issue_isOpen", nullable = false)
    private Boolean issueOpen;
    @CreatedDate
    private LocalDateTime createdDate;
    @LastModifiedDate
    private LocalDateTime updatedDate;

    public IssueEntity(IssueDTO issueDTO) {
        LocalDateTime now = LocalDateTime.now();

        this.memberId = issueDTO.getMemberId();
        this.roomId = issueDTO.getRoomId();
        this.issueTitle = issueDTO.getIssueTitle();
        this.issueDescription = issueDTO.getIssueDescription();
        this.issueGitUrl = issueDTO.getIssueGitUrl();
        this.gitFileName = issueDTO.getGitFileName();
        this.createdDate = now;

    }
}

