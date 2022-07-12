package exception;

public class CustomException extends RuntimeException {

    public CustomException(ErrorCode errorCode) { super(errorCode.getMessage()); }

    public CustomException(ErrorCode errorCode, Throwable cause) { super(errorCode.getMessage(), cause); }

    public static void validate(boolean expression, ErrorCode errorCode) {
        if(!expression) {
            throw new CustomException(errorCode);
        }
    }
}
