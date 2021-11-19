package ru.novikova.echo_server.netty.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.Date;

public class ServerHandler extends SimpleChannelInboundHandler<String> {
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("Client connected");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("Client disconnected");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("Cause.server :" + cause.getMessage());
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String incomingMessage) throws Exception {
        System.out.println("Incoming message from client: " + new Date().toString() + ": " + incomingMessage);
        ctx.writeAndFlush(incomingMessage);
    }
}
