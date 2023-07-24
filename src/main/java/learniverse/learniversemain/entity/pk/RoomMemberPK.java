package learniverse.learniversemain.entity.pk;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Id;
import lombok.Data;

import java.io.Serializable;

@Data
@Embeddable
public class RoomMemberPK implements Serializable {
    private long room_id;
    private long member_id;
}
