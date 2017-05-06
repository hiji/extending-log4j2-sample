package exception;

/**
 * Created by hijiri on 2016/06/07.
 */
public class CustomException extends RuntimeException {

    private final String code;

    public CustomException(String code, String message) {
        super(message);
        this.code = code;
    }

    public CustomException(String code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
