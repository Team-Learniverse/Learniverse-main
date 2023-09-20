package learniverse.learniversemain.entity.ID;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
public class RoomMemberID implements Serializable {
    @NotNull(message = "roomId 입력은 필수입니다.")
    private Long roomId;
    @NotNull(message = "memberId 입력은 필수입니다.")
    private Long memberId;

    public RoomMemberID(){

    }
    public RoomMemberID(long roomId, long memberId) {
        this.roomId = roomId;
        this.memberId = memberId;
    }
}
