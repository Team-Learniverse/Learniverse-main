package learniverse.learniversemain.dto;

import jakarta.validation.constraints.NotNull;
import learniverse.learniversemain.entity.IssueEntity;
import learniverse.learniversemain.entity.RoomEntity;
import lombok.Data;

@Data
public class IssueDTO {
    @NotNull(message = "roomId 입력은 필수입니다.")
    private Long roomId;
    @NotNull(message = "memberId 입력은 필수입니다.")
    private Long memberId;
    @NotNull(message = "issueTitle 입력은 필수입니다.")
    private String issueTitle;
    @NotNull(message = "issueDescription 입력은 필수입니다.")
    private String issueDescription;
    @NotNull(message = "issueGitUrl 입력은 필수입니다.")
    private String issueGitUrl;
    @NotNull(message = "gitFileName 입력은 필수입니다.")
    private String gitFileName;
    //private String gitCode;
    private long issueId;
}
