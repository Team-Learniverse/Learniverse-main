package learniverse.learniversemain.dto;

import jakarta.validation.constraints.NotNull;
import learniverse.learniversemain.dto.validGroups.Update;
import lombok.Data;

@Data
public class WorkspaceDTO {
    @NotNull(groups = Update.class, message = "roomId 입력은 필수입니다.")
    private Long roomId;
    private String roomGitOrg;
    private String roomNotion;
    private String roomGoogleDrive;
    private String roomFigma;

}
