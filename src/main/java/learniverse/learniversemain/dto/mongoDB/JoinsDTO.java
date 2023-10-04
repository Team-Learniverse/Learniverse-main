package learniverse.learniversemain.dto.mongoDB;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class JoinsDTO {
    @NotNull(message = "memberId 입력은 필수입니다.")
    private Long memberId;
    @NotEmpty(message = "roomIds 입력은 필수입니다.")
    private Long[] roomIds;
}
