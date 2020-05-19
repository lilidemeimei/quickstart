package org.myorg.test;

import org.myorg.nio.NioServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.Iterator;


public class test {
    private static final Logger LOGGER = LoggerFactory.getLogger(NioServer.class);

    public void start() throws IOException {
        //1.创建selector
        Selector selector = Selector.open();

        //2.通过serversocketchannel创建channel通道
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();

        //3.为channel通道绑定监听端口
        serverSocketChannel.bind(new InetSocketAddress("127.0.0.1",8000));

        //4.设置channel为非阻塞模式，非阻塞模式下，accept()会立刻返回
        serverSocketChannel.configureBlocking(false);

        //5.将channel注册到selector上，监听连接事件
        SelectionKey key = serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        LOGGER.info("start");

        //6.循环等待新的连接
        while (true) {
            //有多少通道已就绪
            int readyChannels = selector.select();
            System.out.println("readyChannels: "+readyChannels);

            if (readyChannels == 0) {
                continue;
            }

            Iterator<SelectionKey> keyIterator = selector.selectedKeys().iterator();
            while (keyIterator.hasNext()) {

                //selectionKey实例
                SelectionKey sKey = (SelectionKey) keyIterator.next();

                //移除set中的当前selectionKey
                keyIterator.remove();

                //7.根据就绪状态
                if (sKey.isAcceptable()) {

                    //监听新进来的连接
                    ServerSocketChannel ac = (ServerSocketChannel) sKey.channel();
                    ac.accept();

//                    SocketChannel ac = serverSocketChannel.accept();
//                    ac.register(selector, SelectionKey.OP_READ);
//                    ac.configureBlocking(false);
//                    ByteBuffer buf = ByteBuffer.allocate(1000);
//                    int byteRead = ac.read(buf);
//                    System.out.println(byteRead);
                    System.out.println("1111");

                }
                else if (sKey.isReadable()) {

                    System.out.println("22222");
                }
//                else if (sKey.isWritable()) {
//
//                } else if (sKey.isConnectable()) {
//                    System.out.println("22222");
//
//                }

            }
        }

    }
//    public static void main(String[] args) {
//        NioHttpServer server = new NioHttpServer();
//        try {
//            server.start();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
}

