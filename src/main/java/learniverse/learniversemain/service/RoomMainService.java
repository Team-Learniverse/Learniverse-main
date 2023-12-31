package learniverse.learniversemain.service;

import jakarta.transaction.Transactional;
import learniverse.learniversemain.controller.Exception.CannotFindRoomException;
import learniverse.learniversemain.controller.Exception.CustomBadRequestException;
import learniverse.learniversemain.dto.*;
import learniverse.learniversemain.controller.Exception.CustomUnprocessableException;
import learniverse.learniversemain.dto.BoardDTO;
import learniverse.learniversemain.dto.mongoDB.GitCodeDTO;
import learniverse.learniversemain.entity.*;
import learniverse.learniversemain.entity.ID.RoomMemberID;
import learniverse.learniversemain.entity.mongoDB.GitcodeEntity;
import learniverse.learniversemain.repository.*;
import learniverse.learniversemain.repository.mongoDB.GitCodeMongoDBRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.*;

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
    private final GitCodeMongoDBRepository gitCodeMongoDBRepository;
    private final MemberRepository memberRepository;

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


    public long createCore(CoreTimeDTO coreTimeDTO) {
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
        long memberId = fcmTokenDTO.getMemberId();
        FcmTokenEntity existedToken = fcmTokenRepository.findByMemberId(memberId);
        FcmTokenEntity newfcmTokenEntity = new FcmTokenEntity(fcmTokenDTO);

        if (existedToken != null) {
            existedToken.update(newfcmTokenEntity);
        } else {
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

    public boolean createIssue(Long memberId, IssueDTO issueDTO) { //디비에 이슈 등록
        IssueEntity issueEntity = new IssueEntity(issueDTO);

        //깃헙에 이슈 업로드 후 이슈 넘버 저장
        String gitIssueNumber = uploadIssue(issueEntity);
        issueEntity.setGitIssueNumber(gitIssueNumber);

        //이슈 상태 열림으로
        issueEntity.setIssueOpen(true);
        issueRepository.save(issueEntity);

        //깃헙에서 코드 가져와서 파일에 있는 코드 저장
        if (!issueDTO.getGitFileName().isEmpty()) {
            GitcodeEntity gitcodeEntity = new GitcodeEntity();
            String gitCode = getCodeFromGit(issueEntity);
            gitcodeEntity.setGitCode(gitCode);
            gitcodeEntity.setGitCodeModify(gitCode);
            gitcodeEntity.setIssueId(issueEntity.getIssueId());
            gitcodeEntity.setRoomId(issueEntity.getRoomId());
            gitcodeEntity.setCreatedDate(issueEntity.getCreatedDate());
            gitCodeMongoDBRepository.save(gitcodeEntity);
        }

        return true;
    }


    public String uploadIssue(IssueEntity issueEntity) { //깃헙에 이슈 업로드
        log.info("uploadIssue");

        String issueNumberinGit = "";

        String issueTitle = issueEntity.getIssueTitle();
        String fullUrl = issueEntity.getIssueGitUrl();
        String prefix = "https://github.com/";
        String issueGitUrl = "";

        if (fullUrl.startsWith(prefix)) {
            issueGitUrl = fullUrl.substring(prefix.length());
        } else {
            throw new CustomBadRequestException("해당 주소와 일치하는 레포지토리가 존재하지 않습니다.");
        }
        String issueDescription = issueEntity.getIssueDescription().replaceAll("\\\\", "\\\\\\\\");

        String addIssueUrl = "https://api.github.com/repos/" + issueGitUrl + "/issues";

        Long memberId = issueEntity.getMemberId();
        Optional<MemberEntity> memberEntity = memberRepository.findById(memberId);
        String accessCode = memberEntity.get().getAccessCode();
        log.info(accessCode);

        WebClient webClient = WebClient.builder()
                .baseUrl(addIssueUrl)
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + accessCode) //여기에 access token 넣기
                .defaultHeader("X-GitHub-Api-Version", "2022-11-28")
                .build();

        String requestBody = String.format("{\"title\":\"%s\",\"body\":\"%s\"}", issueTitle, issueDescription);

        Mono<Map<String, Object>> response = webClient.post()
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestBody)
                .retrieve()
                .onStatus(httpStatusCode -> httpStatusCode.is4xxClientError(), clientResponse -> {
                    return Mono.error(new CustomBadRequestException("레포지토리를 잘못 입력했습니다."));
                })
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

        log.info(responseBody.toString());

        return issueNumberinGit;
    }

    public String getCodeFromGit(IssueEntity issueEntity) { //깃헙 파일에서 코드 가져오기
        log.info("GitCode");

        String gitCode = "";
        String fullUrl = issueEntity.getGitFileName();
        String prefix = "https://github.com/";
        String blobIndicator = "blob/";
        String gitFileRepo = "";
        String ref = "";
        String gitFileName = "";

        int startIndex = fullUrl.indexOf(prefix);
        int endIndex = fullUrl.indexOf(blobIndicator);

        if (startIndex != -1 && endIndex != -1) {
            gitFileRepo = fullUrl.substring(startIndex + prefix.length(), endIndex);
            log.info("Git File Repo: " + gitFileRepo);

            int refStartIndex = endIndex + blobIndicator.length();
            int refEndIndex = fullUrl.indexOf('/', refStartIndex);
            if (refEndIndex != -1) {
                ref = fullUrl.substring(refStartIndex, refEndIndex);
                log.info("Branch Name: " + ref);

                gitFileName = fullUrl.substring(refEndIndex + 1);
                log.info("Git File Name: " + gitFileName);
            } else {
                throw new CustomBadRequestException("브랜치를 찾을 수 없습니다.");
            }
        } else {
            throw new CustomBadRequestException("해당 주소와 일치하는 레포지토리가 존재하지 않습니다.");
        }

        String getGitUrl = "https://api.github.com/repos/" + gitFileRepo + "contents/" + gitFileName + "?ref=" + ref;

        Long memberId = issueEntity.getMemberId();
        Optional<MemberEntity> memberEntity = memberRepository.findById(memberId);
        String accessCode = memberEntity.get().getAccessCode();
        log.info(accessCode);

        WebClient webClient = WebClient.builder()
                .baseUrl(getGitUrl)
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + accessCode) //여기에 access token 넣기
                .defaultHeader("X-GitHub-Api-Version", "2022-11-28")
                .build();

        // GitHub API에 GET 요청 보내기
        Mono<Map<String, Object>> response = webClient.get()
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .onStatus(httpStatusCode -> httpStatusCode.is4xxClientError(), clientResponse -> {
                    return Mono.error(new CustomBadRequestException("레포지토리에 해당 파일이 없습니다. 파일명을 확인해주세요"));
                })
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {
                });

        Map<String, Object> responseBody = response.block();

        if (responseBody != null && responseBody.containsKey("content")) {
            String base64Content = (String) responseBody.get("content");
            base64Content = base64Content.replaceAll("\\s", ""); // 공백 제거

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
    public void closeIssue(Long issueId) { //이슈 클로즈하기
        IssueEntity existedIssue = issueRepository.findById(issueId)
                .orElseThrow(() -> new IllegalArgumentException("해당 이슈가 없습니다."));
        //열려있으면 issueOpen false로
        if (existedIssue.getIssueOpen()) {
            changeIssue(existedIssue);
            existedIssue.setIssueOpen(false);
            existedIssue.setUpdatedDate(LocalDateTime.now());
        }

        issueRepository.save(existedIssue);
    }

    public void changeIssue(IssueEntity issueEntity) { //깃헙에 이슈 업데이트
        log.info("changeIssue");

        String fullUrl = issueEntity.getIssueGitUrl();
        String prefix = "https://github.com/";
        String issueGitUrl = "";

        if (fullUrl.startsWith(prefix)) {
            issueGitUrl = fullUrl.substring(prefix.length());
        } else {
            throw new CustomBadRequestException("해당 주소와 일치하는 레포지토리가 존재하지 않습니다.");
        }

        String issueNumberinGit = issueEntity.getGitIssueNumber();

        String addIssueUrl = "https://api.github.com/repos/" + issueGitUrl + "/issues/" + issueNumberinGit;

        Long memberId = issueEntity.getMemberId();
        Optional<MemberEntity> memberEntity = memberRepository.findById(memberId);
        String accessCode = memberEntity.get().getAccessCode();
        log.info(accessCode);

        WebClient webClient = WebClient.builder()
                .baseUrl(addIssueUrl)
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + accessCode) //여기에 access token 넣기
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

    /*@Transactional
    public void updateIssue(IssueDTO issueDTO) { //이슈 업데이트
        IssueEntity existedIssue = issueRepository.findById(issueDTO.getIssueId())
                .orElseThrow(() -> new IllegalArgumentException("해당 이슈가 없습니다."));
        if (issueDTO.getGitCode() != null) existedIssue.setGitCode(issueDTO.getGitCode());
        existedIssue.update(existedIssue);
        issueRepository.save(existedIssue);
    }*/

    @Transactional
    public void updateGitCode(GitCodeDTO gitCodeDTO) { //깃코드 업데이트
        GitcodeEntity existedGitcode = gitCodeMongoDBRepository.findByIssueId(gitCodeDTO.getIssueId());

        if (gitCodeDTO.getGitCodeModify() != null) existedGitcode.setGitCodeModify(gitCodeDTO.getGitCodeModify());
        gitCodeMongoDBRepository.save(existedGitcode);
    }

    public List<IssueEntity> getIssues(Long roomId) {
        List<IssueEntity> issuesEntites = issueRepository.findByRoomIdOrderByCreatedDateDesc(roomId);
        return issuesEntites;
    }

    public List<GitcodeEntity> getGitcodes(Long roomId) {
        List<GitcodeEntity> gitCodeEntities = gitCodeMongoDBRepository.findByRoomIdOrderByCreatedDateDesc(roomId);
        return gitCodeEntities;
    }

    public Optional<IssueEntity> getIssueById(Long issueId) {
        Optional<IssueEntity> issueEntity = issueRepository.findById(issueId);
        return issueEntity;
    }

    public String getGitcodeByIssueId(Long issueId) {
        GitcodeEntity gitCodeEntity = gitCodeMongoDBRepository.findByIssueId(issueId);

        if (gitCodeEntity != null) {
            String gitCode = gitCodeEntity.getGitCode();
            return gitCode;
        } else {
            return null;
        }
    }

    public String getGitcodeModifyByIssueId(Long issueId) {
        GitcodeEntity gitCodeEntity = gitCodeMongoDBRepository.findByIssueId(issueId);
        if (gitCodeEntity != null) {
            String gitCodeModify = gitCodeEntity.getGitCodeModify();
            return gitCodeModify;
        } else {
            return null;
        }
    }

    public boolean createOpinion(IssueOpinionDTO issueOpinionDTO) { //디비에 이슈 디스커션 등록
        if (issueOpinionDTO.getIssueOpinionCode() != null && !issueOpinionDTO.getIssueOpinionCode().isEmpty()) {
            if (issueOpinionDTO.getIssueOpinionStartLine() == null || issueOpinionDTO.getIssueOpinionEndLine() == null) {
                throw new CustomBadRequestException("수정하고 싶은 줄의 시작과 끝 번호를 입력해주세요.");
            }
        }
        IssueOpinionEntity issueOpinionEntity = new IssueOpinionEntity(issueOpinionDTO);
        long issueId = issueOpinionEntity.getIssueId();
        IssueEntity issueEntity = issueRepository.findById(issueId)
                .orElseThrow(() -> new IllegalArgumentException("해당 이슈가 없습니다."));

        //깃허브 이슈에 코멘트로 등록
        uploadOpinion(issueOpinionEntity, issueEntity);
        log.info("이슈 업로드 완료");

        //이슈 수락 false로
        issueOpinionEntity.setIssueAccepted(false);

        issueOpinionRepository.save(issueOpinionEntity);
        return true;
    }

    public void uploadOpinion(IssueOpinionEntity issueOpinionEntity, IssueEntity issueEntity) {
        log.info("uploadOpinion");

        String fullUrl = issueEntity.getIssueGitUrl();
        String prefix = "https://github.com/";
        String issueGitUrl = "";
        String issueOpinion = issueOpinionEntity.getIssueOpinion().replaceAll("\\\\", "\\\\\\\\").replaceAll("\n", "\\\\n");
        String issueOpinionCode = (issueOpinionEntity.getIssueOpinionCode() != null) ? issueOpinionEntity.getIssueOpinionCode().replaceAll("\\\\", "\\\\\\\\").replaceAll("\n", "\\\\n") : "코드 제안 없음";
        String issueComment = "<리뷰내용>: " + issueOpinion + "\\n<수정제안코드>: `" + issueOpinionCode + "`\\n\\n\\ncommented from Learniverse";
        log.info(issueComment);

        if (fullUrl.startsWith(prefix)) {
            issueGitUrl = fullUrl.substring(prefix.length());
        } else {
            throw new CustomBadRequestException("해당 주소와 일치하는 레포지토리가 존재하지 않습니다.");
        }

        String issueNumberinGit = issueEntity.getGitIssueNumber();

        String uploadIssueComment = "https://api.github.com/repos/" + issueGitUrl + "/issues/" +
                issueNumberinGit + "/comments";

        log.info(uploadIssueComment);

        Long memberId = issueOpinionEntity.getMemberId();
        Optional<MemberEntity> memberEntity = memberRepository.findById(memberId);
        String accessCode = memberEntity.get().getAccessCode();

        WebClient webClient = WebClient.builder()
                .baseUrl(uploadIssueComment)
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + accessCode) //여기에 access token 넣기
                .defaultHeader("X-GitHub-Api-Version", "2022-11-28")
                .build();

        String requestBody = String.format("{\"body\":\"%s\"}", issueComment);
        log.info(requestBody.toString());

        Mono<Map<String, Object>> response = webClient.post()
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestBody)
                .retrieve()
                /*.onStatus(httpStatusCode -> httpStatusCode.is4xxClientError(), clientResponse -> {
                    return Mono.error(new CustomBadRequestException("이슈 코멘트 등록에 문제가 발생했습니다."));
                })*/
                .onStatus(httpStatusCode -> httpStatusCode.is4xxClientError(), clientResponse -> {
                    return clientResponse.bodyToMono(String.class).flatMap(errorBody -> {
                        System.err.println("GitHub API 오류: " + errorBody);
                        return Mono.error(new CustomBadRequestException("이슈 코멘트 등록에 문제가 발생했습니다."));
                    });
                })
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {
                });

        //요청 실행 및 응답 처리
        response.block();
    }

    @Transactional
    public void applyOpinion(Long opinionId) { //이슈 디스커션 상태 true로 바꾸기 > 수락
        IssueOpinionEntity existOpinion = issueOpinionRepository.findById(opinionId)
                .orElseThrow(() -> new IllegalArgumentException("해당 이슈 디스커션이 없습니다."));

        //issue 수락 true로
        if (!existOpinion.getIssueAccepted()) {
            existOpinion.setIssueAccepted(true);
            existOpinion.setUpdatedDate(LocalDateTime.now());
        }

        issueOpinionRepository.save(existOpinion);
    }

    //수락 여부 출력
    public boolean isOpinonAccepted(Long opinionId) {
        IssueOpinionEntity issueOpinionEntity = issueOpinionRepository.findById(opinionId)
                .orElseThrow(() -> new IllegalArgumentException("해당 이슈 디스커션이 없습니다."));

        return issueOpinionEntity.getIssueAccepted();
    }

    public List<IssueOpinionEntity> getOpinions(Long issueId) {
        List<IssueOpinionEntity> issuesOpinionEntites = issueOpinionRepository.findByIssueId(issueId);
        return issuesOpinionEntites;
    }

}
