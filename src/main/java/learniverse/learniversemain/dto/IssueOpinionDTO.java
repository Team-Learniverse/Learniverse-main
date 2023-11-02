package learniverse.learniversemain.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class IssueOpinionDTO {
    @NotNull(message = "issueId 입력은 필수입니다.")
    private Long issueId;
    @NotNull(message = "memberId 입력은 필수입니다.")
    private Long memberId;
    @NotNull(message = "issueOpinion 입력은 필수입니다.")
    private String issueOpinion;
    private Integer issueOpinionStartLine;
    private Integer issueOpinionEndLine;
    private String issueOpinionCode;


}
