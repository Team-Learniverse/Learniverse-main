package learniverse.learniversemain.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import learniverse.learniversemain.controller.Exception.CannotFindRoomException;
import learniverse.learniversemain.controller.Exception.CustomBadRequestException;
import learniverse.learniversemain.dto.RoomCardDTO;
import learniverse.learniversemain.dto.RoomDTO;
import learniverse.learniversemain.dto.RoomSettingDTO;
import learniverse.learniversemain.entity.*;
import learniverse.learniversemain.entity.ID.RoomMemberID;
import learniverse.learniversemain.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoomService {
    @PersistenceContext
    private EntityManager entityManager;

    private final RoomRepository roomRepository;
    private final RoomMemberRepository roomMemberRepository;
    private final HashtagRepository hashtagRepository;
    private final MemberRepository memberRepository;
    private final MemberStatusRepository memberStatusRepository;
    private final RoomSettingRepository roomSettingRepository;

    public List<RoomSettingDTO> getSetting(String type){
        List<RoomSettingEntity> roomSettingEntities = roomSettingRepository.findByType(type);
        List<RoomSettingDTO> roomSettingDTOS = new ArrayList<>();
        for(int i=0;i<roomSettingEntities.size();i++){
            roomSettingDTOS.add(new RoomSettingDTO(i, roomSettingEntities.get(i).getName()));
        }
        return roomSettingDTOS;
    }

    @Transactional
    public long createRoom(RoomDTO roomDTO){
        boolean existMember = memberRepository.existsByMemberId(roomDTO.getMemberId());
        if(!existMember) throw new CustomBadRequestException("해당 멤버Id와 매칭되는 멤버를 찾을 수 없습니다.");
        //방 생성
        RoomEntity roomEntity = RoomEntity.toRoomEntity(roomDTO);
        roomRepository.save(roomEntity);
        //해시태그 처리
        saveHashtag(roomEntity.getRoomId(), roomDTO.getRoomHashtags());
        //방장 처리
        RoomMemberEntity roomMemberEntity
                = new RoomMemberEntity(roomEntity.getRoomId(), roomDTO.getMemberId(), true);
        roomMemberRepository.save(roomMemberEntity);
        return roomEntity.getRoomId();
    }

    @Transactional
    public void updateRoom(RoomDTO roomDTO){
        long roomId = roomDTO.getRoomId();
        RoomEntity oldRoom = roomRepository.findById(roomId)
                .orElseThrow(()-> new CannotFindRoomException());
        RoomEntity newRoom = RoomDTO.toRoomEntity(roomDTO);
        oldRoom.update(newRoom);

        //해시태그 아이디
        List<HashtagEntity> hashtagList = hashtagRepository.findByRoomId(roomId);
        if(hashtagList != null){
            for(String hashtag : roomDTO.getRoomHashtags()){
                HashtagEntity tempHashtagEntity = new HashtagEntity(roomId, hashtag);
                if(!hashtagList.contains(tempHashtagEntity)) {
                    hashtagRepository.save(tempHashtagEntity);
                }else {
                    hashtagList.remove(tempHashtagEntity);
                }
            }
        }

        if(hashtagList != null) {
            for (HashtagEntity hashtagEntity : hashtagList){
                hashtagRepository.delete(hashtagEntity);
            }
        }
    }

    public RoomDTO getRoomModifyInfo(Long roomId){
        RoomEntity findRoom = roomRepository.findById(roomId)
                .orElseThrow(()-> new CannotFindRoomException());
        RoomEntity roomEntity = roomRepository.findById(roomId).orElseThrow(() -> new RuntimeException());
        RoomDTO roomDTO = roomEntity.toRoomDTO();
        List<String> hashtags2String = getHashtags2String(roomId);
        String[] hashtags = new String[hashtags2String.size()];
        for(int i=0;i<hashtags.length;i++){
            hashtags[i] = hashtags2String.get(i);
        }
        roomDTO.setRoomHashtags(hashtags);
        return roomDTO;
    }

    public boolean saveHashtags(long roomId, List<String> hashtags){
        //roomId 확인
        boolean existRoom = hashtagRepository.existsByRoomId(roomId);
        if(!existRoom) throw new CannotFindRoomException();

        for (String hashtag : hashtags){
            HashtagEntity hashtagEntity = new HashtagEntity(roomId, hashtag);
            hashtagRepository.save(hashtagEntity);
        }
        return true;
    }

    public void saveHashtag(long room_id, String[] hashtags){
        for(String hashtag : hashtags){
            HashtagEntity hashtagEntity = new HashtagEntity(room_id, hashtag);
            hashtagRepository.save(hashtagEntity);
        }
    }

    @Transactional
    public boolean deleteHashtags(Long[] hashtagIds) {
        List<HashtagEntity> hashtagEntityList = new ArrayList<>();
        //해시태그 존재여부 확인
        for (Long hashtagId : hashtagIds){
            Optional<HashtagEntity> hashtagEntity = hashtagRepository.findById(hashtagId);
            if(hashtagEntity.isEmpty()) {return false;}
            hashtagEntityList.add(hashtagEntity.get());
        }
        //삭제
        for (HashtagEntity hashtagEntity : hashtagEntityList){
            hashtagRepository.delete(hashtagEntity);
        }
        return true;
    }

    @Transactional
    public List<HashtagEntity> getHashtags(long roomId){
        return hashtagRepository.findByRoomId(roomId);
    }


    public List<String> getHashtags2String(long roomId){
        List<String> res = new ArrayList<>();
        List<HashtagEntity>  hashtagEntityList = hashtagRepository.findByRoomId(roomId);
        for(HashtagEntity hashtagEntity : hashtagEntityList){
            res.add(hashtagEntity.getHashtag());
        }
        return res;
    }

    public void deleteHashtag(long hashtagId){
        hashtagRepository.deleteById(hashtagId);
    }

    public String getRoomEncoding (long roomId) throws NoSuchAlgorithmException,
            GeneralSecurityException, UnsupportedEncodingException {
        //roomId 확인
        boolean existRoom = roomRepository.existsByRoomId(roomId);
        if(!existRoom) throw new CannotFindRoomException();

        AES256Util aes = new AES256Util();
        return aes.encrypt(String.valueOf(roomId));
    }
    
    public long getRoomDecoding (String encoded) throws UnsupportedEncodingException, GeneralSecurityException {
        AES256Util aes = new AES256Util();
        String roomStr = aes.decrypt(encoded);
        long roomId = Integer.parseInt(roomStr);
        //roomId 확인
        boolean existRoom = hashtagRepository.existsByRoomId(roomId);
        if(!existRoom) throw new CannotFindRoomException();
        return roomId;
    }

    public Page<RoomEntity> getSearch (String str, int page){
        //List<RoomDTO> result = new ArrayList<>();
        PageRequest pageRequest = PageRequest.of(page,10);
        Page<RoomEntity> roomList1 = roomRepository.findByRoomNameContainingOrRoomIntroContaining(str, str, pageRequest);
        //List<HashtagEntity> roomList3 = hashtagRepository.findByHashtagContaining(str);
        return roomList1;
    }

    public String getCategory(int settingId){
        RoomSettingEntity roomCategoryEntity = roomSettingRepository.findFirstByTypeOrderBySettingIdAsc("category");
        System.out.println(settingId + roomCategoryEntity.getSettingId());
        RoomSettingEntity roomSettingEntity = roomSettingRepository.findById(settingId + roomCategoryEntity.getSettingId())
                .orElseThrow(() -> new CustomBadRequestException("settingId 오류"));

        return roomSettingEntity.getName();
    }

    public String getIsMember(long roomId, long memberId){
        RoomMemberID roomMemberID = new RoomMemberID(roomId, memberId);
        Optional<RoomMemberEntity> roomMemberEntity = roomMemberRepository.findById(roomMemberID);
        if(roomMemberEntity.isEmpty()) return null;

        if(roomMemberEntity.get().isLeader()) return ("팀장");
        else if(roomMemberEntity.get().isReject()) return ("거절");
        else if(!roomMemberEntity.get().isWait()) return("승인");
        else return ("대기");
    }

    public int getRoomCount(long roomId){
        List<RoomMemberEntity> roomMemberEntities = roomMemberRepository.findByRoomIdAndIsWait(roomId, false);
        if(roomMemberEntities != null) return roomMemberEntities.size();
        else return 0;
    }

    public RoomCardDTO getRoom(long roomId, long memberId){
        RoomEntity roomEntity = roomRepository.findById(roomId)
                .orElseThrow(()->new CannotFindRoomException());
        String isMember = getIsMember(roomId, memberId);
        List<String> hashtags = getHashtags2String(roomId);
        String roomCategory = getCategory(roomEntity.getRoomCategory());
        int roomCount = getRoomCount(roomId);

        return new RoomCardDTO(roomEntity, hashtags, roomCategory, isMember, roomCount);
    }

    public List<RoomCardDTO> getRooms(long memberId) {
        List<RoomCardDTO> resRooms = new ArrayList<>();
        List<RoomEntity> roomEntities = roomRepository.findAll(Sort.by(Sort.Direction.DESC, "roomId"));
        for(RoomEntity roomEntity : roomEntities){
            long roomId = roomEntity.getRoomId();
            String isMember = getIsMember(roomId, memberId);
            List<String> hashtags = getHashtags2String(roomId);
            String roomCategory = getCategory(roomEntity.getRoomCategory());
            int roomCount = getRoomCount(roomId);
            resRooms.add(new RoomCardDTO(roomEntity, hashtags, roomCategory, isMember, roomCount));
        }
        return resRooms;
    }

    @Transactional
    public void deleteRoom(long roomId){
        Optional<RoomEntity> roomEntity = roomRepository.findById(roomId);
        if(roomEntity.isEmpty()) throw new CannotFindRoomException();
        else roomRepository.delete(roomEntity.get());
        //해시태그 삭제
        List<HashtagEntity> hashtagEntities = hashtagRepository.findByRoomId(roomId);
        for (HashtagEntity hashtagEntity : hashtagEntities){
            hashtagRepository.delete(hashtagEntity);
        }
        //관련 멤버 삭제
        List<RoomMemberEntity> roomMemberEntities = roomMemberRepository.findByRoomId(roomId);
        for (RoomMemberEntity roomMemberEntity : roomMemberEntities){
            roomMemberRepository.delete(roomMemberEntity);
        }
    }
}
