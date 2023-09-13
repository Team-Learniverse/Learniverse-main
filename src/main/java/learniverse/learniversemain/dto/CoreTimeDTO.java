package learniverse.learniversemain.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CoreTimeDTO {
    @NotNull(message = "roomId 입력은 필수입니다.")
    private Long roomId;
    @NotNull(message = "coreStartDate 입력은 필수입니다.")
    private LocalDateTime coreStartDate;
    @NotNull(message = "coreEndDate 입력은 필수입니다.")
    private LocalDateTime coreEndDate;
}