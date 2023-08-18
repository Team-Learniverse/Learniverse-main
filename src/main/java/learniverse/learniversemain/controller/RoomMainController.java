package learniverse.learniversemain.controller;


import learniverse.learniversemain.entity.CoreTimeEntity;
import learniverse.learniversemain.entity.ScheduleEntity;
import learniverse.learniversemain.service.RoomMainService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/room")
public class RoomMainController {
    private final RoomMainService roomMainService;
    @PostMapping("/schedule/create")
    public void createSchedule(@RequestBody ScheduleEntity scheduleEntity){
        roomMainService.createSchedule(scheduleEntity);
    }

    @PostMapping("/schedule/delete")
    public void deleteSchedule(@RequestBody ScheduleEntity scheduleEntity){
        roomMainService.deleteSchedule(scheduleEntity.getScheduleId());
    }

    @GetMapping("/schedules")
    public List<ScheduleEntity> getSchedules(@RequestParam Long roomId){
        return roomMainService.getSchedules(roomId);
    }

    @PostMapping("/core/create")
    public void createCoreTime(@RequestBody CoreTimeEntity coreTimeEntity){
        roomMainService.createCore(coreTimeEntity);
    }

    @PostMapping("/core/delete")
    public void deleteCoreTime(@RequestBody CoreTimeEntity coreTimeEntity){
        roomMainService.deleteCore(coreTimeEntity.getCoreTimeId());
    }

    @GetMapping("/cores")
    public List<CoreTimeEntity> getCores(@RequestParam Long roomId){
        return roomMainService.getCores(roomId);
    }


}
