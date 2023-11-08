package learniverse.learniversemain.controller;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import learniverse.learniversemain.controller.response.Response;
import learniverse.learniversemain.dto.*;
import learniverse.learniversemain.entity.HashtagEntity;
import learniverse.learniversemain.entity.ID.RoomMemberID;
import learniverse.learniversemain.entity.MemberEntity;
import learniverse.learniversemain.entity.RoomMemberEntity;
import learniverse.learniversemain.jwt.TokenService;
import learniverse.learniversemain.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Tag(name = "member", description = "member 관련 api")
@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
@Validated
public class MemberController {

    private final MemberService memberService;
    private final TokenService tokenService;

    @PostMapping("/profile/update")
    public ResponseEntity<Response> updateProfile(@Valid @RequestBody ProfileDTO profileDTO){
        Response response = new Response();

        memberService.updateProfile(profileDTO);

        response.setStatus(Response.StatusEnum.CREATED);
        response.setMessage("프로필 정보 저장 성공");
        return new ResponseEntity<>(response, HttpStatus.CREATED);

    }

    @PostMapping("/moon/add")
    public ResponseEntity<Response> saveMoon(@Valid @RequestBody MoonDTO moonDTO) {
        Response response = new Response();

        memberService.saveMoon(moonDTO);

        response.setStatus(Response.StatusEnum.CREATED);
        response.setMessage("달 추가 성공");
        return new ResponseEntity<>(response, HttpStatus.CREATED);

    }

    @PostMapping("/moon/add/core")
    public ResponseEntity<Response> saveCoreMoon(@Valid @RequestBody MoonDTO moonDTO) {
        Response response = new Response();

        memberService.saveCoreMoon(moonDTO);

        response.setStatus(Response.StatusEnum.OK);
        response.setMessage("달 추가 성공(코어타입 접속)");
        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    @Hidden
    @PostMapping("/moon/minus")
    public ResponseEntity<Response> deleteMoon(@Valid @RequestBody MoonDTO moonDTO) {
        Response response = new Response();

        memberService.deleteMoon(moonDTO);

        response.setStatus(Response.StatusEnum.OK);
        response.setMessage("달 삭제 성공");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/moon/list")
    public ResponseEntity<Response> getMoon(@RequestHeader("Authorization") String accessToken) {
        Response response = new Response();
        Map<String, List<ResMoonDTO>> data = new HashMap<>();
        Long memberId = tokenService.getMemberId(accessToken);
        data.put("moons", memberService.getMoon(memberId));
        response.setData(data);

        response.setStatus(Response.StatusEnum.OK);
        response.setMessage("달 리스트 출력 성공");
        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    @GetMapping("/room/list")
    public ResponseEntity<Response> getRooms(@RequestHeader("Authorization") String accessToken) {
        Response response = new Response();
        Long memberId = tokenService.getMemberId(accessToken);
        response.setData(memberService.getRooms(memberId));
        response.setStatus(Response.StatusEnum.OK);
        response.setMessage("참여 스터디룸 출력 성공");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/room/list/apply")
    public ResponseEntity<Response> getApplyRooms(@RequestHeader("Authorization") String accessToken) {
        Response response = new Response();
        Long memberId = tokenService.getMemberId(accessToken);
        List<RoomCardDTO> roomCardDTOS = memberService.getRoomsIs(memberId, false);
        Map<String, List<RoomCardDTO>> data = new HashMap<>();
        data.put("rooms", roomCardDTOS);
        response.setData(data);
        response.setStatus(Response.StatusEnum.OK);
        response.setMessage("신청한 스터디룸 출력 성공");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @GetMapping("/room/list/leader")
    public ResponseEntity<Response> getLeaderRooms(@RequestHeader("Authorization") String accessToken) {
        Response response = new Response();
        Long memberId = tokenService.getMemberId(accessToken);
        List<RoomCardDTO> roomCardDTOS = memberService.getRoomsIs(memberId, true);
        Map<String, List<RoomCardDTO>> data = new HashMap<>();
        data.put("rooms", roomCardDTOS);
        response.setData(data);
        response.setStatus(Response.StatusEnum.OK);
        response.setMessage("내가 만든 스터디룸 출력 성공");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/pin")
    public ResponseEntity<Response> addPin(@Valid @RequestBody RoomMemberID roomMemberID) {
        Response response = new Response();

        boolean change = memberService.updatePin(roomMemberID);
        response.setStatus(Response.StatusEnum.OK);
        response.setMessage("스터디룸 고정 \'" + change + "\' 로 변경 성공");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/profile")
    public ResponseEntity<Response> getMember(@RequestHeader("Authorization") String accessToken) {
        Response response = new Response();
        Map<String, Map<String, String>> data = new HashMap<>();
        Long memberId = tokenService.getMemberId(accessToken);
        data.put("member", memberService.getMember(memberId));
        response.setStatus(Response.StatusEnum.OK);
        response.setMessage("멤버 프로필 정보 출력 완료");
        response.setData(data);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/first")
    public ResponseEntity<Response> getMemberFirst(@RequestHeader("Authorization") String accessToken) {
        Response response = new Response();
        Map<String, String>   data = new HashMap<>();
        response.setStatus(Response.StatusEnum.OK);
        response.setMessage("최초 사용자 확인 정보 출력 완료");
        Long memberId = tokenService.getMemberId(accessToken);

        data.put("memberFirst", memberService.isMemberFirst(memberId));
        data.put("refreshToken", memberService.getRefreshToken(memberId));
        response.setData(data);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/repolanguage")
    public ResponseEntity<Response> getGitRepo(@RequestHeader("Authorization") String accessToken) {
        Response response = new Response();
        Map<String, String>   data = new HashMap<>();
        response.setStatus(Response.StatusEnum.OK);
        response.setMessage("레포언어 바이트 리스트 출력");
        Long memberId = tokenService.getMemberId(accessToken);

        data.put("repoLanguageList", memberService.getRepoLanguage(memberId));
        response.setData(data);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<Response> addPin(@RequestHeader("Authorization") String accessToken) {
        Response response = new Response();
        Long memberId = tokenService.getMemberId(accessToken);
        memberService.login(memberId);
        response.setStatus(Response.StatusEnum.OK);
        response.setMessage("최근 로그인 정보 업데이트 성공");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /*@GetMapping()
    public ResponseEntity<Response> getMemberByToken(Principal principal) { //token으로 멤버 조회
        Response response = new Response();

        response.setStatus(Response.StatusEnum.OK);
        response.setMessage("issue 출력 성공");
        Map<String, Object> data = new HashMap<>();
        data.put("issue", memberService.getMemberById(Long.valueOf(principal.getName())));
        response.setData(data);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }*/
}
