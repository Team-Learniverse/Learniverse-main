package learniverse.learniversemain.entity;

import jakarta.persistence.*;
import learniverse.learniversemain.dto.FcmTokenDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity(name = "fcm_token")
public class FcmTokenEntity {
    //    고유 id, 생성자 id, 토큰, 생성시간
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "_id")
    private long tokenId;
    @Column(name = "member_id")
    private long memberId;
    @Column(name = "token")
    private String token;
    @CreatedDate
    private LocalDateTime createdDate;
    @LastModifiedDate
    private LocalDateTime updatedDate;

    public void update(FcmTokenEntity fcmTokenEntity) {
        LocalDateTime now = LocalDateTime.now();
        this.token = fcmTokenEntity.getToken();
        this.updatedDate = now;
    }

    public FcmTokenEntity(FcmTokenDTO fcmTokenDTO){
        LocalDateTime now = LocalDateTime.now();
        this.memberId = fcmTokenDTO.getMemberId();
        this.token = fcmTokenDTO.getToken();
        this.createdDate = now;
        this.updatedDate = now;
    }
}
