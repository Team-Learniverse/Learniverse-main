package learniverse.learniversemain.service;

import jakarta.transaction.Transactional;
import learniverse.learniversemain.controller.Exception.CustomBadRequestException;
import learniverse.learniversemain.dto.MoonDTO;
import learniverse.learniversemain.dto.ResMoonDTO;
import learniverse.learniversemain.entity.ID.RoomMemberID;
import learniverse.learniversemain.entity.MoonEntity;
import learniverse.learniversemain.entity.RoomMemberEntity;
import learniverse.learniversemain.repository.MoonRepository;
import learniverse.learniversemain.repository.RoomMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MoonRepository moonRepository;
    private final RoomMemberRepository roomMemberRepository;

    public void saveMoon(MoonDTO moonDTO){
        //memberId 체크
        //있는지 확인
        MoonEntity moonEntity
                = moonRepository.findOneByMemberIdAndMoonDate(moonDTO.getMemberId(), moonDTO.getMoonDate());
        if(moonEntity == null) { // 새로 저장
            moonEntity = new MoonEntity(moonDTO);
        }else { // score + 1
            moonEntity.setMoonScore(moonEntity.getMoonScore() + 1);
            if(moonEntity.getMoonScore() > 4)
                throw new CustomBadRequestException("이미 moomScore가 4단계 입니다.");
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
        //
        Map<LocalDate, Integer> resMoon = new HashMap<>();
        for(int i=0;i<30;i++){
            resMoon.put(LocalDate.now().minusDays(i), 0);
        }
        List<MoonEntity> moonEntities = moonRepository.findByMemberIdAndMoonDateGreaterThan(memberId, LocalDate.now().minusDays(30));
        if(moonEntities != null) {
            for(MoonEntity moonEntity : moonEntities){
                resMoon.replace(moonEntity.getMoonDate(), moonEntity.getMoonScore());
            }
        }

        List<ResMoonDTO> resMoonDTOS = new ArrayList<>();
        for(LocalDate date : resMoon.keySet()){
            resMoonDTOS.add(new ResMoonDTO(date, resMoon.get(date)));
        }
        return resMoonDTOS;
    }

    public Map<String,List<Long>> getRooms(long memberId){
        Map<String,List<Long>> data = new HashMap<>();
        List<Long> rooms = new ArrayList<>();
        List<Long> pinRooms = new ArrayList<>();
        List<RoomMemberEntity> roomMemberEntities = roomMemberRepository.findByMemberId(memberId);
        if(roomMemberEntities != null){
            for(RoomMemberEntity roomMemberEntity : roomMemberEntities){
                if(roomMemberEntity.isPin() == true)
                    pinRooms.add(roomMemberEntity.getRoomId());
                else rooms.add(roomMemberEntity.getRoomId());
            }
        }
        data.put("pinRooms", pinRooms);
        data.put("rooms", rooms);
        return data;
    }

    @Transactional
    public boolean updatePin(RoomMemberID roomMemberID){
        Optional<RoomMemberEntity> roomMemberEntity = roomMemberRepository.findById(roomMemberID);
        if(roomMemberEntity.isEmpty()) throw new CustomBadRequestException("해당 roomId, memberId 조합에 대한 결과가 존재하지 않습니다.");
        if(roomMemberEntity.get().isWait()) throw new CustomBadRequestException("대기중인 스터디룸은 고정핀을 사용할 수 없습니다.");
        roomMemberEntity.get().changePin();
        roomMemberRepository.save(roomMemberEntity.get());
        return roomMemberEntity.get().isPin();
    }
}
