package learniverse.learniversemain.controller.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Error {
    private String field;
    private String message;
    private String invalidValue;
}
