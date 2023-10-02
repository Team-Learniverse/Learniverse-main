package learniverse.learniversemain.controller.Exception;

public class CustomUnprocessableException extends RuntimeException{
    public CustomUnprocessableException(String message){
        super(message);
    }
}
