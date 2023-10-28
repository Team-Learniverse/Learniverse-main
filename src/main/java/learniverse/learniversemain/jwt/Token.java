package learniverse.learniversemain.jwt;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class Token {
    private String accessToken;
    private String refreshToken;
    public Token(String token, String refreshToken) {
        this.accessToken = token;
        this.refreshToken = refreshToken;
    }
}