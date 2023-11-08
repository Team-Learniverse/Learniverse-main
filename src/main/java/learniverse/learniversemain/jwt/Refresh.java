package learniverse.learniversemain.jwt;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity(name="refresh_token")
public class Refresh {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "refresh_token_id")
    private Long tokenId;
    @Column(name = "member_id")
    private Long memberId;
    @Column(name = "refresh_token")
    private String token;
    @CreatedDate
    private LocalDateTime createdDate;
    @LastModifiedDate
    private LocalDateTime updatedDate;


    public Refresh(long memberId, String refreshToken) {
        LocalDateTime now = LocalDateTime.now();
        this.memberId=memberId;
        this.token=refreshToken;
        this.createdDate = now;
    }
}
