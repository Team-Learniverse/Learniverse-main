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
    private long room_id;
    @Column(nullable = false)
    private String room_name;
    @Column(nullable = true)
    private int room_category;
    @Column(nullable = true)
    private String room_intro;
    @Column(nullable = false)
    private int room_limit;
    @Column(nullable = true)
    private String room_git_org;
    @Column(nullable = true)
    private String room_notion;
    @Column(nullable = true)
    private String room_google_drive;
    @Column(nullable = true)
    private String room_figma;

    public static RoomEntity toRoomEntity(RoomDTO roomDTO){
        RoomEntity roomEntity = new RoomEntity();
        roomEntity.setRoom_id(roomDTO.getRoom_id());
        roomEntity.setRoom_name(roomDTO.getRoom_name());
        roomEntity.setRoom_category(roomDTO.getRoom_category());
        roomEntity.setRoom_intro(roomDTO.getRoom_intro());
        roomEntity.setRoom_limit(roomDTO.getRoom_limit());
        roomEntity.setRoom_git_org(roomEntity.getRoom_git_org());
        roomEntity.setRoom_notion(roomEntity.getRoom_notion());
        roomEntity.setRoom_google_drive(roomEntity.getRoom_google_drive());
        roomEntity.setRoom_figma(roomEntity.getRoom_figma());

        return roomEntity;
    }



}
