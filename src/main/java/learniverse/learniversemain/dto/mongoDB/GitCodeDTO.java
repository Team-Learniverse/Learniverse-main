package learniverse.learniversemain.dto.mongoDB;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class GitCodeDTO {
    @NotNull(message = " issueId 입력은 필수입니다.")
    private Long issueId;
    @NotNull(message = " roomId 입력은 필수입니다.")
    private Long roomId;
    @NotEmpty(message = "gitCode 입력은 필수입니다.")
    private String gitCode;
}
