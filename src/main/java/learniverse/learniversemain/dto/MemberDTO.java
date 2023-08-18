package learniverse.learniversemain.dto;

import learniverse.learniversemain.entity.MemberEntity;
import learniverse.learniversemain.entity.MemberStatusEntity;
import lombok.Data;

import java.util.Optional;

@Data
public class MemberDTO {
    private long memberId;
    private String memberEmail;
    private String nickname;
    private String memberMessage;
    private int status;

    public static MemberDTO toMemberDTO(MemberEntity memberEntity, MemberStatusEntity memberStatusEntity) {
        MemberDTO memberDTO = new MemberDTO();
        memberDTO.setMemberId(memberEntity.getMemberId());
        memberDTO.setMemberEmail(memberEntity.getMemberEmail());
        memberDTO.setNickname(memberEntity.getNickname());
        memberDTO.setMemberMessage(memberEntity.getMemberMessage());
        memberDTO.setStatus(memberStatusEntity.getMemberStatus());

        return memberDTO;
    }
}
