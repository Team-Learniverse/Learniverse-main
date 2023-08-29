package learniverse.learniversemain.service;

import learniverse.learniversemain.controller.Exception.CustomBadRequestException;
import learniverse.learniversemain.dto.MoonDTO;
import learniverse.learniversemain.dto.ResMoonDTO;
import learniverse.learniversemain.entity.MoonEntity;
import learniverse.learniversemain.repository.MoonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MoonRepository moonRepository;


    public void saveMoon(MoonDTO moonDTO){
        //memberId 체크
        //있는지 확인
        MoonEntity moonEntity
                = moonRepository.findOneByMemberIdAndMoonDate(moonDTO.getMemberId(), moonDTO.getMoonDate());
        if(moonEntity == null) { // 새로 저장
            moonEntity = new MoonEntity(moonDTO);
        }else { // score + 1
            moonEntity.setMoonScore(moonEntity.getMoonScore() + 1);
            if(moonEntity.getMoonScore() > 5)
                throw new CustomBadRequestException("이미 moomScore가 5단계 입니다.");
        }

        moonRepository.save(moonEntity);
    }

    public void deleteMoon(MoonDTO moonDTO){
        MoonEntity moonEntity
                = moonRepository.findOneByMemberIdAndMoonDate(moonDTO.getMemberId(), moonDTO.getMoonDate());
        if(moonEntity == null) throw new CustomBadRequestException("입력한 정보와 관련해 달이 저장된 기록이 없습니다.");

        moonEntity.setMoonScore(moonEntity.getMoonScore() - 1);
        if(moonEntity.getMoonScore() > 0)
            moonRepository.save(moonEntity);
        else
            moonRepository.delete(moonEntity);
    }

    public List<ResMoonDTO> getMoon(long memberId){
        //memberId 체크
        List<ResMoonDTO> resMoon = new ArrayList<>();
        List<MoonEntity> moonEntities = moonRepository.findByMemberIdAndMoonDateGreaterThan(memberId, LocalDate.now().minusDays(30));
        if(moonEntities != null) {
            for(MoonEntity moonEntity : moonEntities){
                ResMoonDTO resMoonDTO = new ResMoonDTO(moonEntity);
                resMoon.add(resMoonDTO);
            }
        }

        return resMoon;
    }

}
