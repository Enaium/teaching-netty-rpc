package cn.enaium;

import cn.enaium.message.Request;
import cn.enaium.message.Response;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.lang.reflect.InvocationTargetException;

/**
 * @author Enaium
 */
public class PRCHandler extends SimpleChannelInboundHandler<Request> {
    @Override
    public void channelRead0(ChannelHandlerContext channelHandlerContext, Request request) {
        try {
            Class<?> aClass = Class.forName(request.getKlass());
            Object o = aClass.getConstructor().newInstance();
            Object invoke = aClass.getMethod(request.getName(), request.getParameterType()).invoke(o, request.getArgument());
            channelHandlerContext.channel().writeAndFlush(new Response(request.getOrder(), invoke, null));
        } catch (ClassNotFoundException | InvocationTargetException | InstantiationException | IllegalAccessException | NoSuchMethodException e) {
            e.printStackTrace();
            channelHandlerContext.channel().writeAndFlush(new Response(request.getOrder(), null, e.getCause()));
        }
    }
}
