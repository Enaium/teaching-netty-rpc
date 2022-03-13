package cn.enaium.message;

/**
 * @author Enaium
 */
public class Request extends Message {
    private final String klass;
    private final String name;
    private final Class<?>[] parameterType;
    private final Object[] argument;

    public Request(long order, String klass, String name, Class<?>[] parameterType, Object[] argument) {
        super(order);
        this.klass = klass;
        this.name = name;
        this.parameterType = parameterType;
        this.argument = argument;
    }

    public String getKlass() {
        return klass;
    }

    public String getName() {
        return name;
    }

    public Class<?>[] getParameterType() {
        return parameterType;
    }

    public Object[] getArgument() {
        return argument;
    }
}
