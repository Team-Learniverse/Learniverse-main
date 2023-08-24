package learniverse.learniversemain.service;

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

    public void createCore(CoreTimeEntity coreTimeEntity){
        coreTimeRepository.save(coreTimeEntity);

    }

    //bool로수정
    public void deleteCore(Long coreId){
        coreTimeRepository.deleteById(coreId);

    }

    public List<CoreTimeEntity> getCores(Long roomId){
        List<CoreTimeEntity> coreTimeEntities = coreTimeRepository.findByRoomId(roomId);
        return coreTimeEntities;

    }
}
