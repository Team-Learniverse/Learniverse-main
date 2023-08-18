package learniverse.learniversemain.dto;

import jakarta.persistence.Column;
import learniverse.learniversemain.entity.ID.RoomMemberID;
import learniverse.learniversemain.entity.RoomEntity;
import lombok.*;

@Data
public class RoomDTO {
    private long member_id;
    private long room_id;
    private String[] room_hashtags;
    private String room_name;
    private int room_category;
    private String room_intro;
    private int room_limit;
    private String room_git_org;
    private String room_notion;
    private String room_google_drive;
    private String room_figma;
}
