package learniverse.learniversemain.entity.mongoDB;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.awt.*;
import java.time.LocalDate;

@Data
@Document(collection = "searchHistory")
public class HistoryEntity {
    private long memberId;
    private String search;
    private LocalDate createdDate;

    public HistoryEntity(long memberId, String search, LocalDate createdDate){
        this.memberId = memberId;
        this.search = search;
        this.createdDate = createdDate;
    }
}
