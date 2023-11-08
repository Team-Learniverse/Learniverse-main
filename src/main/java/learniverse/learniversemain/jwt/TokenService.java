package learniverse.learniversemain.jwt;

import io.jsonwebtoken.*;
import jakarta.transaction.Transactional;
import learniverse.learniversemain.controller.Exception.CustomBadRequestException;
import learniverse.learniversemain.controller.Exception.CustomUnauthorizedException;
import learniverse.learniversemain.entity.BoardEntity;
import learniverse.learniversemain.repository.RefreshTokenRepository;
import learniverse.learniversemain.service.MemberService;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpServletRequest;


import java.security.Key;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Date;
import java.util.Optional;

@Slf4j
@Service
public class TokenService implements InitializingBean {
    private final RefreshTokenRepository refreshTokenRepository;
    private static final String BEARER_PREFIX = "Bearer ";
    private final String secret;
    private final long accessTokenValidityInMilliseconds;
    private final long refreshTokenValidityInMilliseconds;
    private final MemberService memberService;
    private Key key;

    public TokenService(RefreshTokenRepository refreshTokenRepository, @Value("${jwt.key}") String secret,
                        @Value("${jwt.expiration.access}") long accessTokenValidityInMilliseconds,
                        @Value("${jwt.expiration.refresh}") long refreshTokenValidityInMilliseconds,
                        MemberService memberService) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.secret = secret;
        this.accessTokenValidityInMilliseconds = accessTokenValidityInMilliseconds * 1000L;
        this.refreshTokenValidityInMilliseconds = refreshTokenValidityInMilliseconds * 1000L;
        this.memberService = memberService;
    }

    @Override
    public void afterPropertiesSet() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public Token generateToken(long memberId, String role) {
        log.info("generateToken");
        Claims claims = Jwts.claims().setSubject(String.valueOf(memberId));
        claims.put("role", role);

        Instant now = Instant.now();

        String accessToken = makeJwtValue(claims, now, accessTokenValidityInMilliseconds);
        String refreshToken = makeJwtValue(claims, now, refreshTokenValidityInMilliseconds);
        log.info(accessToken);
        log.info(refreshToken);

        saveRefreshToken(memberId, refreshToken);

        return new Token(accessToken, refreshToken);
    }


    //멤버id, 토큰 발급시간, 토큰 만료시간
    private String makeJwtValue(Claims claims, Instant now, long tokenValidityInMilliseconds) {
        return Jwts.builder().setClaims(claims).setIssuedAt(new Date(now.toEpochMilli()))
                .setExpiration(new Date(now.toEpochMilli() + tokenValidityInMilliseconds))
                .signWith(key, SignatureAlgorithm.HS256).compact();
    }

    private void saveRefreshToken(long memberId, String refreshToken) {
        log.info("saveRefreshToken");

        Optional<Refresh> existRefresh = refreshTokenRepository.findByMemberId(memberId);

        if(existRefresh.isPresent()){
            Refresh existingRefresh = existRefresh.get();
            existingRefresh.setToken(refreshToken); // 새로운 토큰으로 갱신
            existingRefresh.setUpdatedDate(LocalDateTime.now());
            log.info(String.valueOf(existingRefresh.getToken()));
            refreshTokenRepository.save(existingRefresh);
        }else{
            Refresh refresh = new Refresh(memberId, refreshToken);
            log.info(String.valueOf(refresh.getToken()));
            refreshTokenRepository.save(refresh);
        }
    }

    public boolean validateToken(String token) {
        log.info("validateToken");
        log.info(token);
        long memberId = getMemberId(token);
        log.info("Authenticated Member Id:" + String.valueOf(memberId));

        if (null == token) {
            throw new CustomUnauthorizedException("토큰이 없습니다.");
        }

        if (false == token.startsWith(BEARER_PREFIX)) {
            throw new CustomUnauthorizedException("올바르지 않은 토큰 포맷입니다.");
        }

        String value = token.substring(BEARER_PREFIX.length());

        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(value);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.error("잘못된 jwt 서명을 가진 토큰입니다", e);
        } catch (ExpiredJwtException e) {
            log.error("만료된 jwt 토큰입니다", e);
            throw new CustomUnauthorizedException("만료된 토큰입니다");
        } catch (UnsupportedJwtException e) {
            log.error("지원하지 않는 jwt 토큰입니다", e);
        } catch (IllegalArgumentException e) {
            log.error("잘못된 jwt 토큰입니다", e);
        }
        throw new CustomUnauthorizedException("토큰이 유효하지 않습니다");
    }

    public Authentication getAuthentication(String token) {
        long memberId = getMemberId(token);
        log.info("Authenticated Member Id:" + String.valueOf(memberId));
        return new UsernamePasswordAuthenticationToken(memberId, "",
                Arrays.asList(new SimpleGrantedAuthority("ROLE_USER")));
    }

    public Long getMemberId(String token) {
        String value = token.substring(BEARER_PREFIX.length());
        String subject = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(value).getBody()
                .getSubject();
        return Long.parseLong(subject);
    }

    public boolean validateRefreshToken(String token) {
        log.info("validateRefreshToken");

        Refresh refreshToken = refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new CustomUnauthorizedException("해당 Refresh 토큰을 찾을 수 없습니다."));

        return refreshToken != null && !isTokenExpired(refreshToken);
    }
    private boolean isTokenExpired(Refresh refreshToken) {
        LocalDateTime expirationDateTime = refreshToken.getCreatedDate().plusSeconds(refreshTokenValidityInMilliseconds/1000);
        return LocalDateTime.now().isAfter(expirationDateTime);
    }

    /*public Token updateAccessToken(long memberId, String role) {
        log.info("updateAccessToken");
        Claims claims = Jwts.claims().setSubject(String.valueOf(memberId));
        claims.put("role", role);

        Instant now = Instant.now();

        String accessToken = makeJwtValue(claims, now, accessTokenValidityInMilliseconds);
        Refresh existingRefreshToken = refreshTokenRepository.findByMemberId(memberId)
                .orElseThrow(() -> new CustomUnauthorizedException("해당 Refresh 토큰을 찾을 수 없습니다."));

        String refreshToken = existingRefreshToken.getToken();

        return new Token(accessToken, refreshToken);
    }*/

    public String refreshAccessToken(String refreshToken) {
        log.info("refresh Access Token");

        long memberId = Long.parseLong(Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(refreshToken).getBody().getSubject());

        Claims claims = Jwts.claims().setSubject(String.valueOf(memberId));
        claims.put("role", "USER");

        Instant now = Instant.now();
        String accessToken = makeJwtValue(claims, now, accessTokenValidityInMilliseconds);


        // 새로운 Access Token 생성
        Token newAccessToken = new Token(accessToken, refreshToken);

        log.info("new access token: " + newAccessToken.getAccessToken());
        log.info("refresh token: " +newAccessToken.getRefreshToken());

        return newAccessToken.getAccessToken();
    }

    @Transactional
    public void removeRefreshToken(String accessToken) {
        log.info("removeRefreshToken");
        log.info(accessToken);
        long memberId = getMemberId(accessToken);
        log.info("Authenticated Member Id:" + String.valueOf(memberId));
        Refresh refreshToken = refreshTokenRepository.findByMemberId(memberId)
                .orElseThrow(() -> new CustomUnauthorizedException("해당 Refresh 토큰을 찾을 수 없습니다."));

        refreshTokenRepository.delete(refreshToken);
    }



    //헤더에서 RefreshToken 추출: 헤더를 가져온 후 "Bearer"를 삭제
    public Optional<String> extractRefreshToken(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader("Refresh"))
                .filter(refreshToken -> refreshToken.startsWith("Bearer"))
                .map(refreshToken -> refreshToken.replace("Bearer", ""));
    }

    //헤더에서 AccessToken 추출: 헤더를 가져온 후 "Bearer"를 삭제
    public Optional<String> extractAccessToken(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader(HttpHeaders.AUTHORIZATION))
                .filter(refreshToken -> refreshToken.startsWith("Bearer"))
                .map(refreshToken -> refreshToken.replace("Bearer", ""));
    }

}
