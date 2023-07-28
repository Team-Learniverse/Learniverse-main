package learniverse.learniversemain.dto;

import learniverse.learniversemain.entity.MemberEntity;
import learniverse.learniversemain.entity.MemberStatusEntity;
import lombok.Data;

import java.util.Optional;

@Data
public class MemberDTO {
    private long member_id;
    private String member_email;
    private String nickname;
    private String member_message;
    private int status;

    public static MemberDTO toMemberDTO(MemberEntity memberEntity, MemberStatusEntity memberStatusEntity) {
        MemberDTO memberDTO = new MemberDTO();
        memberDTO.setMember_id(memberEntity.getMember_id());
        memberDTO.setMember_email(memberEntity.getMember_email());
        memberDTO.setNickname(memberEntity.getNickname());
        memberDTO.setMember_message(memberEntity.getMember_message());
        memberDTO.setStatus(memberStatusEntity.getMemberStatus());

        return memberDTO;
    }
}
