package learniverse.learniversemain.entity.ID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoomMemberID implements Serializable {
    private long room_id;
    private long member_id;
}
