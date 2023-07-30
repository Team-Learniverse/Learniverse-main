package learniverse.learniversemain.controller;


import learniverse.learniversemain.entity.CoreTimeEntity;
import learniverse.learniversemain.entity.ScheduleEntity;
import learniverse.learniversemain.service.RoomMainService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @PostMapping("/core/create")
    public void createCoreTime(@RequestBody CoreTimeEntity coreTimeEntity){
        roomMainService.createCore(coreTimeEntity);
    }

    @PostMapping("/core/delete")
    public void deleteCoreTime(@RequestBody CoreTimeEntity coreTimeEntity){
        roomMainService.deleteCore(coreTimeEntity.getCoreTimeId());
    }


}
