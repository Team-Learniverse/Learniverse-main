package learniverse.learniversemain.service;

import jakarta.transaction.Transactional;
import learniverse.learniversemain.controller.Exception.CannotFindRoomException;
import learniverse.learniversemain.controller.Exception.CustomBadRequestException;
import learniverse.learniversemain.controller.Exception.CustomUnprocessableException;
import learniverse.learniversemain.dto.MemberDTO;
import learniverse.learniversemain.dto.MoonDTO;
import learniverse.learniversemain.dto.ResMoonDTO;
import learniverse.learniversemain.dto.RoomCardDTO;
import learniverse.learniversemain.entity.ID.RoomMemberID;
import learniverse.learniversemain.entity.MemberEntity;
import learniverse.learniversemain.entity.MoonEntity;
import learniverse.learniversemain.entity.RoomEntity;
import learniverse.learniversemain.entity.RoomMemberEntity;
import learniverse.learniversemain.repository.MemberRepository;
import learniverse.learniversemain.repository.MoonRepository;
import learniverse.learniversemain.repository.RoomMemberRepository;
import learniverse.learniversemain.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import learniverse.learniversemain.entity.MemberEntity;
import learniverse.learniversemain.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class MemberService {
    private final MoonRepository moonRepository;
    private final RoomMemberRepository roomMemberRepository;
    private final RoomService roomService;
    private final RoomRepository roomRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public void saveMoon(MoonDTO moonDTO) {
        //memberId 체크
        //있는지 확인
        MoonEntity moonEntity
                = moonRepository.findOneByMemberIdAndMoonDate(moonDTO.getMemberId(), moonDTO.getMoonDate());
        if (moonEntity == null) { // 새로 저장
            moonEntity = new MoonEntity(moonDTO);
        } else { // score + 1
            moonEntity.setMoonScore(moonEntity.getMoonScore() + 1);
            if (moonEntity.getMoonScore() > 4)
                throw new CustomBadRequestException("이미 moomScore가 4단계 입니다.");
        }

        moonRepository.save(moonEntity);
    }

    public void deleteMoon(MoonDTO moonDTO) {
        MoonEntity moonEntity
                = moonRepository.findOneByMemberIdAndMoonDate(moonDTO.getMemberId(), moonDTO.getMoonDate());
        if (moonEntity == null) throw new CustomBadRequestException("입력한 정보와 관련해 달이 저장된 기록이 없습니다.");

        moonEntity.setMoonScore(moonEntity.getMoonScore() - 1);
        if (moonEntity.getMoonScore() > 0)
            moonRepository.save(moonEntity);
        else
            moonRepository.delete(moonEntity);
    }

    public List<ResMoonDTO> getMoon(long memberId) {
        //memberId 체크
        //
        Map<LocalDate, Integer> resMoon = new HashMap<>();
        for (int i = 0; i < 30; i++) {
            resMoon.put(LocalDate.now().minusDays(i), 0);
        }
        List<MoonEntity> moonEntities = moonRepository.findByMemberIdAndMoonDateGreaterThan(memberId, LocalDate.now().minusDays(30));
        if (moonEntities != null) {
            for (MoonEntity moonEntity : moonEntities) {
                resMoon.replace(moonEntity.getMoonDate(), moonEntity.getMoonScore());
            }
        }

        List<ResMoonDTO> resMoonDTOS = new ArrayList<>();
        for (LocalDate date : resMoon.keySet()) {
            resMoonDTOS.add(new ResMoonDTO(date, resMoon.get(date)));
        }
        Collections.sort(resMoonDTOS, new Comparator<ResMoonDTO>() {
            @Override
            public int compare(ResMoonDTO o1, ResMoonDTO o2) {
                if (o2.getMoonDate().isAfter(o1.getMoonDate())) return 1;
                return -1;
            }
        });
        return resMoonDTOS;
    }

    public Map<String, String> getMember(long memberId) {
        Map<String, String> member = new HashMap<>();
        MemberEntity memberEntity = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomBadRequestException("해당 memberId와 매칭되는 정보를 찾을 수 없습니다."));

        member.put("nickname", memberEntity.getNickname());
        member.put("imageUrl", memberEntity.getImageUrl());
        return member;
    }

    public Map<String, List<RoomCardDTO>> getRooms(long memberId) {
        Map<String, List<RoomCardDTO>> data = new HashMap<>();
        List<RoomCardDTO> rooms = new ArrayList<>();
        List<RoomCardDTO> pinRooms = new ArrayList<>();

        List<RoomMemberEntity> roomMemberEntities = roomMemberRepository.findByMemberIdAndIsWaitAndIsPinOrderByJoinTimeDesc(memberId, false, false);
        List<RoomMemberEntity> roomMemberPinEntities = roomMemberRepository.findByMemberIdAndIsWaitAndIsPinOrderByPinTimeDesc(memberId, false, true);
        if (roomMemberEntities != null) {
            for (RoomMemberEntity roomMemberEntity : roomMemberEntities) {
                long roomId = roomMemberEntity.getRoomId();
                Optional<RoomEntity> roomEntity = roomRepository.findById(roomId);
                if (roomEntity.isEmpty()) throw new CannotFindRoomException();
                String isMember = roomService.getIsMember(roomId, memberId);
                List<String> hashtags = roomService.getHashtags2String(roomId);
                String roomCategory = roomService.getCategory(roomEntity.get().getRoomCategory());
                int roomCount = roomService.getRoomCount(roomId);
                rooms.add(new RoomCardDTO(roomEntity.get(), hashtags, roomCategory, isMember, roomCount));
            }
        }

        if (roomMemberPinEntities != null) {
            for (RoomMemberEntity roomMemberEntity : roomMemberPinEntities) {
                long roomId = roomMemberEntity.getRoomId();
                Optional<RoomEntity> roomEntity = roomRepository.findById(roomId);
                if (roomEntity.isEmpty()) throw new CannotFindRoomException();
                String isMember = roomService.getIsMember(roomId, memberId);
                List<String> hashtags = roomService.getHashtags2String(roomId);
                String roomCategory = roomService.getCategory(roomEntity.get().getRoomCategory());
                int roomCount = roomService.getRoomCount(roomId);
                pinRooms.add(new RoomCardDTO(roomEntity.get(), hashtags, roomCategory, isMember, roomCount));
            }
        }

        data.put("pinRooms", pinRooms);
        data.put("rooms", rooms);
        return data;
    }

    public List<RoomCardDTO> getRoomsIs(long memberId, boolean isLeader) {
        List<RoomCardDTO> rooms = new ArrayList<>();
        List<RoomMemberEntity> roomMemberEntities = roomMemberRepository.findByMemberId(memberId);
        if (roomMemberEntities != null)
            for (RoomMemberEntity roomMemberEntity : roomMemberEntities) {
                if (roomMemberEntity.isLeader() == isLeader) {
                    long roomId = roomMemberEntity.getRoomId();
                    Optional<RoomEntity> roomEntity = roomRepository.findById(roomId);
                    if (roomEntity.isEmpty()) throw new CannotFindRoomException();
                    String isMember = roomService.getIsMember(roomId, memberId);
                    List<String> hashtags = roomService.getHashtags2String(roomId);
                    String roomCategory = roomService.getCategory(roomEntity.get().getRoomCategory());
                    int roomCount = roomService.getRoomCount(roomId);
                    rooms.add(new RoomCardDTO(roomEntity.get(), hashtags, roomCategory, isMember, roomCount));
                }
            }

        return rooms;
    }

    @Transactional
    public boolean updatePin(RoomMemberID roomMemberID) {
        Optional<RoomMemberEntity> roomMemberEntity = roomMemberRepository.findById(roomMemberID);
        if (roomMemberEntity.isEmpty())
            throw new CustomBadRequestException("해당 roomId, memberId 조합에 대한 결과가 존재하지 않습니다.");
        if (roomMemberEntity.get().isWait()) throw new CustomBadRequestException("대기중인 스터디룸은 고정핀을 사용할 수 없습니다.");
//        if (!roomMemberEntity.get().isPin()){
//            long cnt = roomMemberRepository.countByMemberIdAndIsPin(roomMemberID.getMemberId(), true);
//            if(cnt == 5) throw new CustomUnprocessableException("핀 설정은 최대 5개까지 가능합니다.");
//        }
        roomMemberEntity.get().changePin();
        roomMemberEntity.get().setPinTime(LocalDateTime.now());
        roomMemberRepository.save(roomMemberEntity.get());
        return roomMemberEntity.get().isPin();
    }

    @Transactional
    public void registerMember(MemberEntity member) {
        if (!memberRepository.existsByGithubId(member.getGithubId())) {
            memberRepository.save(member);
        }
    }

    @Transactional
    public Optional<MemberEntity> findMemberByGithubId(String githubId) {
        return memberRepository.getByGithubId(githubId);
    }

    //public MemberDTO updateMember(MemberDTO updateMember);

    // public Optional<MemberEntity> findMemberByEmail(String email){
    //     return memberRepository.getByMemberEmail(email);
    //}

    //MemberDTO getMemberById(Long id);


    //List<MemberDTO> searchByKeyword(String keyword, int size);


}
