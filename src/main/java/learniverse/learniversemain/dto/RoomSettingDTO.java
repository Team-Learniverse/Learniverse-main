package learniverse.learniversemain.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import learniverse.learniversemain.dto.validGroups.Update;
import learniverse.learniversemain.entity.RoomSettingEntity;
import lombok.Data;

import java.util.Arrays;

@Data
public class RoomSettingDTO {
    @NotNull(groups = Update.class, message = "roomId 입력은 필수입니다.")
    private long roomId;
    @NotNull(message = "languages 배열 입력은 필수입니다.")
    private String[] languages;

}
