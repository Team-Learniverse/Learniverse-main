package learniverse.learniversemain.dto;

import jakarta.persistence.Column;
import learniverse.learniversemain.entity.ID.RoomMemberID;
import learniverse.learniversemain.entity.RoomEntity;
import lombok.*;

@Data
public class RoomDTO {
    private long memberId;
    private long roomId;
    private String[] roomHashtags;
    private String roomName;
    private int roomCategory;
    private String roomIntro;
    private int roomLimit;
    private String roomGitOrg;
    private String roomNotion;
    private String roomGoogleDrive;
    private String roomFigma;
}
