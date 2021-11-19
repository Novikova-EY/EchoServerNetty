package ru.novikova.echo_server.netty.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.bytes.ByteArrayDecoder;
import io.netty.handler.codec.bytes.ByteArrayEncoder;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

public class Server {

    private final int port;

    public static void main(String[] args) throws InterruptedException {
        new Server(9000).run();
    }

    public Server(int port) {
        this.port = port;
    }

    public void run() throws InterruptedException {
        final NioEventLoopGroup bossGroup = new NioEventLoopGroup(1);
        final NioEventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap server = new ServerBootstrap();
            server
                    .group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(
                                    new LengthFieldBasedFrameDecoder(512, 0, 2 , 0, 2),
                                    new LengthFieldPrepender(2),
//                                    new LineBasedFrameDecoder(128),
                                    new ByteArrayDecoder(),
                                    new ByteArrayEncoder(),
                                    new ServerHandlerStringDecoder(),
                                    new ServerHandlerStringEncoder(),
//                                    new StringEncoder(),
//                                    new StringDecoder(),
                                    new ServerHandler()
                            );
                        }
                    })
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);

            ChannelFuture future = server.bind(9000).sync();

            System.out.println("Server started");

            future.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
