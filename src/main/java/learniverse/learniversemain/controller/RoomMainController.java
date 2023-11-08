package learniverse.learniversemain.controller;


import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import learniverse.learniversemain.controller.response.Response;
import learniverse.learniversemain.dto.*;
import learniverse.learniversemain.dto.mongoDB.GitCodeDTO;
import learniverse.learniversemain.entity.*;
import learniverse.learniversemain.entity.mongoDB.GitcodeEntity;
import learniverse.learniversemain.jwt.TokenService;
import learniverse.learniversemain.service.RoomMainService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Tag(name = "roomMain", description = "스터디룸 입장 시 필요한 정보와 관련된 api")
@RestController
@RequiredArgsConstructor
@RequestMapping("/room")
@Validated
public class RoomMainController {
    private final RoomMainService roomMainService;
    private final TokenService tokenService;


    @PostMapping("/workspace/update")
    public ResponseEntity<Response> updateWorkspace(@Valid @RequestBody WorkspaceDTO workspaceDTO) {
        Response response = new Response();

        roomMainService.updateWorkspace(workspaceDTO);
        response.setStatus(Response.StatusEnum.CREATED);
        response.setMessage("워크스페이스 수정 성공");
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/core/create")
    public ResponseEntity<Response> createCoreTime(@Valid @RequestBody CoreTimeDTO coreTimeDTO) {
        Response response = new Response();

        long id = roomMainService.createCore(coreTimeDTO);
        Map<String, Long> data = new HashMap<>();
        data.put("coreTimeId", id);
        response.setData(data);
        response.setStatus(Response.StatusEnum.CREATED);
        response.setMessage("코어타임 생성 성공");
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @DeleteMapping("/core/delete")
    public ResponseEntity<Response> deleteCoreTime(@NotNull @RequestParam long coreTimeId) {
        Response response = new Response();
        if (roomMainService.deleteCore(coreTimeId)) {
            response.setStatus(Response.StatusEnum.OK);
            response.setMessage("코어타임 삭제 성공");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else throw new RuntimeException();
    }

    @GetMapping("/core/list")
    public ResponseEntity<Response> getCores(@NotNull @RequestParam Long roomId) {
        Response response = new Response();

        List<CoreTimeEntity> coreTimeEntities = roomMainService.getCores(roomId);

        response.setStatus(Response.StatusEnum.OK);
        response.setMessage("코어타임 리스트 출력 성공");
        Map<String, Object> data = new HashMap<>();
        data.put("cores", coreTimeEntities);
        data.put("isCore", roomMainService.isCore(roomId));
        response.setData(data);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/core/isCore")
    public ResponseEntity<Response> isCore(@NotNull @RequestParam Long roomId) {
        Response response = new Response();

        response.setStatus(Response.StatusEnum.OK);
        response.setMessage("코어타임 여부 출력 성공");
        Map<String, Boolean> data = new HashMap<>();
        data.put("isCore", roomMainService.isCore(roomId));
        response.setData(data);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/core/endTime")
    public ResponseEntity<Response> getEndTime(@NotNull @RequestParam Long coreTimeId) {
        Response response = new Response();

        response.setStatus(Response.StatusEnum.OK);
        response.setMessage("코어타임 endTime 출력 성공");
        Map<String, LocalDateTime> data = new HashMap<>();
        data.put("coreEndTime", roomMainService.getEndTime(coreTimeId));
        response.setData(data);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/core/id")
    public ResponseEntity<Response> getNowId(@NotNull @RequestParam Long roomId) {
        Response response = new Response();

        response.setStatus(Response.StatusEnum.OK);
        response.setMessage("coreTimeId 출력 성공");
        Map<String, Long> data = new HashMap<>();
        data.put("coreTimeId", roomMainService.getNowId(roomId));
        response.setData(data);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /*
    @Hidden
    @PostMapping("/schedule/create")
    public ResponseEntity<Response> createSchedule(@Valid @RequestBody ScheduleDTO scheduleDTO){
        Response response = new Response();

        if(roomMainService.createSchedule(scheduleDTO)){
            response.setStatus(Response.StatusEnum.CREATED);
            response.setMessage("일정 생성 성공");
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        }
        else throw new RuntimeException();
    }

    @Hidden
    @DeleteMapping("/schedule/delete")
    public ResponseEntity<Response> deleteSchedule(@NotNull @RequestParam long scheduleId){
        Response response = new Response();

        if(roomMainService.deleteSchedule(scheduleId)){
            response.setStatus(Response.StatusEnum.OK);
            response.setMessage("일정 삭제 성공");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        else throw new RuntimeException();
    }

    @Hidden
    @GetMapping("/schedules")
    public ResponseEntity<Response> getSchedules(@RequestParam Long roomId){
        Response response = new Response();

        List<ScheduleEntity> scheduleEntities = roomMainService.getSchedules(roomId);

        response.setStatus(Response.StatusEnum.OK);
        response.setMessage("일정 리스트 출력 성공");
        Map<String, List<ScheduleEntity>> data = new HashMap<>();
        data.put("schedules", scheduleEntities);
        response.setData(data);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
     */

    @PostMapping("/board/create")
    public ResponseEntity<Response> createBoardPost(@Valid @RequestBody BoardDTO boardDTO) {
        Response response = new Response();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType((new MediaType("application", "json", Charset.forName("UTF-8"))));

        if (roomMainService.createBoard(boardDTO)) {
            response.setStatus(Response.StatusEnum.CREATED);
            response.setMessage("게시물 생성 성공");
            response.setData(boardDTO);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } else throw new RuntimeException();
    }

    @DeleteMapping("/board/delete")
    public ResponseEntity<Response> deleteBoardPost(@NotNull @RequestParam long boardId) {
        Response response = new Response();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType((new MediaType("application", "json", Charset.forName("UTF-8"))));

        roomMainService.deleteBoardPost(boardId);
        response.setMessage(boardId + "게시물 삭제 성공");

        return new ResponseEntity<>(response, headers, HttpStatus.ACCEPTED);
    }

    @PostMapping("/board/update")
    public ResponseEntity<Response> updateBoardPost(@RequestBody BoardEntity boardEntity) {
        Response response = new Response();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType((new MediaType("application", "json", Charset.forName("UTF-8"))));

        roomMainService.updateBoard(boardEntity);
        response.setMessage("게시물 업데이트 성공");
        response.setData(boardEntity);

        return new ResponseEntity<>(response, headers, HttpStatus.ACCEPTED);
    }

    @GetMapping("/boards")
    public List<BoardEntity> getBoards(@RequestParam Long roomId) {
        return roomMainService.getBoards(roomId);
    }

    @GetMapping("/board")
    public Optional<BoardEntity> getBoardById(@RequestParam Long boardId) {
        return roomMainService.getBoardById(boardId);
    }


    @PostMapping("/alarm/createToken")
    public ResponseEntity<Response> createFcmToken(@Valid @RequestBody FcmTokenDTO fcmTokenDTO) {
        Response response = new Response();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType((new MediaType("application", "json", Charset.forName("UTF-8"))));

        if (roomMainService.createToken(fcmTokenDTO)) {
            response.setStatus(Response.StatusEnum.CREATED);
            response.setMessage("토큰 생성 성공");
            response.setData(fcmTokenDTO);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } else throw new RuntimeException();
    }

    @PostMapping("/alarm/updateToken")
    public ResponseEntity<Response> updateFcmToken(@RequestBody FcmTokenEntity fcmTokenEntity) {
        Response response = new Response();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType((new MediaType("application", "json", Charset.forName("UTF-8"))));

        roomMainService.updateToken(fcmTokenEntity);
        response.setMessage("토큰 업데이트 성공");
        response.setData(fcmTokenEntity);

        return new ResponseEntity<>(response, headers, HttpStatus.ACCEPTED);
    }

    @GetMapping("/getTokenByMemberId")
    public Optional<FcmTokenEntity> getTokenByMemberId(@RequestParam Long memberId) {
        return Optional.ofNullable(roomMainService.getTokenByMemberId(memberId));
    }

    @GetMapping("/tokenList")
    public ResponseEntity<Response> getTokenList(@NotNull @RequestParam long roomId) {
        Response response = new Response();

        List<FcmTokenEntity> tokenList = roomMainService.getTokenList(roomId, false);
        if (tokenList == null) throw new RuntimeException();

        response.setStatus(Response.StatusEnum.OK);
        response.setMessage("토큰 리스트 출력 성공");
        response.setData(tokenList);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/issue/create")
    public ResponseEntity<Response> createIssue(@RequestHeader("Authorization") String accessToken, @Valid @RequestBody IssueDTO issueDTO) {
        Response response = new Response();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType((new MediaType("application", "json", Charset.forName("UTF-8"))));

        Long memberId = tokenService.getMemberId(accessToken);

        if (roomMainService.createIssue(memberId, issueDTO)) {
            response.setStatus(Response.StatusEnum.CREATED);
            response.setMessage("이슈 생성 성공");
            response.setData(issueDTO);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } else throw new RuntimeException();
    }

    //이슈 클로즈
    @PostMapping("/issue/close")
    public ResponseEntity<Response> closeIssue(@Valid @RequestBody IssueEntity issueEntity) {
        Response response = new Response();

        roomMainService.closeIssue(issueEntity.getIssueId());
        response.setMessage("깃허브에 이슈 클로즈 성공");
        response.setData(issueEntity.getIssueId());

        return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
    }

    @PostMapping("/issue/update")
    public ResponseEntity<Response> updateIssue(@Valid @RequestBody GitCodeDTO gitCodeDTO) {
        Response response = new Response();

        roomMainService.updateGitCode(gitCodeDTO);

        response.setStatus(Response.StatusEnum.OK);
        response.setMessage("gitCode 수정 성공");
        response.setData(gitCodeDTO);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/issues")
    public ResponseEntity<Response> getIssues(@RequestParam Long roomId) {
        Response response = new Response();

        List<IssueEntity> issueEntities = roomMainService.getIssues(roomId);
        List<GitcodeEntity> gitcodeEntities = roomMainService.getGitcodes(roomId);

        response.setStatus(Response.StatusEnum.OK);
        response.setMessage("issue 리스트 출력 성공");
        Map<String, Object> data = new HashMap<>();
        data.put("issues", issueEntities);
        data.put("gitCodes and gitCodeModifies", gitcodeEntities);
        response.setData(data);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/issue")
    public ResponseEntity<Response> getIssueById(@RequestParam Long issueId) {
        Response response = new Response();

        response.setStatus(Response.StatusEnum.OK);
        response.setMessage("issue 출력 성공");
        Map<String, Object> data = new HashMap<>();
        data.put("issue", roomMainService.getIssueById(issueId));
        data.put("gitCode", roomMainService.getGitcodeByIssueId(issueId));
        data.put("gitCodeModify", roomMainService.getGitcodeModifyByIssueId(issueId));
        response.setData(data);
        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    @PostMapping("/discussion/create")
    public ResponseEntity<Response> createOpinion(@Valid @RequestBody IssueOpinionDTO issueOpinionDTO) {
        Response response = new Response();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType((new MediaType("application", "json", Charset.forName("UTF-8"))));

        if (roomMainService.createOpinion(issueOpinionDTO)) {
            response.setStatus(Response.StatusEnum.CREATED);
            response.setMessage("디스커션 생성 성공");
            response.setData(issueOpinionDTO);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } else throw new RuntimeException();
    }

    @PostMapping("/discussion/apply")
    public ResponseEntity<Response> applyOpinion(@Valid @RequestBody IssueOpinionEntity issueOpinionEntity) {
        Response response = new Response();

        roomMainService.applyOpinion(issueOpinionEntity.getOpinionId());
        response.setMessage("이슈 디스커션 수락 요청 성공");
        response.setData(issueOpinionEntity.getIssueId());

        return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
    }

    @GetMapping("/discussion/isAccepted")
    public ResponseEntity<Response> isOpinionAccepted(@NotNull @RequestParam Long opinionId) {
        Response response = new Response();

        response.setStatus(Response.StatusEnum.OK);
        response.setMessage("디스커션 수락 여부 출력 성공");
        Map<String, Boolean> data = new HashMap<>();
        data.put("isOpinionAccepted", roomMainService.isOpinonAccepted(opinionId));
        response.setData(data);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/discussions")
    public ResponseEntity<Response> getOpinions(@RequestParam Long issueId) {
        Response response = new Response();

        List<IssueOpinionEntity> issueOpinionEntities = roomMainService.getOpinions(issueId);

        response.setStatus(Response.StatusEnum.OK);
        response.setMessage("이슈디스커션 리스트 출력 성공");
        Map<String, Object> data = new HashMap<>();
        data.put("opinions", issueOpinionEntities);
        response.setData(data);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
