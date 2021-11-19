package ru.novikova.echo_server.netty.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.Date;

public class ClientHandler extends SimpleChannelInboundHandler<String> {
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, String incomingMessage) throws Exception {
        System.out.println("ECHO: incoming message from client: " + new Date().toString() + ": " + incomingMessage);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("Cause.client :" + cause.getMessage());
    }
}
