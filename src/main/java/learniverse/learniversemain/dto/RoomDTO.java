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

    public static RoomEntity toRoomEntity(RoomDTO roomDTO){
        RoomEntity roomEntity = new RoomEntity();
        roomEntity.setRoomId(roomDTO.getRoomId());
        roomEntity.setRoomName(roomDTO.getRoomName());
        roomEntity.setRoomCategory(roomDTO.getRoomCategory());
        roomEntity.setRoomIntro(roomDTO.getRoomIntro());
        roomEntity.setRoomLimit(roomDTO.getRoomLimit());
        roomEntity.setRoomGitOrg(roomDTO.getRoomGitOrg());
        roomEntity.setRoomNotion(roomDTO.getRoomNotion());
        roomEntity.setRoomGoogleDrive(roomDTO.getRoomGoogleDrive());
        roomEntity.setRoomFigma(roomDTO.getRoomFigma());
        return roomEntity;
    }
}
