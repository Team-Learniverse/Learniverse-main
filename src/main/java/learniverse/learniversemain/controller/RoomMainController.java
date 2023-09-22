package learniverse.learniversemain.controller;


import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import learniverse.learniversemain.controller.response.Response;
import learniverse.learniversemain.entity.BoardEntity;
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
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/room")
@Validated
public class RoomMainController {
    private final RoomMainService roomMainService;
    @PostMapping("/schedule/create")
    public void createSchedule(@Valid @RequestBody ScheduleEntity scheduleEntity){
        roomMainService.createSchedule(scheduleEntity);
    }

    @DeleteMapping("/schedule/delete")
    public void deleteSchedule(@NotNull @RequestParam long scheduleId){
        roomMainService.deleteSchedule(scheduleId);
    }

    @GetMapping("/schedules")
    public List<ScheduleEntity> getSchedules(@RequestParam Long roomId){
        return roomMainService.getSchedules(roomId);
    }

    @PostMapping("/core/create")
    public void createCoreTime(@RequestBody CoreTimeEntity coreTimeEntity){
        roomMainService.createCore(coreTimeEntity);
    }

    @DeleteMapping("/core/delete")
    public void deleteCoreTime(@NotNull @RequestParam long coreTimeId){
        roomMainService.deleteCore(coreTimeId);
    }

    @GetMapping("/cores")
    public List<CoreTimeEntity> getCores(@RequestParam Long roomId){
        return roomMainService.getCores(roomId);
    }

    @PostMapping("/board/create")
    public ResponseEntity<Response>  createBoardPost(@RequestBody BoardEntity boardEntity){
        Response response = new Response();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType((new MediaType("application","json", Charset.forName("UTF-8"))));
        roomMainService.createBoard(boardEntity);

        response.setStatus(Response.StatusEnum.CREATED);
        response.setMessage("게시물 생성 성공");
        response.setData(boardEntity);

        return new ResponseEntity<>(response, headers, HttpStatus.CREATED);
    }

    @DeleteMapping("/board/delete")
    public ResponseEntity<Response> deleteBoardPost(@NotNull @RequestParam long boardId){
        Response response = new Response();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType((new MediaType("application","json", Charset.forName("UTF-8"))));

        roomMainService.deleteBoardPost(boardId);
        response.setMessage(boardId+"게시물 삭제 성공");

        return new ResponseEntity<>(response, headers, HttpStatus.ACCEPTED);
    }

    @PostMapping("/board/update")
    public ResponseEntity<Response> updateBoardPost(@RequestBody BoardEntity boardEntity){
        Response response = new Response();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType((new MediaType("application","json", Charset.forName("UTF-8"))));

        roomMainService.updateBoard(boardEntity);
        response.setMessage("게시물 업데이트 성공");
        response.setData(boardEntity);

        return new ResponseEntity<>(response, headers, HttpStatus.ACCEPTED);
    }

    @GetMapping("/boards")
    public List<BoardEntity> getBoards(@RequestParam Long roomId){
        return roomMainService.getBoards(roomId);
    }

    @GetMapping("/board")
    public Optional<BoardEntity> getBoardById(@RequestParam Long boardId){
        return roomMainService.getBoardById(boardId);
    }
}
