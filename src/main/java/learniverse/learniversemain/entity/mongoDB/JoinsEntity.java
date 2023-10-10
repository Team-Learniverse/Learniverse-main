package learniverse.learniversemain.entity.mongoDB;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Data
@Document(collection = "joins")
public class JoinsEntity {
    private String id;
    private Long roomId;
    private Long memberId;
    private boolean isDefault;
    private LocalDate pinDate;
    private int enterRoom;

    public JoinsEntity(Long memberId, long roomId, boolean isDefault) {
        this.memberId = memberId;
        this.roomId = roomId;
        this.isDefault = isDefault;
        this.enterRoom = 0;
    }
}
