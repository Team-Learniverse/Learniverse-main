package learniverse.learniversemain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import learniverse.learniversemain.entity.pk.RoomMemberPK;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(name = "room_members")
public class RoomMemberEntity {
    @EmbeddedId
    private RoomMemberPK roomMemberPK;
    @Column(nullable = false)
    private int is_leader;
    @Column(nullable = false)
    private int is_pin;
}
