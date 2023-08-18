package learniverse.learniversemain.entity;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity(name = "hashtags")
public class HashtagEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long hashtag_id;
    @Column(nullable = false)
    private long room_id;
    @Column(nullable = false)
    private String hashtag;

    public HashtagEntity(long room_id,String hashtag) {
        this.room_id = room_id;
        this.hashtag = hashtag;
    }
}
