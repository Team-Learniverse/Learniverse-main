package learniverse.learniversemain.entity;

import jakarta.persistence.*;
import learniverse.learniversemain.entity.ID.RoomMemberID;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity(name = "room_members")
@IdClass(RoomMemberID.class)
public class RoomMemberEntity {
    @Id
    @Column(name = "room_id")
    private long roomId;
    @Id
    @Column(name = "member_id")
    private long memberId;
    @Column(nullable = false)
    private int is_leader;
    @Column(nullable = false)
    private int is_wait;
    @Column(nullable = false)
    private int is_pin;

    public RoomMemberEntity(long room_id, long member_id, int is_leader) {
        this.roomId = room_id;
        this.memberId = member_id;
        this.is_leader = is_leader;
        this.is_wait = (is_leader==0)? 1:0;
        this.is_pin = 0;
    }

    public RoomMemberEntity(RoomMemberID roomMemberID, int is_leader) {
        this.roomId = roomMemberID.getRoomId();
        this.memberId = roomMemberID.getMemberId();
        this.is_leader = is_leader;
        this.is_wait = (is_leader==0)? 1:0;
        this.is_pin = 0;
    }

    public void changePin(){
        setIs_pin((this.getIs_pin()==0)? 1:0);
    }
}
