package learniverse.learniversemain.jwt;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity(name="refresh_token")
public class Refresh {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "refresh_token_id")
    private long tokenId;
    @Column(name = "member_id")
    private long memberId;
    @Column(name = "refresh_token")
    private String token;
    @CreatedDate
    private LocalDateTime createdDate;

    public Refresh(int memberId, String refreshToken) {
        LocalDateTime now = LocalDateTime.now();
        this.memberId=memberId;
        this.token=refreshToken;
        this.createdDate = now;
    }
}
