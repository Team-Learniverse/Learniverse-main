package learniverse.learniversemain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class HashtagDTO {
    @NotNull(message = "roomId 입력은 필수입니다.")
    private Long roomId;
    @NotEmpty(message = "해시태그 리스트 입력은 필수입니다.")
    private List<String> hashtags;
}
