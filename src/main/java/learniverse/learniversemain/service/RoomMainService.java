package learniverse.learniversemain.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import learniverse.learniversemain.controller.Exception.CannotFindRoomException;
import learniverse.learniversemain.controller.Exception.CustomBadRequestException;
import learniverse.learniversemain.dto.CoreTimeDTO;
import learniverse.learniversemain.dto.RoomSettingDTO;
import learniverse.learniversemain.dto.ScheduleDTO;
import learniverse.learniversemain.entity.CoreTimeEntity;
import learniverse.learniversemain.entity.RoomEntity;
import learniverse.learniversemain.entity.ScheduleEntity;
import learniverse.learniversemain.repository.CoreTimeRepository;
import learniverse.learniversemain.repository.RoomRepository;
import learniverse.learniversemain.repository.ScheduleRepository;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoomMainService {
    private final ScheduleRepository scheduleRepository;
    private final CoreTimeRepository coreTimeRepository;
    private final RoomRepository roomRepository;



    public boolean createSchedule(ScheduleDTO scheduleDTO){
        //roomId 확인
        boolean existRoom = roomRepository.existsByRoomId(scheduleDTO.getRoomId());
        if(!existRoom) throw new CannotFindRoomException();

        ScheduleEntity scheduleEntity = new ScheduleEntity(scheduleDTO);
        scheduleRepository.save(scheduleEntity);
        return true;
    }

    public boolean deleteSchedule(Long scheduleId){
        ScheduleEntity scheduleEntity = scheduleRepository.findById(scheduleId)
                .orElseThrow(()-> new CustomBadRequestException("일정 삭제 실패, scheduleId 확인 필요"));
        scheduleRepository.delete(scheduleEntity);
        return true;
    }

    public List<ScheduleEntity> getSchedules(Long roomId){
        boolean existRoom = roomRepository.existsByRoomId(roomId);
        if(!existRoom) throw new CannotFindRoomException();

        List<ScheduleEntity> scheduleEntities = scheduleRepository.findByRoomId(roomId);
        return scheduleEntities;
    }

    public boolean createCore(CoreTimeDTO coreTimeDTO){
        //중복체크
        CoreTimeEntity coreTimeEntities = coreTimeRepository
                .findOneByRoomIdAndCoreStartDateLessThanEqualAndCoreEndDateGreaterThan(coreTimeDTO.getRoomId(), coreTimeDTO.getCoreStartDate(), coreTimeDTO.getCoreStartDate());
        if(coreTimeEntities != null) throw new CustomBadRequestException("해당 시간과 겹치는 코어타임 시간이 이미 존재합니다.");

        if(coreTimeDTO.getCoreEndDate().isBefore(coreTimeDTO.getCoreStartDate()))
            throw new CustomBadRequestException("coreEndTime은 coreStartTime 이후 datetime이어야 합니다.");

        if(coreTimeDTO.getCoreEndDate().isEqual(coreTimeDTO.getCoreStartDate()))
            throw new CustomBadRequestException("coreEndTime은 coreStartTime 이후 datetime이어야 합니다.");

        boolean existRoom = roomRepository.existsByRoomId(coreTimeDTO.getRoomId());
        if(!existRoom) throw new CannotFindRoomException();

        CoreTimeEntity coreTimeEntity = new CoreTimeEntity(coreTimeDTO);
        coreTimeRepository.save(coreTimeEntity);
        return true;

    }

    public boolean deleteCore(Long coreId){
        CoreTimeEntity coreTimeEntity = coreTimeRepository.findById(coreId)
                .orElseThrow(()-> new CustomBadRequestException("코어타임 삭제 실패, coreTimeId 확인 필요"));
        coreTimeRepository.delete(coreTimeEntity);
        return true;

    }

    public List<CoreTimeEntity> getCores(Long roomId){
        boolean existRoom = roomRepository.existsByRoomId(roomId);
        if(!existRoom) throw new CannotFindRoomException();

        List<CoreTimeEntity> coreTimeEntities = coreTimeRepository.findByRoomId(roomId);
        return coreTimeEntities;
    }

    public boolean isCore(Long roomId){
        boolean existRoom = roomRepository.existsByRoomId(roomId);
        if(!existRoom) throw new CannotFindRoomException();

        CoreTimeEntity coreTimeEntities = coreTimeRepository.findOneByRoomIdAndCoreStartDateLessThanEqualAndCoreEndDateGreaterThan(roomId, LocalDateTime.now(), LocalDateTime.now());
        if(coreTimeEntities == null) return false;
        else return true;
    }
}
