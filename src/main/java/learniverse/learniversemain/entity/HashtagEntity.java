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
    @Column(name = "hashtag_id")
    private long hashtagId;
    @Column(name = "room_id", nullable = false)
    private long roomId;
    @Column(nullable = false)
    private String hashtag;

    public HashtagEntity(long roomId,String hashtag) {
        this.roomId = roomId;
        this.hashtag = hashtag;
    }

    @Override
    public boolean equals(Object obj){
        HashtagEntity hashtagEntity = (HashtagEntity) obj;
        if(this.roomId == hashtagEntity.getRoomId() && this.hashtag.equals(hashtagEntity.getHashtag()))
            return true;
        else return false;

    }
}
