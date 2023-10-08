package learniverse.learniversemain.entity.mongoDB;

import jakarta.persistence.Column;
import learniverse.learniversemain.entity.RoomEntity;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Data
@Document("rooms")
public class RoomsEntity {
    private long roomId;
    private String roomName;
    private String roomIntro;
    private String roomHashtags = "";
    private String roomLanguages = "";
    private String roomCategory;
    private LocalDate createdDate;

    public RoomsEntity(RoomEntity roomEntity, String roomCategory, String[] hashtags) {
        this.roomId = roomEntity.getRoomId();
        this.roomName = roomEntity.getRoomName();
        this.roomIntro = roomEntity.getRoomIntro();
        for(String hashtag : hashtags)
            this.roomHashtags += hashtag+" ";
        this.roomLanguages = roomEntity.getRoomLanguages();
        this.roomCategory = roomCategory;
        this.createdDate = LocalDate.now();
    }
}
