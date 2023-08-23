package learniverse.learniversemain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(name = "member_status")
public class MemberStatusEntity {
    @Id
    @Column(name = "member_id")
    private long memberId;
    @Column(nullable = false, name = "member_status")
    private boolean memberStatus;

    public boolean getMemberStatus() {
        return this.memberStatus;
    }
}
