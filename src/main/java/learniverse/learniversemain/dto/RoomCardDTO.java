package learniverse.learniversemain.dto;

import learniverse.learniversemain.dto.mongoDB.DefaultRoomsDTO;
import learniverse.learniversemain.entity.RoomEntity;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class RoomCardDTO {
    private Long roomId;
    private String roomName;
    private String roomIntro;
    private List<String> roomHashtags;
    private String roomCategory;
    private int roomCount; // 참여 인원
    private int roomLimit; // 제한 인원
    private String isMember; // "팀장" "승인" "대기" "거절"

    public RoomCardDTO(RoomEntity roomEntity, List<String> hashtags, String roomCategory, String isMember, int roomCount) {
        this.roomId = roomEntity.getRoomId();
        this.roomName = roomEntity.getRoomName();
        this.roomIntro = roomEntity.getRoomIntro();
        this.roomHashtags = hashtags;
        this.roomCategory = roomCategory;
        this.roomCount = roomCount;
        this.roomLimit = roomEntity.getRoomLimit();
        this.isMember = isMember;

    }

    public RoomCardDTO(DefaultRoomsDTO defaultRoomsDTO) {
        this.roomId = defaultRoomsDTO.getRoomId();
        this.roomName = defaultRoomsDTO.getRoomName();
        this.roomIntro = defaultRoomsDTO.getRoomIntro();
        String[] hashtags = defaultRoomsDTO.getRoomHashtags().split(" ");
        List<String> hashtagList = new ArrayList<>();
        for(String hashtag : hashtags) hashtagList.add(hashtag);
        this.roomHashtags = hashtagList;
        this.roomCategory = defaultRoomsDTO.getRoomCategory();
    }
}
