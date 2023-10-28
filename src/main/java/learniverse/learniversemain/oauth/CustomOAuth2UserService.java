package learniverse.learniversemain.oauth;

import learniverse.learniversemain.entity.MemberEntity;
import learniverse.learniversemain.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService <OAuth2UserRequest, OAuth2User> {

    private final MemberService memberService;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        log.info("djdjj");

        log.info("getAccessToken: "+userRequest.getAccessToken().getTokenValue());

        OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService = new DefaultOAuth2UserService();

        // 생성된 Service 객체로부터 User를 받는다
        OAuth2User oAuth2User = oAuth2UserService.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId(); // github

        // SuccessHandler가 사용할 수 있도록 등록해준다
        OAuth2Attribute oAuth2Attribute =
                OAuth2Attribute.of(registrationId, oAuth2User.getAttributes());

        log.info(oAuth2Attribute.toString());
        log.info(oAuth2User.getAttributes().toString());

        MemberEntity requestMember = oAuth2Attribute.toMember();
        requestMember.setAccessCode(userRequest.getAccessToken().getTokenValue());
        memberService.registerMember(requestMember); // login(닉네임)로 멤버가 있는지 확인후 없으면 생성
        Optional<MemberEntity> findMember = memberService.findMemberByGithubId(requestMember.getGithubId());

        return new DefaultOAuth2User(Collections.singleton(
                new SimpleGrantedAuthority("ROLE_USER")),
                oAuth2Attribute.toMap(findMember.get().getMemberId()),
                "id");
    }
}
