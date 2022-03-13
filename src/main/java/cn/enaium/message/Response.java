package cn.enaium.message;

/**
 * @author Enaium
 */
public class Response extends Message {
    private final Object result;
    private final Throwable throwable;

    public Response(long order, Object result, Throwable throwable) {
        super(order);
        this.result = result;
        this.throwable = throwable;
    }

    public Object getResult() {
        return result;
    }

    public Throwable getThrowable() {
        return throwable;
    }
}
