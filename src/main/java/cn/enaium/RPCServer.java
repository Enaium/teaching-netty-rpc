package cn.enaium;

import cn.enaium.protocol.MessageCodec;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 * @author Enaium
 */
public class RPCServer {

    private static final LoggingHandler LOGGING_HANDLER = new LoggingHandler(LogLevel.INFO);
    private static final MessageCodec MESSAGE_CODEC = new MessageCodec();
    private static final PRCHandler PRC_HANDLER = new PRCHandler();

    public static void main(String[] args) {
        NioEventLoopGroup boss = new NioEventLoopGroup();
        NioEventLoopGroup worker = new NioEventLoopGroup();
        try {
            Channel localhost = new ServerBootstrap().group(boss, worker).channel(NioServerSocketChannel.class).childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                public void initChannel(SocketChannel channel) {
                    channel.pipeline().addLast(LOGGING_HANDLER);
                    channel.pipeline().addLast(MESSAGE_CODEC);
                    channel.pipeline().addLast(PRC_HANDLER);
                }
            }).bind("localhost", 3828).sync().channel();
            System.out.println("Runnable...");
            localhost.closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }
    }
}
