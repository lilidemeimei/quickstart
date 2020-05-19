package org.myorg.netty.UDP;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;

import java.io.File;
import java.io.RandomAccessFile;
import java.net.InetSocketAddress;

public class LogEventBroadcaster {
    private final EventLoopGroup group;
    private final Bootstrap bootstrap;
    private final File file;


    public LogEventBroadcaster(InetSocketAddress address, File file) {
        group = new NioEventLoopGroup();
        bootstrap = new Bootstrap();
        bootstrap.group(group).channel(NioDatagramChannel.class)
                .option(ChannelOption.SO_BROADCAST, true)//设置SO_BROADCAST套接字选项
        .handler(new LogEventEncoder(address));
        this.file = file;
    }

    public void run() throws Exception {
        System.out.println("running......");
        Channel ch = bootstrap.bind(0).sync().channel();//绑定channel
        long pointer = 0;
        for(;;) {
            long len = file.length();
            System.out.println("lens: " + len);
            if (len < pointer) {
                //file was reset
                pointer = len;
            } else if (len > pointer) {
                //Content was added
                RandomAccessFile raf = new RandomAccessFile(file, "r");
                raf.seek(pointer);//设置当前的文件指针，以确保没有任何的旧日志被发送
                String line;
                //对于每个日志条目，写入一个logEvent到channel中
                while ((line = raf.readLine()) != null) {
                    System.out.println(line);
                    ch.writeAndFlush(new LogEvent(null, -1, file.getAbsolutePath(), line));
                }
                pointer = raf.getFilePointer();//存储其在文件中的当前位置
                raf.close();
            }
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                Thread.interrupted();
                break;
            }
        }
    }

    public void stop() {
        group.shutdownGracefully();
    }

    public static void main(String[] args) throws Exception {
        int port = 9000;
        String file="/Users/limeitang/esc1.log";
        LogEventBroadcaster broadcaster = new LogEventBroadcaster(
                new InetSocketAddress("255.255.255.255", port), new File(file));

        try {
            broadcaster.run();
        } finally {
            broadcaster.stop();
        }
    }
}
