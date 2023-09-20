package learniverse.learniversemain.oauth;

import learniverse.learniversemain.entity.MemberEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Builder
@ToString
public class OAuth2Attribute {
    private Map<String, Object> attributes;
    private String attributeKey;
    private String email;
    private String name;

    private Integer id;

    public static OAuth2Attribute of(String provider,
                                     Map<String, Object> attributes) {
        switch (provider) {
            case "github":
                return ofGithub(attributes);
            default:
                throw new RuntimeException();
        }
    }

    public static OAuth2Attribute ofGithub(Map<String, Object> attributes) {
        for (String s : attributes.keySet()) {
            log.info("{}={}", s, attributes.get(s));
        }
        return OAuth2Attribute.builder()
                .id((Integer) attributes.get("id"))
                .name((String) attributes.get("login"))
                .email((String) attributes.get("email"))
                .attributes(attributes)
                .attributeKey("id")
                .build();
    }

    public MemberEntity toMember(){
        return new MemberEntity(name,email);
    }

    public Map<String, Object> toMap(long id) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("key", attributeKey);
        map.put("name", name);
        map.put("email", email);

        return map;
    }
}