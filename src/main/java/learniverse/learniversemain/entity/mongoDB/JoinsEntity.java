package learniverse.learniversemain.entity.mongoDB;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "joins")
public class JoinsEntity {
    private Long roomId;
    private Long memberId;
    private boolean isDefault;

    public JoinsEntity(Long memberId, long roomId, boolean isDefault) {
        this.memberId = memberId;
        this.roomId = roomId;
        this.isDefault = isDefault;
    }
}
