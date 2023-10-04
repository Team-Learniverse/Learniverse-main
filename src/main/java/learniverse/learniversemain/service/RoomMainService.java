package learniverse.learniversemain.service;

import jakarta.transaction.Transactional;
import learniverse.learniversemain.controller.Exception.CannotFindRoomException;
import learniverse.learniversemain.controller.Exception.CustomBadRequestException;
import learniverse.learniversemain.dto.*;
import learniverse.learniversemain.controller.Exception.CustomUnprocessableException;
import learniverse.learniversemain.dto.BoardDTO;
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
import java.util.ArrayList;
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
    private final IssueOpinionRepository issueOpinionRepository;
    private final FcmTokenRepository fcmTokenRepository;
    private final RoomMemberRepository roomMemberRepository;

    /*
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
    */


    public long createCore(CoreTimeDTO coreTimeDTO){
        CoreTimeEntity coreTimeEntity = new CoreTimeEntity(coreTimeDTO);
        //방체크
        boolean existRoom = roomRepository.existsByRoomId(coreTimeDTO.getRoomId());
        if (!existRoom) throw new CannotFindRoomException();

        //중복체크
        CoreTimeEntity coreTimeEntities = coreTimeRepository
                .findOneByRoomIdAndCoreStartTimeLessThanEqualAndCoreEndTimeGreaterThan(coreTimeEntity.getRoomId(), coreTimeEntity.getCoreStartTime(), coreTimeEntity.getCoreStartTime());
        if (coreTimeEntities != null) throw new CustomUnprocessableException("해당 시간과 겹치는 코어타임 시간이 이미 존재합니다.");

        //중복체크 end 기준으로도 진행
        coreTimeEntities = coreTimeRepository
                .findOneByRoomIdAndCoreStartTimeLessThanEqualAndCoreEndTimeGreaterThan(coreTimeEntity.getRoomId(), coreTimeEntity.getCoreEndTime(), coreTimeEntity.getCoreEndTime());
        if (coreTimeEntities != null) throw new CustomUnprocessableException("해당 시간과 겹치는 코어타임 시간이 이미 존재합니다.");

//        if(coreTimeDTO.getCoreEndDate().isBefore(coreTimeDTO.getCoreStartDate()))
//            throw new CustomBadRequestException("coreEndTime은 coreStartTime 이후 datetime이어야 합니다.");
//
//        if(coreTimeDTO.getCoreEndDate().isEqual(coreTimeDTO.getCoreStartDate()))
//            throw new CustomBadRequestException("coreEndTime은 coreStartTime 이후 datetime이어야 합니다.");


        return coreTimeRepository.save(coreTimeEntity).getCoreTimeId();
    }

    public boolean deleteCore(Long coreId) {
        CoreTimeEntity coreTimeEntity = coreTimeRepository.findById(coreId)
                .orElseThrow(() -> new CustomBadRequestException("코어타임 삭제 실패, coreTimeId 확인 필요"));
        coreTimeRepository.delete(coreTimeEntity);
        return true;

    }

    public List<CoreTimeEntity> getCores(Long roomId) {
        boolean existRoom = roomRepository.existsByRoomId(roomId);
        if (!existRoom) throw new CannotFindRoomException();

        LocalDateTime now = LocalDateTime.now().plusHours(9);
        List<CoreTimeEntity> coreTimeEntities = coreTimeRepository
                .findByRoomIdAndCoreEndTimeGreaterThanOrderByCoreStartTime(roomId, now);
        return coreTimeEntities;
    }

    public boolean isCore(Long roomId) {
        boolean existRoom = roomRepository.existsByRoomId(roomId);
        if (!existRoom) throw new CannotFindRoomException();

        LocalDateTime now = LocalDateTime.now().plusHours(9);
        CoreTimeEntity coreTimeEntity = coreTimeRepository.findOneByRoomIdAndCoreStartTimeLessThanEqualAndCoreEndTimeGreaterThan(roomId, now, now);
        if (coreTimeEntity == null) return false;
        else return true;
    }

    public LocalDateTime getEndTime(Long coreTimeId) {
        CoreTimeEntity coreTimeEntity = coreTimeRepository.findById(coreTimeId)
                .orElseThrow(() -> new CustomBadRequestException("해당 coreTimeId와 매칭되는 코어타임이 존재하지 않습니다."));

        return coreTimeEntity.getCoreEndTime();
    }

    public long getNowId(Long roomId) {
        boolean existRoom = roomRepository.existsByRoomId(roomId);
        if (!existRoom) throw new CannotFindRoomException();

        LocalDateTime now = LocalDateTime.now().plusHours(9);
        CoreTimeEntity coreTimeEntity = coreTimeRepository.findOneByRoomIdAndCoreStartTimeLessThanEqualAndCoreEndTimeGreaterThan(roomId, now, now);
        if (coreTimeEntity == null) throw new CustomBadRequestException("현재 코어타임이 아닙니다. 코어타임인 경우 해당 API를 호출해주세요.");
        else return coreTimeEntity.getCoreTimeId();
    }

    public void updateWorkspace(WorkspaceDTO workspaceDTO) {
        RoomEntity roomEntity = roomRepository.findById(workspaceDTO.getRoomId())
                .orElseThrow(() -> new CannotFindRoomException());

        if (workspaceDTO.getRoomGitOrg() != null) roomEntity.setRoomGitOrg(workspaceDTO.getRoomGitOrg());
        if (workspaceDTO.getRoomFigma() != null) roomEntity.setRoomFigma(workspaceDTO.getRoomFigma());
        if (workspaceDTO.getRoomNotion() != null) roomEntity.setRoomNotion(workspaceDTO.getRoomNotion());
        if (workspaceDTO.getRoomGoogleDrive() != null) roomEntity.setRoomGoogleDrive(workspaceDTO.getRoomGoogleDrive());

        roomRepository.save(roomEntity);
    }

    public boolean createBoard(BoardDTO boardDTO) {
        BoardEntity boardEntity = new BoardEntity(boardDTO);
        boardRepository.save(boardEntity);
        return true;
    }

    public void deleteBoardPost(Long boardPostId) {
        boardRepository.deleteById(boardPostId);
    }

    @Transactional
    public void updateBoard(BoardEntity boardEntity) {
        BoardEntity exitedBoard = boardRepository.findById(boardEntity.getBoardId())
                .orElseThrow(() -> new IllegalArgumentException("해당 방이 없습니다."));
        exitedBoard.update(boardEntity);
    }

    public List<BoardEntity> getBoards(Long roomId) {
        List<BoardEntity> boardsEntities = boardRepository.findByRoomId(roomId);
        return boardsEntities;
    }

    public Optional<BoardEntity> getBoardById(Long boardId) {
        Optional<BoardEntity> boardsEntity = boardRepository.findById(boardId);
        return boardsEntity;
    }

    @Transactional
    public boolean createToken(FcmTokenDTO fcmTokenDTO) {
        long memberId =fcmTokenDTO.getMemberId();
        FcmTokenEntity existedToken = fcmTokenRepository.findByMemberId(memberId);
        FcmTokenEntity newfcmTokenEntity = new FcmTokenEntity(fcmTokenDTO);

        if (existedToken != null){
            existedToken.update(newfcmTokenEntity);
        }
        else{
            fcmTokenRepository.save(newfcmTokenEntity);
        }
        return true;
    }

    @Transactional
    public void updateToken(FcmTokenEntity fcmTokenEntity) {
        FcmTokenEntity exitedToken = fcmTokenRepository.findById(fcmTokenEntity.getMemberId())
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자의 토큰이 없습니다."));
        exitedToken.update(fcmTokenEntity);
    }

    public FcmTokenEntity getTokenByMemberId(Long memberId) {
        FcmTokenEntity fcmTokenEntity = fcmTokenRepository.findByMemberId(memberId);
        return fcmTokenEntity;
    }

    public List<FcmTokenEntity> getTokenList(long roomId, boolean isWait) {
        List<FcmTokenEntity> tokenList = new ArrayList<>();
        List<RoomMemberEntity> roomMemberEntities = roomMemberRepository.findByRoomId(roomId);
        if (roomMemberEntities.isEmpty()) throw new CannotFindRoomException();

        if (isWait) {
            roomMemberEntities.removeIf(entity -> entity.isLeader() == true);
        } else {
            roomMemberEntities.removeIf(entity -> entity.isWait() == true);
        }


        for (RoomMemberEntity roomMemberEntity : roomMemberEntities) {
            long memberId = roomMemberEntity.getMemberId();
            FcmTokenEntity fcmTokenEntity = fcmTokenRepository.findByMemberId(memberId);
            tokenList.add(fcmTokenEntity);
        }
        return tokenList;
    }

    public boolean createIssue(IssueDTO issueDTO) { //디비에 이슈 등록
        IssueEntity issueEntity = new IssueEntity(issueDTO);

        //깃헙에 이슈 업로드 후 이슈 넘버 저장
        String gitIssueNumber = uploadIssue(issueEntity);
        issueEntity.setGitIssueNumber(gitIssueNumber);

        //깃헙에서 코드 가져와서 파일에 있는 코드 저장
        String gitCode = getCodeFromGit(issueEntity);
        issueEntity.setGitCode(gitCode);
        issueEntity.setIssueOpen(true);

        issueRepository.save(issueEntity);

        return true;
    }


    public String uploadIssue(IssueEntity issueEntity) { //깃헙에 이슈 업로드
        log.info("uploadIssue");

        String issueNumberinGit = "";

        String issueGitUrl = issueEntity.getIssueGitUrl();
        String issueTitle = issueEntity.getIssueTitle();
        String issueDescription = issueEntity.getIssueDescription();

        String addIssueUrl = "https://api.github.com/repos/" + issueGitUrl + "/issues";

        WebClient webClient = WebClient.builder()
                .baseUrl(addIssueUrl)
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer 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") //여기에 access token 넣기
                .defaultHeader("X-GitHub-Api-Version", "2022-11-28")
                .build();

        String requestBody = String.format("{\"title\":\"%s\",\"body\":\"%s\"}", issueTitle, issueDescription);

        Mono<Map<String, Object>> response = webClient.post()
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {
                });

        //요청 실행 및 응답 처리
        Map<String, Object> responseBody = response.block();

        if (responseBody != null && responseBody.containsKey("number")) {
            try {
                issueNumberinGit = responseBody.get("number").toString();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
        }

        return issueNumberinGit;
    }

    public String getCodeFromGit(IssueEntity issueEntity) { //깃헙 파일에서 코드 가져오기
        log.info("GitCode");

        String gitCode = "";
        String issueGitUrl = issueEntity.getIssueGitUrl();
        String gitFileName = issueEntity.getGitFileName();

        String getGitUrl = "https://api.github.com/repos/" + issueGitUrl + "/contents/" + gitFileName;

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
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {
                });

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
    public void updateIssue(Long issueId) { //이슈 클로즈하기
        IssueEntity existedIssue = issueRepository.findById(issueId)
                .orElseThrow(() -> new IllegalArgumentException("해당 이슈가 없습니다."));
        //열려있으면 issueOpen false로
        if(existedIssue.getIssueOpen()){
            changeIssue(existedIssue);
            existedIssue.setIssueOpen(false);
        }

        existedIssue.update(existedIssue);
    }

    public void changeIssue(IssueEntity issueEntity) { //깃헙에 이슈 업데이트
        log.info("changeIssue");

        String issueGitUrl = issueEntity.getIssueGitUrl();

        String issueNumberinGit = issueEntity.getGitIssueNumber();

        String addIssueUrl = "https://api.github.com/repos/" + issueGitUrl + "/issues/" + issueNumberinGit;

        WebClient webClient = WebClient.builder()
                .baseUrl(addIssueUrl)
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer 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") //여기에 access token 넣기
                .defaultHeader("X-GitHub-Api-Version", "2022-11-28")
                .build();

        //이슈 클로즈
        String requestBody = String.format("{\"state\":\"closed\"}");

        Mono<Void> response = webClient.patch()
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(Void.class);

        //요청 실행 및 응답 처리
        response.block(); //블로킹 방식으로 요청 보내고 응답 기다림

    }


    public List<IssueEntity> getIssues(Long roomId) {
        List<IssueEntity> issuesEntites = issueRepository.findByRoomId(roomId);
        return issuesEntites;
    }

    public Optional<IssueEntity> getIssueById(Long issueId) {
        Optional<IssueEntity> issueEntity = issueRepository.findById(issueId);
        return issueEntity;
    }

    public boolean createOpinion(IssueOpinionDTO issueOpinionDTO) { //디비에 이슈 디스커션 등록
        IssueOpinionEntity issueOpinionEntity = new IssueOpinionEntity(issueOpinionDTO);
        issueOpinionRepository.save(issueOpinionEntity);
        return true;
    }

    public List<IssueOpinionEntity> getOpinions(Long issueId) {
        List<IssueOpinionEntity> issuesOpinionEntites = issueOpinionRepository.findByIssueId(issueId);
        return issuesOpinionEntites;
    }

}
