package org.myorg.netty.netty1;


//import io.grpc.netty.shaded.io.netty.bootstrap.ServerBootstrap;
//import io.grpc.netty.shaded.io.netty.channel.ChannelFuture;
//import io.grpc.netty.shaded.io.netty.channel.ChannelInitializer;
//import io.grpc.netty.shaded.io.netty.channel.EventLoopGroup;
//import io.grpc.netty.shaded.io.netty.channel.nio.NioEventLoopGroup;
//import io.grpc.netty.shaded.io.netty.channel.socket.SocketChannel;
//import io.grpc.netty.shaded.io.netty.channel.socket.nio.NioServerSocketChannel;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetSocketAddress;

/*
* 1、绑定到服务器将在其上监听并接受传入连接请求的端口
* 2、配置channel，以将有关的入站消息通知给EchoServerhandler实例
* */
public class EchoServer {
    private final int port;

    public EchoServer(int port) {
        this.port = port;
    }

    public static void main(String[] args) throws Exception {
//        if (args.length != 1) {
//            System.err.println(
//                    "Usage: " + EchoServer.class.getSimpleName() + "<port>");
//        }
//        int port = Integer.parseInt(args[0]);
        int port = 8080;
        new EchoServer(port).start();
    }

    public void start() throws Exception {
        final EchoServerHandler serverHandler = new EchoServerHandler();
        //创建EventLoopGroup
        EventLoopGroup group = new NioEventLoopGroup();

        try {
            //创建ServerBootstrap
            ServerBootstrap b = new ServerBootstrap();

            b.group(group)
                    .channel(NioServerSocketChannel.class)//指定所使用的nio传输channel
                    .localAddress(new InetSocketAddress(port)) //使用指定的端口设置套接字地址
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        //添加一个EchoServerHandler到子channel的channelpipeline
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(serverHandler);//EchoServerHandler被标注为@Shareable，所以我们可以总是使用同样的实例
                        }
                    });

            ChannelFuture f = b.bind().sync(); //异步地绑定服务器，调用sync()方法阻塞直到绑定完成
            f.channel().closeFuture().sync(); //获取Channel的closeFuture,并且阻塞当前线程直到它完成
        } finally {
            group.shutdownGracefully().sync();//关闭EventLoopGroup,释放所有的资源
        }
    }
}
