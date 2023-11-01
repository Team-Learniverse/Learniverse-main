package learniverse.learniversemain.dto;

import learniverse.learniversemain.entity.MemberEntity;
import learniverse.learniversemain.entity.MemberStatusEntity;
import learniverse.learniversemain.entity.RoomMemberEntity;
import lombok.Data;

@Data
public class MemberDTO {
    private Long memberId;
    private String memberEmail;
    private String githubId;
    private String nickname;
    private String memberMessage;
    private String imageUrl;
    //private boolean status;
    private String isMember; // "팀장" "대기" "승인" "거절"

    public static MemberDTO toMemberDTO(MemberEntity memberEntity, RoomMemberEntity roomMemberEntity) {
        MemberDTO memberDTO = new MemberDTO();
        memberDTO.setMemberId(memberEntity.getMemberId());
        memberDTO.setGithubId(memberEntity.getGithubId());
        memberDTO.setMemberEmail(memberEntity.getMemberEmail());
        memberDTO.setNickname(memberEntity.getNickname());
        memberDTO.setMemberMessage(memberEntity.getMemberMessage());
        memberDTO.setImageUrl(memberEntity.getImageUrl());
       //memberDTO.setStatus(memberStatusEntity.getMemberStatus());
        if (roomMemberEntity.isLeader()) memberDTO.setIsMember("팀장");
        else if (roomMemberEntity.isReject()) memberDTO.setIsMember("거절");
        else if (!roomMemberEntity.isWait()) memberDTO.setIsMember("승인");
        else memberDTO.setIsMember("대기");
        return memberDTO;
    }
}
