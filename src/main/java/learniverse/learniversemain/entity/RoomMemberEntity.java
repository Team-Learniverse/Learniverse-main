package learniverse.learniversemain.entity;

import jakarta.persistence.*;
import learniverse.learniversemain.entity.ID.RoomMemberID;
import learniverse.learniversemain.entity.pk.RoomMemberPK;
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
    private long room_id;
    @Id
    private long member_id;
    @Column(nullable = false)
    private int is_leader;
    @Column(nullable = false)
    private int is_wait;
    @Column(nullable = false)
    private int is_pin;

    public RoomMemberEntity(long room_id, long member_id, int is_leader) {
        this.room_id = room_id;
        this.member_id = member_id;
        this.is_leader = is_leader;
        this.is_wait = (is_leader==0)? 1:0;
        this.is_pin = 0;
    }

    public RoomMemberEntity(RoomMemberID roomMemberID, int is_leader) {
        this.room_id = roomMemberID.getRoom_id();
        this.member_id = roomMemberID.getMember_id();
        this.is_leader = is_leader;
        this.is_wait = (is_leader==0)? 1:0;
        this.is_pin = 0;
    }

    public void changePin(){
        setIs_pin((this.getIs_pin()==0)? 1:0);
    }
}
