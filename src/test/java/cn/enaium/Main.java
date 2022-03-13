package cn.enaium;

import cn.enaium.message.Request;
import cn.enaium.protocol.MessageCodec;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.concurrent.DefaultPromise;
import io.netty.util.concurrent.Promise;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author Enaium
 */
public class Main {

    private static final LoggingHandler LOGGING_HANDLER = new LoggingHandler(LogLevel.INFO);
    private static final MessageCodec MESSAGE_CODEC = new MessageCodec();
    private static final Handler HANDLER = new Handler();

    private static Channel channel;

    public static void main(String[] args) {
        NioEventLoopGroup nioEventLoopGroup = new NioEventLoopGroup();
        try {
            channel = new Bootstrap().group(nioEventLoopGroup).channel(NioSocketChannel.class).handler(new ChannelInitializer<SocketChannel>() {
                @Override
                public void initChannel(SocketChannel channel) {
                    channel.pipeline().addLast(LOGGING_HANDLER);
                    channel.pipeline().addLast(MESSAGE_CODEC);
                    channel.pipeline().addLast(HANDLER);
                }
            }).connect("localhost", 3828).sync().channel();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Runtime.getRuntime().addShutdownHook(new Thread(nioEventLoopGroup::shutdownGracefully));

        Service service = getService(Service.class);
        System.out.println(service.render());
    }


    @SuppressWarnings("unchecked")
    private static <T> T getService(Class<T> klass) {

        Object o = Proxy.newProxyInstance(klass.getClassLoader(), new Class<?>[]{klass}, (proxy, method, args) -> {

            if (!method.isAnnotationPresent(Call.class)) {
                return null;
            }


            Promise<Object> promise = new DefaultPromise<>(channel.eventLoop());

            Call annotation = method.getAnnotation(Call.class);
            long increment = Util.increment();
            channel.writeAndFlush(new Request(increment, annotation.klass(), annotation.name(), method.getParameterTypes(), args));
            Main.HANDLER.getPromiseMap().put(increment, promise);

            promise.await();

            if (promise.cause() != null) {
                return new RuntimeException(promise.cause());
            }


            return promise.getNow();
        });

        return (T) o;
    }

}
