package learniverse.learniversemain.dto;

import learniverse.learniversemain.entity.RoomSettingEntity;
import lombok.Data;

import java.util.Arrays;

@Data
public class RoomSettingDTO {
    private int settingId;
    private String name;

    public RoomSettingDTO(RoomSettingEntity roomSettingEntity) {
        this.settingId = roomSettingEntity.getSettingId();
        this.name = roomSettingEntity.getName();
    }

    public RoomSettingDTO(int i, String name) {
        this.settingId = i;
        this.name = name;
    }
}
