package learniverse.learniversemain.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class MoonDTO {
    @NotNull(message = "memberId 값은 필수입니다.")
    private Long memberId;
    @NotNull(message = "moonDate 값은 필수입니다.")
    private LocalDate moonDate;

    public MoonDTO(long memberId, LocalDate localDate) {
        this.memberId = memberId;
        this.moonDate = localDate;
    }
}
