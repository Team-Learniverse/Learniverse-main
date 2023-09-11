package learniverse.learniversemain.jwt;

import lombok.*;

@Getter
@AllArgsConstructor
public class Token {
    private final String accessToken;
    private final String refreshToken;
}