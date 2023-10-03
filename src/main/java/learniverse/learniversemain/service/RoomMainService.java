package learniverse.learniversemain.service;

import jakarta.transaction.Transactional;
import learniverse.learniversemain.controller.Exception.CannotFindRoomException;
import learniverse.learniversemain.controller.Exception.CustomBadRequestException;
import learniverse.learniversemain.dto.*;
import learniverse.learniversemain.entity.*;
import learniverse.learniversemain.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j

@Service
@RequiredArgsConstructor
public class RoomMainService {
    private final ScheduleRepository scheduleRepository;
    private final CoreTimeRepository coreTimeRepository;
    private final RoomRepository roomRepository;
    private final BoardRepository boardRepository;
    private final IssueRepository issueRepository;


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
        CoreTimeEntity coreTimeEntity = new CoreTimeEntity(coreTimeDTO);
        //방체크
        boolean existRoom = roomRepository.existsByRoomId(coreTimeDTO.getRoomId());
        if(!existRoom) throw new CannotFindRoomException();

        //중복체크
        CoreTimeEntity coreTimeEntities = coreTimeRepository
                .findOneByRoomIdAndCoreStartTimeLessThanEqualAndCoreEndTimeGreaterThan(coreTimeDTO.getRoomId(), coreTimeDTO.getCoreStartTime(), coreTimeDTO.getCoreStartTime());
        if(coreTimeEntities != null) throw new CustomBadRequestException("해당 시간과 겹치는 코어타임 시간이 이미 존재합니다.");

        //중복체크 end 기준으로도 진행
        coreTimeEntities = coreTimeRepository
                .findOneByRoomIdAndCoreStartTimeLessThanEqualAndCoreEndTimeGreaterThan(coreTimeDTO.getRoomId(), coreTimeEntity.getCoreEndTime(), coreTimeEntity.getCoreEndTime());
        if(coreTimeEntities != null) throw new CustomBadRequestException("해당 시간과 겹치는 코어타임 시간이 이미 존재합니다.");

//        if(coreTimeDTO.getCoreEndDate().isBefore(coreTimeDTO.getCoreStartDate()))
//            throw new CustomBadRequestException("coreEndTime은 coreStartTime 이후 datetime이어야 합니다.");
//
//        if(coreTimeDTO.getCoreEndDate().isEqual(coreTimeDTO.getCoreStartDate()))
//            throw new CustomBadRequestException("coreEndTime은 coreStartTime 이후 datetime이어야 합니다.");


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

        List<CoreTimeEntity> coreTimeEntities = coreTimeRepository
                .findByRoomIdAndCoreEndTimeGreaterThanOrderByCoreStartTime(roomId, LocalDateTime.now());
        return coreTimeEntities;
    }

    public boolean isCore(Long roomId){
        boolean existRoom = roomRepository.existsByRoomId(roomId);
        if(!existRoom) throw new CannotFindRoomException();

        CoreTimeEntity coreTimeEntity = coreTimeRepository.findOneByRoomIdAndCoreStartTimeLessThanEqualAndCoreEndTimeGreaterThan(roomId, LocalDateTime.now(), LocalDateTime.now());
        if(coreTimeEntity == null) return false;
        else return true;
    }

    public LocalDateTime getEndTime(Long coreTimeId){
        CoreTimeEntity coreTimeEntity = coreTimeRepository.findById(coreTimeId)
                .orElseThrow(() -> new CustomBadRequestException("해당 coreTimeId와 매칭되는 코어타임이 존재하지 않습니다."));

        return coreTimeEntity.getCoreEndTime();
    }

    public long getNowId(Long roomId){
        boolean existRoom = roomRepository.existsByRoomId(roomId);
        if(!existRoom) throw new CannotFindRoomException();

        CoreTimeEntity coreTimeEntity = coreTimeRepository.findOneByRoomIdAndCoreStartTimeLessThanEqualAndCoreEndTimeGreaterThan(roomId, LocalDateTime.now(), LocalDateTime.now());
        if(coreTimeEntity == null) throw new CustomBadRequestException("현재 코어타임이 아닙니다. 코어타임인 경우 해당 API를 호출해주세요.");
        else return coreTimeEntity.getCoreTimeId();
    }

    public void updateWorkspace(WorkspaceDTO workspaceDTO){
        RoomEntity roomEntity = roomRepository.findById(workspaceDTO.getRoomId())
                .orElseThrow(()-> new CannotFindRoomException());

        if(workspaceDTO.getRoomGitOrg() != null) roomEntity.setRoomGitOrg(workspaceDTO.getRoomGitOrg());
        if(workspaceDTO.getRoomFigma() != null) roomEntity.setRoomFigma(workspaceDTO.getRoomFigma());
        if(workspaceDTO.getRoomNotion() != null) roomEntity.setRoomNotion(workspaceDTO.getRoomNotion());
        if(workspaceDTO.getRoomGoogleDrive() != null) roomEntity.setRoomGoogleDrive(workspaceDTO.getRoomGoogleDrive());

        roomRepository.save(roomEntity);
    }

    public boolean createBoard(BoardDTO boardDTO){
        BoardEntity boardEntity = new BoardEntity(boardDTO);
        boardRepository.save(boardEntity);
        return true;
    }

    public void deleteBoardPost(Long boardPostId){
        boardRepository.deleteById(boardPostId);
    }

    @Transactional
    public void updateBoard(BoardEntity boardEntity){
        BoardEntity exitedBoard = boardRepository.findById(boardEntity.getBoardId())
                .orElseThrow(()-> new IllegalArgumentException("해당 방이 없습니다."));
        exitedBoard.update(boardEntity);
    }

    public List<BoardEntity> getBoards(Long roomId){
        List<BoardEntity> boardsEntities = boardRepository.findByRoomId(roomId);
        return boardsEntities;
    }

    public Optional<BoardEntity> getBoardById(Long boardId){
        Optional<BoardEntity> boardsEntity = boardRepository.findById(boardId);
        return boardsEntity;
    }

    public boolean createIssue(IssueDTO issueDTO){ //디비에 이슈 등록
        IssueEntity issueEntity = new IssueEntity(issueDTO);

        //깃헙에 이슈 업로드
        uploadIssue(issueEntity);
        //깃헙에서 코드 가져오기
        String gitCode = getCodeFromGit(issueEntity);
        issueEntity.setGitCode(gitCode);
        log.info("Decoded issueEntity.getGitCode1: " + issueEntity.getGitCode());

        issueRepository.save(issueEntity);

        log.info("Decoded issueEntity.getGitCode2: " + issueEntity.getGitCode());

        return true;
    }


    public void uploadIssue(IssueEntity issueEntity){ //깃헙에 이슈 업로드
        log.info("uploadIssue");

        String issueGitOwner=issueEntity.getIssueGitOwner();
        String issueGitRepo=issueEntity.getIssueGitRepo();
        String issueTitle=issueEntity.getIssueTitle();
        String issueDescription=issueEntity.getIssueDescription();

        String addIssueUrl= "https://api.github.com/repos/"+issueGitOwner + "/"+ issueGitRepo+"/issues";

        WebClient webClient = WebClient.builder()
                .baseUrl(addIssueUrl)
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer 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") //여기에 access token 넣기
                .defaultHeader("X-GitHub-Api-Version", "2022-11-28")
                .build();

        String requestBody = String.format("{\"title\":\"%s\",\"body\":\"%s\"}", issueTitle, issueDescription);

        Mono<Void> response = webClient.post()
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(Void.class);

        //요청 실행 및 응답 처리
        response.block(); //블로킹 방식으로 요청 보내고 응답 기다림

    }

    public String getCodeFromGit(IssueEntity issueEntity){
        log.info("GitCode");

        String gitCode="";
        String issueGitOwner=issueEntity.getIssueGitOwner();
        String issueGitRepo=issueEntity.getIssueGitRepo();
        String gitFileName=issueEntity.getGitFileName();

        String getGitUrl= "https://api.github.com/repos/"+issueGitOwner + "/"+ issueGitRepo+"/contents/"+gitFileName;

        WebClient webClient = WebClient.builder()
                .baseUrl(getGitUrl)
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer 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") //여기에 access token 넣기
                .defaultHeader("X-GitHub-Api-Version", "2022-11-28")
                .build();

        // GitHub API에 GET 요청 보내기
        Mono<Map<String, Object>> response = webClient.get()
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {});

        Map<String, Object> responseBody = response.block();

        if (responseBody != null && responseBody.containsKey("content")) {
            String base64Content = (String) responseBody.get("content");
            base64Content = base64Content.replaceAll("\\s", ""); // 공백 제거
            //base64Content = base64Content.replaceAll("^(.*?)base64,", ""); // "base64," 이전 문자열 제거

            try {
                byte[] decodedBytes = Base64.getDecoder().decode(base64Content);
                String decodedContent = new String(decodedBytes, StandardCharsets.UTF_8);
                log.info("Decoded Content: " + decodedContent);

                gitCode = decodedContent;

            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
        }

        return gitCode;
    }

    @Transactional
    public void updateIssue(IssueEntity issueEntity){
        IssueEntity existedIssue = issueRepository.findById(issueEntity.getIssueId())
                .orElseThrow(()-> new IllegalArgumentException("해당 방이 없습니다."));

        changeIssue(existedIssue);
    }

    public void changeIssue(IssueEntity issueEntity){ //깃헙에 이슈 업로드
        log.info("uploadIssue");

        String issueGitOwner=issueEntity.getIssueGitOwner();
        String issueGitRepo=issueEntity.getIssueGitRepo();
        String issueTitle=issueEntity.getIssueTitle();
        String issueDescription=issueEntity.getIssueDescription();

        String addIssueUrl= "https://api.github.com/repos/"+issueGitOwner + "/"+ issueGitRepo+"/issues";

        WebClient webClient = WebClient.builder()
                .baseUrl(addIssueUrl)
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer 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") //여기에 access token 넣기
                .defaultHeader("X-GitHub-Api-Version", "2022-11-28")
                .build();

        String requestBody = String.format("{\"title\":\"%s\",\"body\":\"%s\"}", issueTitle, issueDescription);

        Mono<Void> response = webClient.post()
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(Void.class);

        //요청 실행 및 응답 처리
        response.block(); //블로킹 방식으로 요청 보내고 응답 기다림

    }


    public List<IssueEntity> getIssues(Long roomId){
        List<IssueEntity> issuesEntites = issueRepository.findByRoomId(roomId);
        return issuesEntites;
    }

    public Optional<IssueEntity> getIssueById(Long issueId){
        Optional<IssueEntity> issueEntity = issueRepository.findById(issueId);
        return issueEntity;
    }
}
