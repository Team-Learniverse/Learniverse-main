package learniverse.learniversemain.entity.mongoDB;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDate;

@Data
@Document(collection = "memberGitLang")
public class LangEntity {
    private Long memberId;
    private String language;
    @Field(name = "byte")
    private long bytes;

    public LangEntity(long memberId, String key, Long cnt) {
        this.memberId = memberId;
        this.language = key;
        this.bytes = cnt;
    }
}
