package learniverse.learniversemain.controller;


import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import learniverse.learniversemain.controller.response.Response;
import learniverse.learniversemain.dto.CoreTimeDTO;
import learniverse.learniversemain.dto.ScheduleDTO;
import learniverse.learniversemain.entity.CoreTimeEntity;
import learniverse.learniversemain.entity.ScheduleEntity;
import learniverse.learniversemain.service.RoomMainService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/room")
@Validated
public class RoomMainController {
    private final RoomMainService roomMainService;
    @PostMapping("/schedule/create")
    public ResponseEntity<Response> createSchedule(@Valid @RequestBody ScheduleDTO scheduleDTO){
        Response response = new Response();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType((new MediaType("application","json", Charset.forName("UTF-8"))));

        if(roomMainService.createSchedule(scheduleDTO)){
            response.setStatus(Response.StatusEnum.CREATED);
            response.setMessage("일정 생성 성공");
            return new ResponseEntity<>(response, headers, HttpStatus.CREATED);
        }
        else{
            response.setStatus(Response.StatusEnum.BAD_REQUEST);
            response.setMessage("해당 방 정보 없음. roomId 확인 필요");
            return new ResponseEntity<>(response, headers, HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/schedule/delete")
    public ResponseEntity<Response> deleteSchedule(@NotNull @RequestParam long scheduleId){
        Response response = new Response();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType((new MediaType("application","json", Charset.forName("UTF-8"))));

        if(roomMainService.deleteSchedule(scheduleId)){
            response.setStatus(Response.StatusEnum.OK);
            response.setMessage("일정 삭제 성공");
            return new ResponseEntity<>(response, headers, HttpStatus.OK);
        }
        else{
            response.setStatus(Response.StatusEnum.BAD_REQUEST);
            response.setMessage("일정 삭제 실패, scheduleId 확인 필요");
            return new ResponseEntity<>(response, headers, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/schedules")
    public ResponseEntity<Response> getSchedules(@RequestParam Long roomId){
        Response response = new Response();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType((new MediaType("application","json", Charset.forName("UTF-8"))));

        List<ScheduleEntity> scheduleEntities = roomMainService.getSchedules(roomId);
        if(scheduleEntities == null){
            response.setStatus(Response.StatusEnum.BAD_REQUEST);
            response.setMessage("조회 실패. roomId 확인 필요");
            return new ResponseEntity<>(response, headers, HttpStatus.BAD_REQUEST);
        }
        response.setStatus(Response.StatusEnum.OK);
        response.setMessage("일정 리스트 출력 성공");
        Map<String, List<ScheduleEntity>> data = new HashMap<>();
        data.put("schedules", scheduleEntities);
        response.setData(data);
        return new ResponseEntity<>(response, headers, HttpStatus.OK);
    }

    @PostMapping("/core/create")
    public ResponseEntity<Response>  createCoreTime(@Valid @RequestBody CoreTimeDTO coreTimeDTO){
        Response response = new Response();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType((new MediaType("application","json", Charset.forName("UTF-8"))));

        if(roomMainService.createCore(coreTimeDTO)){
            response.setStatus(Response.StatusEnum.CREATED);
            response.setMessage("코어타임 생성 성공");
            return new ResponseEntity<>(response, headers, HttpStatus.CREATED);
        }
        else{
            response.setStatus(Response.StatusEnum.BAD_REQUEST);
            response.setMessage("해당 방 정보 없음. roomId 확인 필요");
            return new ResponseEntity<>(response, headers, HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/core/delete")
    public ResponseEntity<Response>  deleteCoreTime(@NotNull @RequestParam long coreTimeId){
        Response response = new Response();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType((new MediaType("application","json", Charset.forName("UTF-8"))));

        if(roomMainService.deleteCore(coreTimeId)){
            response.setStatus(Response.StatusEnum.OK);
            response.setMessage("코어타임 삭제 성공");
            return new ResponseEntity<>(response, headers, HttpStatus.OK);
        }
        else{
            response.setStatus(Response.StatusEnum.BAD_REQUEST);
            response.setMessage("코어타임 삭제 실패, coreTimeId 확인 필요");
            return new ResponseEntity<>(response, headers, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/cores")
    public ResponseEntity<Response> getCores(@NotNull @RequestParam Long roomId){
        Response response = new Response();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType((new MediaType("application","json", Charset.forName("UTF-8"))));
        List<CoreTimeEntity> coreTimeEntities = roomMainService.getCores(roomId);

        if(coreTimeEntities == null){
            response.setStatus(Response.StatusEnum.BAD_REQUEST);
            response.setMessage("조회 실패. roomId 확인 필요");
            return new ResponseEntity<>(response, headers, HttpStatus.BAD_REQUEST);
        }
        response.setStatus(Response.StatusEnum.OK);
        response.setMessage("코어타임 리스트 출력 성공");
        Map<String, List<CoreTimeEntity>> data = new HashMap<>();
        data.put("cores", coreTimeEntities);
        response.setData(data);
        return new ResponseEntity<>(response, headers, HttpStatus.OK);
    }


}
