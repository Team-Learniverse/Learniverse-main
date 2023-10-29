package learniverse.learniversemain.entity.mongoDB;

import learniverse.learniversemain.entity.MemberEntity;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Data
@Document(collection = "members")
public class MembersEntity {
    private String id;
    private long memberId;
    private LocalDate lastLoginDate;
    private LocalDate createdDate;

    public MembersEntity(long memberId) {
        this.memberId = memberId;
        this.createdDate = LocalDate.now();
    }
}
