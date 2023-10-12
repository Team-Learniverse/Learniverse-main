package learniverse.learniversemain.entity.mongoDB;

import lombok.Data;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document(collection = "Gitcodes")
public class GitcodeEntity {
    private String id;
    private long issueId;
    private long RoomId;
    private String gitCode;
    @LastModifiedDate
    private LocalDateTime updatedDate;

}
