package learniverse.learniversemain.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ProfileDTO {
    @NotNull(message = "memberId 값은 필수입니다.")
    private Long memberId;
    //@NotNull(message = "nickname 값은 필수입니다.")
    private String nickname;
    //@NotNull(message = "memberMessage 값은 필수입니다.")
    private String memberMessage;
}
