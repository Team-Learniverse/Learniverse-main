package learniverse.learniversemain.dto;

import jakarta.validation.constraints.*;

import learniverse.learniversemain.dto.validGroups.Create;
import learniverse.learniversemain.dto.validGroups.Update;
import learniverse.learniversemain.entity.RoomEntity;
import lombok.*;

@Data
public class RoomDTO {
    @NotNull(groups = Create.class, message = "memberId 입력은 필수입니다.")
    private Long memberId;
    @NotNull(groups = Update.class, message = "roomId 입력은 필수입니다.")
    private Long roomId;
    @NotBlank(message = "방 이름은 필수입니다.")
    private String roomName;
    @Min(groups = {Create.class, Update.class}, value = 0, message = "카테고리 id는 0 이상입니다.")
    @Max(groups = {Create.class, Update.class}, value = 5, message = "카테고리 id는 5 이하입니다.")
    @NotNull(groups = {Create.class, Update.class}, message = "roomCategory 입력은 필수입니다. ")
    private Integer roomCategory;
    private String roomIntro;
    @NotNull(groups = {Create.class, Update.class}, message = "방 제한 인원 입력은 필수입니다.")
    @Min(groups = {Create.class, Update.class}, value = 2, message = "방 제한 인원은 2명 이상입니다.")
    @Max(groups = {Create.class, Update.class}, value = 5, message = "방 제한 인원은 5명 이하입니다.")
    private Integer roomLimit;
    @NotEmpty(groups = {Create.class, Update.class}, message = "해시태그 입력은 필수입니다.")
    private String[] roomHashtags;

    public static RoomEntity toRoomEntity(RoomDTO roomDTO){
        RoomEntity roomEntity = new RoomEntity();
        roomEntity.setRoomName(roomDTO.getRoomName());
        roomEntity.setRoomCategory(roomDTO.getRoomCategory());
        roomEntity.setRoomIntro(roomDTO.getRoomIntro());
        roomEntity.setRoomLimit(roomDTO.getRoomLimit());
        return roomEntity;
    }
}
