package learniverse.learniversemain.entity;

import jakarta.persistence.*;
import learniverse.learniversemain.entity.ID.RoomMemberID;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

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
    private boolean isLeader;
    @Column(name = "is_wait", nullable = false)
    private boolean isWait;
    @Column(name = "is_reject", nullable = false)
    private boolean isReject;
    @Column(name = "is_pin", nullable = false)
    private boolean isPin;
    @Column(name = "join_time", nullable = true)
    private LocalDateTime joinTime;
    @Column(name = "pin_time", nullable = true)
    private LocalDateTime pinTime;

    public RoomMemberEntity(long room_id, long member_id, boolean is_leader) {
        this.roomId = room_id;
        this.memberId = member_id;
        this.isLeader = is_leader;
        this.isWait = (is_leader == true) ? false : true;
        this.joinTime = LocalDateTime.now();
        this.isReject = false;
        this.isPin = false;
    }

    public RoomMemberEntity(RoomMemberID roomMemberID, boolean is_leader) {
        this.roomId = roomMemberID.getRoomId();
        this.memberId = roomMemberID.getMemberId();
        this.isLeader = is_leader;
        this.isWait = (is_leader == true) ? false : true;
        this.isReject = false;
        this.isPin = false;
    }

    public void changePin() {
        setPin((this.isPin() == false) ? true : false);
    }

}
