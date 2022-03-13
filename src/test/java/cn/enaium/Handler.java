package cn.enaium;

import cn.enaium.message.Response;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.concurrent.Promise;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Enaium
 */
public class Handler extends SimpleChannelInboundHandler<Response> {

    private final Map<Long, Promise<Object>> promiseMap = new HashMap<>();

    @Override
    public void channelRead0(ChannelHandlerContext channelHandlerContext, Response response) throws Exception {

        if (null == promiseMap.get(response.getOrder())) {
            return;
        }

        Promise<Object> promise = promiseMap.remove(response.getOrder());

        if (response.getResult() != null) {
            promise.setSuccess(response.getResult());
        } else {
            promise.setFailure(response.getThrowable());
        }
    }

    public Map<Long, Promise<Object>> getPromiseMap() {
        return promiseMap;
    }
}
