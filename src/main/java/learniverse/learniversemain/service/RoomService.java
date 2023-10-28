package learniverse.learniversemain.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import learniverse.learniversemain.controller.Exception.CannotFindRoomException;
import learniverse.learniversemain.controller.Exception.CustomBadRequestException;
import learniverse.learniversemain.controller.Exception.CustomUnprocessableException;
import learniverse.learniversemain.dto.RoomCardDTO;
import learniverse.learniversemain.dto.RoomDTO;
import learniverse.learniversemain.dto.mongoDB.DefaultRoomsDTO;
import learniverse.learniversemain.entity.*;
import learniverse.learniversemain.entity.ID.RoomMemberID;
import learniverse.learniversemain.entity.mongoDB.HistoryEntity;
import learniverse.learniversemain.entity.mongoDB.JoinsEntity;
import learniverse.learniversemain.entity.mongoDB.RoomsEntity;
import learniverse.learniversemain.repository.*;
import learniverse.learniversemain.repository.mongoDB.DefaultMongoDBRepository;
import learniverse.learniversemain.repository.mongoDB.HistoryMongoDBRepository;
import learniverse.learniversemain.repository.mongoDB.JoinsMongoDBRepository;
import learniverse.learniversemain.repository.mongoDB.RoomsMongoDBRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
public class RoomService {
    @PersistenceContext
    private EntityManager entityManager;

    private final RoomRepository roomRepository;
    private final RoomMemberRepository roomMemberRepository;
    private final HashtagRepository hashtagRepository;
    private final MemberRepository memberRepository;
    //private final MemberStatusRepository memberStatusRepository;
    private final RoomSettingRepository roomSettingRepository;
    private final JoinsMongoDBRepository joinsMongoDBRepository;
    private final DefaultMongoDBRepository defaultMongoDBRepository;
    private final HistoryMongoDBRepository historyRepository;
    private final RoomsMongoDBRepository roomMongoRepository;

    public List<String> getSetting(String type){
        List<RoomSettingEntity> roomSettingEntities = roomSettingRepository.findByType(type);
        List<String> strings = new ArrayList<>();
        for (RoomSettingEntity roomSettingEntity : roomSettingEntities){
            strings.add(roomSettingEntity.getName());
        }
        return strings;
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
        roomMongoRepository.save(new RoomsEntity(roomEntity, getCategory(roomEntity.getRoomCategory()), roomDTO.getRoomHashtags()));
        joinsMongoDBRepository.save(new JoinsEntity(roomDTO.getMemberId(), roomEntity.getRoomId(), false));
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
        RoomEntity roomEntity = roomRepository.findById(roomId).orElseThrow(() -> new CannotFindRoomException());
        RoomDTO roomDTO = roomEntity.toRoomDTO();
        List<String> hashtags2String = getHashtags2String(roomId);
        String[] hashtags = new String[hashtags2String.size()];
        for(int i=0;i<hashtags.length;i++){
            hashtags[i] = hashtags2String.get(i);
        }
        roomDTO.setRoomHashtags(hashtags);
        return roomDTO;
    }

    public RoomEntity getRoomInfo(Long roomId){
        RoomEntity roomEntity = roomRepository.findById(roomId)
                .orElseThrow(()-> new CannotFindRoomException());

        return roomEntity;
    }

    public void saveDefaultJoins(Long memberId, Long[] roomIds, boolean isDefault){
        for(long roomId : roomIds){
            JoinsEntity joinsEntity = new JoinsEntity(memberId, roomId, isDefault);
            joinsMongoDBRepository.save(joinsEntity);
        }
    }

    public List<RoomCardDTO> getDefaultRooms(){
        List<DefaultRoomsDTO> defaultRoomsDTOS = defaultMongoDBRepository.findAll();
        List<RoomCardDTO> rooms = new ArrayList<>();
        for(DefaultRoomsDTO defaultRoomsDTO : defaultRoomsDTOS){
            rooms.add(new RoomCardDTO(defaultRoomsDTO));
        }
        return rooms;

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
        if(hashtagEntityList.size() == 0) return res;

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

    public List<RoomCardDTO> getRoomsInSearch(long memberId, int page) {
        List<RoomCardDTO> result = new ArrayList<>();
        Pageable pageable = PageRequest.of(page, 15, Sort.by(Sort.Direction.DESC, "roomId"));
        Page<RoomEntity> roomEntities = roomRepository.findAll(pageable);

        for(RoomEntity roomEntity : roomEntities){
            long roomId = roomEntity.getRoomId();
            String isMember = getIsMember(roomId, memberId);
            List<String> hashtags = getHashtags2String(roomId);
            String roomCategory = getCategory(roomEntity.getRoomCategory());
            int roomCount = getRoomCount(roomId);
            result.add(new RoomCardDTO(roomEntity, hashtags, roomCategory, isMember, roomCount));
        }
        return result;
    }

    public List<RoomCardDTO> getSearchHashtag (String str, long memberId, int page){
        List<RoomCardDTO> result = new ArrayList<>();
        if(str==""){
            result = getRoomsInSearch(memberId, page);
        }
        else{
            Pageable pageable = PageRequest.of(page, 15, Sort.by(Sort.Direction.DESC, "roomId"));
            Page<RoomEntity> roomList = roomRepository.findByHashtagsHashtagContaining(str, pageable);
            //Page<HashtagEntity> roomList = hashtagRepository.findByHashtagContaining(str,pageable);
            //List<HashtagEntity> roomList = hashtagRepository.findByHashtagContaining(str);
            if(page == 0) historyRepository.save(new HistoryEntity(memberId, str, LocalDate.now()));
            for (RoomEntity roomEntity : roomList){
                long roomId = roomEntity.getRoomId();
                //RoomEntity roomEntity = roomRepository.findById(roomId).orElseThrow(()-> new CannotFindRoomException());
                String isMember = getIsMember(roomId, memberId);
                List<String> hashtags = getHashtags2String(roomId);
                String roomCategory = getCategory(roomEntity.getRoomCategory());
                int roomCount = getRoomCount(roomId);
                result.add(new RoomCardDTO(roomEntity, hashtags, roomCategory, isMember, roomCount));
            }
        }


        return result;
    }

    public List<RoomCardDTO> getSearch (String str, long memberId, int page){
        List<RoomCardDTO> result = new ArrayList<>();
        Pageable pageable = PageRequest.of(page, 15, Sort.by(Sort.Direction.DESC, "roomId"));
        Page<RoomEntity> roomList = roomRepository.findByRoomNameContainingOrRoomIntroContaining(str, str, pageable);
        for (RoomEntity roomEntity : roomList){
            long roomId = roomEntity.getRoomId();
            String isMember = getIsMember(roomId, memberId);
            List<String> hashtags = getHashtags2String(roomId);
            String roomCategory = getCategory(roomEntity.getRoomCategory());
            int roomCount = getRoomCount(roomId);
            result.add(new RoomCardDTO(roomEntity, hashtags, roomCategory, isMember, roomCount));
        }
        return result;
    }

    public List<RoomCardDTO> getSearchbyCategory (String str, long memberId, int category, int page){
        List<RoomCardDTO> result = new ArrayList<>();
        Pageable pageable = PageRequest.of(page, 15, Sort.by(Sort.Direction.DESC, "roomId"));
        //Page<RoomEntity> roomList = roomRepository.findByRoomCategoryAndRoomNameContainingOrRoomIntroContaining(category, str, str, pageable);
        Page<RoomEntity> roomList = roomRepository.findByRoomCategoryAndRoomNameContainingOrRoomCategoryAndRoomIntroContaining(
                category, str, category, str, pageable);
        for (RoomEntity roomEntity : roomList){
            long roomId = roomEntity.getRoomId();
            String isMember = getIsMember(roomId, memberId);
            List<String> hashtags = getHashtags2String(roomId);
            String roomCategory = getCategory(roomEntity.getRoomCategory());
            int roomCount = getRoomCount(roomId);
            result.add(new RoomCardDTO(roomEntity, hashtags, roomCategory, isMember, roomCount));
        }
        return result;
    }

    public String getCategory(int settingId){
        RoomSettingEntity roomCategoryEntity = roomSettingRepository.findFirstByTypeOrderBySettingIdAsc("category");
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

    public List<RoomEntity> getPageRooms(int page, int num){
        List<RoomEntity> roomEntities = new ArrayList<>();
        for(int i = 0; i<num;i++){
            Pageable pageable = PageRequest.of(page+i, 3, Sort.by(Sort.Direction.DESC, "roomId"));
            Page<RoomEntity> roomPageEntities = roomRepository.findAll(pageable);
            for(RoomEntity roomPageEntity : roomPageEntities)
                roomEntities.add(roomPageEntity);
        }
        return roomEntities;
    }


    public List<RoomCardDTO> getRooms(long memberId, int page) {
        List<RoomCardDTO> resRooms = new ArrayList<>();
        List<RoomEntity> roomEntities;
        if(page == 0) roomEntities = getPageRooms(page, 3);
        else roomEntities = getPageRooms(3 + (page-1)*5, 5);

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

    @Transactional
    public void enterRoom(RoomMemberID roomMemberID){
        List<JoinsEntity> joinsEntities = joinsMongoDBRepository.findByMemberIdAndRoomId(roomMemberID.getMemberId(), roomMemberID.getRoomId());
        if(joinsEntities.size()==0) throw new CustomUnprocessableException("해당 멤버와 roomId 조합이 없습니다.");
        JoinsEntity joinsEntity =  joinsEntities.get(0);
        int enter = joinsEntity.getEnterRoom();
        joinsEntity.setEnterRoom(enter+1);
        joinsMongoDBRepository.save(joinsEntity);
    }
}
