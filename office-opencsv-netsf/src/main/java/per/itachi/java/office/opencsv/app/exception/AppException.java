package per.itachi.java.office.opencsv.app.exception;

public class AppException extends RuntimeException{

    public AppException() {
        super();
    }

    public AppException(String message) {
        super(message);
    }

    public AppException(String message, Throwable cause) {
        super(message, cause);
    }
}
