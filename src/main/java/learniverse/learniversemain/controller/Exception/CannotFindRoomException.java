package learniverse.learniversemain.controller.Exception;

public class CannotFindRoomException extends RuntimeException {
    @Override
    public String getMessage() {
        return "해당 방이 존재하지 않습니다. roomId 확인 필요";
    }
}
