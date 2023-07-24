package learniverse.learniversemain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(name = "hashtags")
public class HashtagEntity {
    @Id
    private long room_id;
    @Column(nullable = false)
    private String hashtag;
}
