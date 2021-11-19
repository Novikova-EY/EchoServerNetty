package ru.novikova.echo_server.netty.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.util.Scanner;

public class Client {

    public static void main(String[] args) throws InterruptedException {
        new Client().run();
    }

    public void run() throws InterruptedException{
        final NioEventLoopGroup group = new NioEventLoopGroup();

        try {
            Bootstrap bootstrap = new Bootstrap()
                    .group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) {
                            socketChannel.pipeline().addLast(
                                    new LengthFieldBasedFrameDecoder(512, 0, 2 , 0, 2),
                                    new LengthFieldPrepender(2),
                                    new StringEncoder(),
                                    new StringDecoder(),
                                    new ClientHandler());
                        }
                    })
                    .option(ChannelOption.SO_KEEPALIVE, true);

            ChannelFuture channelFuture = bootstrap.connect("localhost", 9000).sync();

            System.out.println("Client started");

            while (true) {
                Scanner in = new Scanner(System.in);
                String input_message = in.nextLine();
                channelFuture.channel().writeAndFlush(input_message);
            }
        } finally {
            group.shutdownGracefully();
        }
    }
}
