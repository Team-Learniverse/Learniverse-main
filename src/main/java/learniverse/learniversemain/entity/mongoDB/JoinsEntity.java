package learniverse.learniversemain.entity.mongoDB;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Document(collection = "joins")
public class JoinsEntity {
    private String id;
    private Long roomId;
    private Long memberId;
    private boolean isDefault;
    private LocalDate pinDate;
    private int enterRoom;
    private LocalDate createdDate;

    public JoinsEntity(Long memberId, long roomId, boolean isDefault) {
        this.memberId = memberId;
        this.roomId = roomId;
        this.isDefault = isDefault;
        this.enterRoom = 0;
        LocalDateTime now = LocalDateTime.now();
        LocalDate localDate = now.toLocalDate();
        this.createdDate = localDate;
    }
}
