package learniverse.learniversemain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ScheduleDTO {
    @NotNull(message = "roomId 입력은 필수입니다.")
    private Long roomId;
    @NotNull(message = "scheduleDate 입력은 필수입니다.")
    private LocalDateTime scheduleDate;
    @NotBlank(message = "scheduleTask 입력은 필수입니다.")
    private String scheduleTask;

}
