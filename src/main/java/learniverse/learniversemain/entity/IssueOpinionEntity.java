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
    private long issue_id;
    @Column(nullable = false)
    private long member_id;
    @Column(nullable = false)
    private String issue_opinion;
}
