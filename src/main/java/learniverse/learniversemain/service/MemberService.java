package learniverse.learniversemain.service;

import jakarta.transaction.Transactional;
import learniverse.learniversemain.controller.Exception.CannotFindRoomException;
import learniverse.learniversemain.controller.Exception.CustomBadRequestException;
import learniverse.learniversemain.controller.Exception.CustomUnprocessableException;
import learniverse.learniversemain.dto.*;
import learniverse.learniversemain.entity.*;
import learniverse.learniversemain.entity.ID.RoomMemberID;
import learniverse.learniversemain.entity.mongoDB.JoinsEntity;
import learniverse.learniversemain.entity.mongoDB.MembersEntity;
import learniverse.learniversemain.jwt.Refresh;
import learniverse.learniversemain.repository.*;
import learniverse.learniversemain.repository.mongoDB.JoinsMongoDBRepository;
import learniverse.learniversemain.repository.mongoDB.MembersMongoDBRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import learniverse.learniversemain.entity.MemberEntity;
import learniverse.learniversemain.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;


@Service
@RequiredArgsConstructor
public class MemberService {
    private final MoonRepository moonRepository;
    private final RoomMemberRepository roomMemberRepository;
    private final RoomService roomService;
    private final RoomRepository roomRepository;
    private final MemberRepository memberRepository;
    private final JoinsMongoDBRepository joinsMongoDBRepository;
    private final MembersMongoDBRepository membersMongoDBRepository;
    private  final RefreshTokenRepository refreshTokenRepository;

    @Transactional
    public void login(long memberId){
        MembersEntity membersEntity = membersMongoDBRepository.findByMemberId(memberId);
        if(membersEntity == null) throw new CustomBadRequestException("memberId 정보가 mongoDB에 존재하지 않습니다");
        membersEntity.setLastLoginDate(LocalDate.now());
        membersMongoDBRepository.save(membersEntity);

        //달 추가
        LocalDateTime localDateTime = LocalDateTime.now().plusHours(9);
        LocalDate localDate = localDateTime.toLocalDate();
        MoonEntity moonEntity
                = moonRepository.findOneByMemberIdAndMoonDate(memberId, localDate);
        if (moonEntity == null) { // 새로 저장
            moonEntity = new MoonEntity(new MoonDTO(memberId, localDate));
            moonRepository.save(moonEntity);
        }
    }
    public void updateProfile(ProfileDTO profileDTO){
        MemberEntity memberEntity = memberRepository.findById(profileDTO.getMemberId())
                .orElseThrow( () -> new CustomBadRequestException("존재하지 않는 memberId 입니다."));

        if(profileDTO.getNickname() != null){
            memberEntity.setNickname(profileDTO.getNickname());
        }

        if(profileDTO.getMemberMessage() != null){
            memberEntity.setMemberMessage(profileDTO.getMemberMessage());
        }
        memberRepository.save(memberEntity);
    }

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
                throw new CustomUnprocessableException("이미 moomScore가 4단계 입니다.");
        }

        moonRepository.save(moonEntity);
    }

    @Transactional
    public void saveCoreMoon(MoonDTO moonDTO){
        //있는지 확인
        MoonEntity moonEntity
                = moonRepository.findOneByMemberIdAndMoonDate(moonDTO.getMemberId(), moonDTO.getMoonDate());
        if(moonEntity == null) throw new CustomBadRequestException("해당 memberId와 date 조합이 없습니다.");
        else if(moonEntity.isCore()) throw new CustomUnprocessableException("코어타임 접속에 대한 달 추가가 이미 처리되었습니다.");
        else{
            moonEntity.setCore(true);
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
        member.put("memberMessage", memberEntity.getMemberMessage());
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
        List<JoinsEntity> joinsEntity = joinsMongoDBRepository.findByMemberIdAndRoomId(roomMemberID.getMemberId(),roomMemberID.getRoomId());
        joinsEntity.get(0).setPinDate(LocalDate.now());
        joinsMongoDBRepository.save(joinsEntity.get(0));
        return roomMemberEntity.get().isPin();
    }

    @Transactional
    public void registerMember(MemberEntity member) {
        //깃허브 아이디로 멤버 찾기
        Optional<MemberEntity> existingMember = memberRepository.getByGithubId(member.getGithubId());

        if (existingMember.isPresent()) {
            MemberEntity savedMember = existingMember.get();
            if (member.getMemberEmail() != null)savedMember.setMemberEmail(member.getMemberEmail());
            if (member.getImageUrl() != null)savedMember.setImageUrl(member.getImageUrl());
            if (member.getAccessCode() != null)savedMember.setAccessCode((member.getAccessCode()));

            savedMember.setMemberFirst(false);
            memberRepository.save(savedMember); //여기서 체크

        }
        else {
            member.setMemberFirst(true);
            member.setAccessCode(member.getAccessCode());
            memberRepository.save(member);
        }
    }

    @Transactional
    public Optional<MemberEntity> findMemberByGithubId(String githubId) {
        return memberRepository.getByGithubId(githubId);
    }

    /*@Transactional
    public Optional<MemberEntity> getMemberById(long memberId){
        Optional<MemberEntity> memberEntity = memberRepository.findById(memberId);

        return memberEntity;
    }*/

    public String isMemberFirst(long memberId) {
        MemberEntity memberEntity = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomBadRequestException("해당 memberId와 매칭되는 정보를 찾을 수 없습니다."));

        String memberFirst = String.valueOf(memberEntity.getMemberFirst());

        return memberFirst;
    }

    public String getRefreshToken(long memberId){
        Refresh refreshToken = refreshTokenRepository.findByMemberId(memberId);
        String token = refreshToken.getToken();

        return token;
    }

    /* public boolean isCore(Long roomId) {
        boolean existRoom = roomRepository.existsByRoomId(roomId);
        if (!existRoom) throw new CannotFindRoomException();

        LocalDateTime now = LocalDateTime.now().plusHours(9);
        CoreTimeEntity coreTimeEntity = coreTimeRepository.findOneByRoomIdAndCoreStartTimeLessThanEqualAndCoreEndTimeGreaterThan(roomId, now, now);
        if (coreTimeEntity == null) return false;
        else return true;
    }*/

    //public MemberDTO updateMember(MemberDTO updateMember);

    // public Optional<MemberEntity> findMemberByEmail(String email){
    //     return memberRepository.getByMemberEmail(email);
    //}

    //MemberDTO getMemberById(Long id);


    //List<MemberDTO> searchByKeyword(String keyword, int size);


}
