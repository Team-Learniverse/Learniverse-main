package learniverse.learniversemain.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class MoonDTO {
    @NotNull(message = "memberId 값은 필수입니다.")
    private Long memberId;
    @NotNull(message = "moonDate 값은 필수입니다.")
    private LocalDate moonDate;
}
