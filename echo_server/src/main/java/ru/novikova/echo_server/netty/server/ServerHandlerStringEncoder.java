package ru.novikova.echo_server.netty.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.nio.charset.StandardCharsets;
import java.util.List;

public class ServerHandlerStringEncoder extends MessageToMessageEncoder<String> {
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, String msg, List<Object> out) throws Exception {
        out.add(msg.getBytes(StandardCharsets.UTF_8));
    }
}
