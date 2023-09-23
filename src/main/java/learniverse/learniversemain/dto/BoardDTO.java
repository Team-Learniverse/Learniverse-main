package learniverse.learniversemain.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class BoardDTO {
    @NotNull(message = "roomId 입력은 필수입니다.")
    private Long roomId;
    @NotNull(message = "memberId 입력은 필수입니다.")
    private Long memberId;
    @NotNull(message = "title 입력은 필수입니다.")
    private String title;
    @NotNull(message = "content 입력은 필수입니다.")
    private String content;
}
