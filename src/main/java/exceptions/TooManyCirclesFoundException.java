package exceptions;

public class TooManyCirclesFoundException extends  BusinessException{

   public TooManyCirclesFoundException() {
        super("Too many circles found in image, adjust image or parameter tuners");
    }
}
