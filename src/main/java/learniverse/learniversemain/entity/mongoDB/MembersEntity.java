package learniverse.learniversemain.entity.mongoDB;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Data
@Document(collection = "members")
public class MembersEntity {
    private long memberId;
    private LocalDate lastLoginDate;
}
