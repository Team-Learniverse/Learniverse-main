package learniverse.learniversemain.entity;

import jakarta.persistence.*;
import learniverse.learniversemain.dto.RoomDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

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
        //roomEntity.setRoomId(roomDTO.getRoomId());
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


    public void update(RoomEntity newRoom) {
        this.roomName = newRoom.getRoomName();
        this.roomCategory = newRoom.getRoomCategory();
        this.roomIntro = newRoom.getRoomIntro();
        this.roomLimit = newRoom.getRoomLimit();
        this.roomGitOrg = newRoom.getRoomGitOrg();
        this.roomNotion = newRoom.getRoomNotion();
        this.roomGoogleDrive = newRoom.getRoomGoogleDrive();
        this.roomFigma = newRoom.getRoomFigma();
    }

    public Map<String, String> getPath(String path) {
        System.out.println(path);
        Map<String, String> data = new HashMap<>();
        if(path.equals("workspace")){
            data.put("roomGitOrg", this.roomGitOrg);
            data.put("roomNotion", this.roomNotion);
            data.put("roomGoogleDrive", this.roomGoogleDrive);
            data.put("roomFigma", this.roomFigma);
        }
        else if (path.equals("roomName")) {
            data.put("roomName", this.roomName);
        }
        else if (path.equals("roomIntro")) {
            data.put("roomIntro", this.roomIntro);
        }
        else return  null;
        return data;
    }
}
