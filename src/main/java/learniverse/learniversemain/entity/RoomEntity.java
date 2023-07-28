package learniverse.learniversemain.entity;

import jakarta.persistence.*;
import learniverse.learniversemain.dto.RoomDTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(name = "rooms")
public class RoomEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "room_id")
    private long roomId;
    @Column(name = "room_name", nullable = false)
    private String roomName;
    @Column(name = "room_category",nullable = true)
    private int roomCategory;
    @Column(name = "room_intro",nullable = true)
    private String roomIntro;
    @Column(name = "room_limit",nullable = false)
    private int roomLimit;
    @Column(name = "room_git_org",nullable = true)
    private String roomGitOrg;
    @Column(name = "room_notion",nullable = true)
    private String roomNotion;
    @Column(name = "room_google_drive",nullable = true)
    private String roomGoogleDrive;
    @Column(name = "room_figma",nullable = true)
    private String roomFigma;

    public static RoomEntity toRoomEntity(RoomDTO roomDTO){
        RoomEntity roomEntity = new RoomEntity();
        roomEntity.setRoomId(roomDTO.getRoom_id());
        roomEntity.setRoomName(roomDTO.getRoom_name());
        roomEntity.setRoomCategory(roomDTO.getRoom_category());
        roomEntity.setRoomIntro(roomDTO.getRoom_intro());
        roomEntity.setRoomLimit(roomDTO.getRoom_limit());
        roomEntity.setRoomGitOrg(roomDTO.getRoom_git_org());
        roomEntity.setRoomNotion(roomDTO.getRoom_notion());
        roomEntity.setRoomGoogleDrive(roomDTO.getRoom_google_drive());
        roomEntity.setRoomFigma(roomDTO.getRoom_figma());

        return roomEntity;
    }



}
