package learniverse.learniversemain.dto.mongoDB;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document(collection = "defaultRooms")
public class DefaultRoomsDTO {
    private Long roomId;
    private String roomName;
    private String roomIntro;
    private String roomHashtags;
    private String roomCategory;
    private String roomLanguages;

}
