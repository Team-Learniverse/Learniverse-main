package learniverse.learniversemain.entity.mongoDB;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "Gitcodes")
public class GitcodeEntity {
    private String id;
    private long issueId;
    private String gitCode;

}
