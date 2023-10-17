package learniverse.learniversemain.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private TokenService tokenService;

    public JwtAuthFilter(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    //jwt 토큰의 인증정보를 SecurityContext에 저장하는 역할 수행
    @Override
    protected void doFilterInternal(HttpServletRequest servletRequest,
                                    HttpServletResponse servletResponse, FilterChain filterChain)
            throws ServletException, IOException {

        HttpServletRequest httpServletRequest = servletRequest;
        String token = httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION); //access token
        String refresh = httpServletRequest.getHeader("Refresh"); //refresh token


        if (token != null && tokenService.validateToken(token)) { // JWT 토큰이 유효한 경우에만, USER객체 셋팅
            Authentication authentication = tokenService.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            log.info("jwt success");
        }else if (refresh != null && tokenService.isRefreshTokenValid(refresh)) {
            // Access Token이 만료되었고 Refresh Token이 유효한 경우
            String newAccessToken = tokenService.refreshAccessToken(token);

            if (newAccessToken != null) {
                // 새로운 Access Token을 응답 헤더에 추가
                servletResponse.setHeader(HttpHeaders.AUTHORIZATION, newAccessToken);
                SecurityContextHolder.getContext().setAuthentication(tokenService.getAuthentication(newAccessToken));

            }
        }
        filterChain.doFilter(servletRequest, servletResponse); // 다음 Filter를 실행(마지막 필터라면 필터 실행 후 리소스를 반환)
    }
}