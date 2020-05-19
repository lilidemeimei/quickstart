package org.myorg.netty.netty1;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;

public class EchoClient {
    private final String host;
    private final int port;

    public EchoClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void start() throws Exception {
        EventLoopGroup group = new NioEventLoopGroup();

        try {
            Bootstrap b = new Bootstrap();//创建bootstrap
            b.group(group) //指定EventLoopGroup以处理客户端事件，需要适用于nio的实现
                    .channel(NioSocketChannel.class)//适用于nio传输的channel类型
                    .remoteAddress(new InetSocketAddress(host, port))//设置服务器的InetSocketAddress
                    .handler(new ChannelInitializer<SocketChannel>() {
                        //在创建channel时，向channelPipeline中添加一个EchoClientHandler实例
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(new EchoClientHandler());
                        }
                    });

            //连接到远程节点，阻塞等待直到连接完成
            ChannelFuture f = b.connect().sync();
            //阻塞直到channel关闭
            f.channel().closeFuture().sync();
        } finally {
            group.shutdownGracefully().sync();//关闭线程池并释放所有的资源
        }
    }

    public static void main(String[] args) throws Exception{
//        if (args.length != 2) {
//            return;
//        }
//        String host = args[0];
//        int port = Integer.parseInt(args[1]);
        String host = "127.0.0.1";
        int port = 8080;
        new EchoClient(host, port).start();


    }
}
