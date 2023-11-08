package learniverse.learniversemain.jwt;

import io.swagger.v3.oas.annotations.tags.Tag;
import learniverse.learniversemain.controller.response.Response;
import learniverse.learniversemain.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@Tag(name = "token", description = "token 관련 api")
@RestController
@RequiredArgsConstructor
@RequestMapping("/token")
@Validated
public class AuthController {
    private final RefreshTokenRepository tokenRepository;
    private final TokenService tokenService;

    @PostMapping("/logout")
    public ResponseEntity<Response> logout(@RequestHeader(HttpHeaders.AUTHORIZATION) final String accessToken) {
        //access token으로 refresh token 삭제
        log.info(accessToken);

        Response response = new Response();
        tokenService.removeRefreshToken(accessToken);

        response.setStatus(Response.StatusEnum.OK);
        response.setMessage("refresh token 삭제 성공");

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/refresh")
    public ResponseEntity<Response> refresh(@RequestHeader("Refresh") final String refreshToken) {
        log.info(refreshToken);

        Response response = new Response();
        //refresh token으로 access token 재발급
        if (tokenService.validateRefreshToken(refreshToken)) {
            String newAccessToken = tokenService.refreshAccessToken(refreshToken);

            log.info(newAccessToken);

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.AUTHORIZATION, newAccessToken); // AccessToken을 HttpHeaders에 추가

            response.setStatus(Response.StatusEnum.OK);
            response.setMessage("access token 재발급 성공");

            return ResponseEntity.ok()
                    .header(HttpHeaders.AUTHORIZATION, newAccessToken)
                    .body(response);
        }

        //refresh 토큰 기한 만료 or 존재하지 않으면
        response.setStatus(Response.StatusEnum.UNAUTHORIZED);
        response.setMessage("유효하지 않은 refresh token");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(response);

    }

}
