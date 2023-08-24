package learniverse.learniversemain.service;

import learniverse.learniversemain.dto.CoreTimeDTO;
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
        if(!existRoom) return false;

        ScheduleEntity scheduleEntity = new ScheduleEntity(scheduleDTO);
        scheduleRepository.save(scheduleEntity);
        return true;
    }

    public boolean deleteSchedule(Long scheduleId){
        Optional<ScheduleEntity> scheduleEntity = scheduleRepository.findById(scheduleId);
        if(scheduleEntity.isEmpty()) return false;
        scheduleRepository.delete(scheduleEntity.get());
        return true;
    }

    public List<ScheduleEntity> getSchedules(Long roomId){
        boolean existRoom = roomRepository.existsByRoomId(roomId);
        if(!existRoom) return null;

        List<ScheduleEntity> scheduleEntities = scheduleRepository.findByRoomId(roomId);
        return scheduleEntities;
    }

    public boolean createCore(CoreTimeDTO coreTimeDTO){
        boolean existRoom = roomRepository.existsByRoomId(coreTimeDTO.getRoomId());
        if(!existRoom) return false;

        CoreTimeEntity coreTimeEntity = new CoreTimeEntity(coreTimeDTO);
        coreTimeRepository.save(coreTimeEntity);
        return true;

    }

    public boolean deleteCore(Long coreId){
        Optional<CoreTimeEntity> coreTimeEntity = coreTimeRepository.findById(coreId);
        if(coreTimeEntity.isEmpty()) return false;
        coreTimeRepository.delete(coreTimeEntity.get());
        return true;

    }

    public List<CoreTimeEntity> getCores(Long roomId){
        boolean existRoom = roomRepository.existsByRoomId(roomId);
        if(!existRoom) return null;

        List<CoreTimeEntity> coreTimeEntities = coreTimeRepository.findByRoomId(roomId);
        return coreTimeEntities;

    }
}
