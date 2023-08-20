package learniverse.learniversemain.dto;

import jakarta.persistence.Column;

import jakarta.validation.constraints.*;

import learniverse.learniversemain.entity.ID.RoomMemberID;
import learniverse.learniversemain.entity.RoomEntity;
import lombok.*;

@Data
public class RoomDTO {
    @NotNull(message = "멤버 id 입력은 필수입니다.")
    private Long memberId;
    private Long roomId;
    @NotBlank(message = "방 이름은 필수입니다.")
    private String roomName;
    private Integer roomCategory;
    private String roomIntro;
    @NotNull(message = "방 제한 인원 입력은 필수입니다.")
    @Min(value = 2, message = "방 제한 인원은 2명 이상입니다.")
    @Max(value = 5, message = "방 제한 인원은 5명 이하입니다.")
    private Integer roomLimit;
    private String roomGitOrg;
    private String roomNotion;
    private String roomGoogleDrive;
    private String roomFigma;
    @NotEmpty(message = "해시태그 입력은 필수입니다.")
    private String[] roomHashtags;

    public static RoomEntity toRoomEntity(RoomDTO roomDTO){
        RoomEntity roomEntity = new RoomEntity();
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
