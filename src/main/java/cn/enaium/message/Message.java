package cn.enaium.message;

import java.io.Serializable;

/**
 * @author Enaium
 */
public class Message implements Serializable {
    private final long order;

    public Message(long order) {
        this.order = order;
    }

    public long getOrder() {
        return order;
    }
}
