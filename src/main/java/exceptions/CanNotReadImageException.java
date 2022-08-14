package exceptions;

public class CanNotReadImageException extends BusinessException {
    protected CanNotReadImageException() {
        super("Can not read current image given!");
    }
}
