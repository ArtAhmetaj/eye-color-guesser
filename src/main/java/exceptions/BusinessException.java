package exceptions;

public abstract class BusinessException extends  RuntimeException{
    public String getMessage() {
        return message;
    }

    private final String message;

    protected  BusinessException(String message){
        this.message = message;
    }
}
