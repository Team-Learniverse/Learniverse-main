package learniverse.learniversemain.service;

import learniverse.learniversemain.entity.MemberEntity;
import learniverse.learniversemain.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    public void registerMember(MemberEntity member){
        if(memberRepository.existsByMemberEmail(member.getMemberEmail())){
            return;
        }
        memberRepository.save(member);
    }
    public Optional<MemberEntity> findMemberByEmail(String email){
        return memberRepository.getByMemberEmail(email);
    }

    //MemberDTO getMemberById(Long id);
    //MemberDTO updateMember(MemberDTO updateMember)

    //Optional<MemberEntity> findById(Long id);

    //List<MemberDTO> searchByKeyword(String keyword, int size);


}
