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
