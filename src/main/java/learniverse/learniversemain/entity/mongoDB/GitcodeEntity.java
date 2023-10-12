package learniverse.learniversemain.entity.mongoDB;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document(collection = "Gitcodes")
public class GitcodeEntity {
    private String id;
    private long issueId;
    private long RoomId;
    private String gitCode;
    @CreatedDate
    private LocalDateTime createdDate;

    /*public GitcodeEntity(long issueId, long RoomId, String gitCode){
        this.issueId = issueId;
        this.RoomId = RoomId;
        this.gitCode = gitCode;
    }*/

}
