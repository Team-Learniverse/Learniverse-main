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
    @Column(name = "is_leader", nullable = false)
    private int isLeader;
    @Column(name = "is_wait", nullable = false)
    private int isWait;
    @Column(name = "is_pin", nullable = false)
    private int isPin;

    public RoomMemberEntity(long room_id, long member_id, int is_leader) {
        this.roomId = room_id;
        this.memberId = member_id;
        this.isLeader = is_leader;
        this.isWait = (is_leader==0)? 1:0;
        this.isPin = 0;
    }

    public RoomMemberEntity(RoomMemberID roomMemberID, int is_leader) {
        this.roomId = roomMemberID.getRoomId();
        this.memberId = roomMemberID.getMemberId();
        this.isLeader = is_leader;
        this.isWait = (is_leader==0)? 1:0;
        this.isPin = 0;
    }

    public void changePin(){
        setIsPin((this.getIsPin()==0)? 1:0);

    }
}
