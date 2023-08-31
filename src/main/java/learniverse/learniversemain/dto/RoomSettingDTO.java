package learniverse.learniversemain.dto;

import learniverse.learniversemain.entity.RoomSettingEntity;
import lombok.Data;

import java.util.Arrays;

@Data
public class RoomSettingDTO {
    private int setting_id;
    private String name;

    public RoomSettingDTO(RoomSettingEntity roomSettingEntity) {
        this.setting_id = roomSettingEntity.getSettingId();
        this.name = roomSettingEntity.getName();
    }
}
