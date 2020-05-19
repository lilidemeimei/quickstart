package org.myorg.nio1;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

public class Work implements Runnable {
    private Selector selector;

    public Work(Selector selector) {
        this.selector = selector;
    }

    @Override
    public void run() {
        try {
            while (true) {
                int red = selector.select();
                if (red == 0) {
                    continue;
                }
                Iterator<SelectionKey> keyIterator = selector.selectedKeys().iterator();
                while (keyIterator.hasNext()) {
                    SelectionKey sKey = (SelectionKey) keyIterator.next();
//                System.out.println(sKey.channel());
                    keyIterator.remove();
//                    if (sKey.isAcceptable()) {
////                    ServerSocketChannel ac = (ServerSocketChannel) sKey.channel();
////                    SocketChannel socketChannel = ac.accept();
//                        SocketChannel socketChannel = serverSocketChannel.accept();
//                        socketChannel.configureBlocking(false);
//                        socketChannel.register(selector, SelectionKey.OP_READ);
//                        System.out.println("11111");
//                    } else
                        if (sKey.isReadable()) {
                        SocketChannel sc = (SocketChannel) sKey.channel();
                        ByteBuffer buf = ByteBuffer.allocate(1000);
                        sc.configureBlocking(false);
                        int by = sc.read(buf);
                        System.out.println(new String(buf.array()));
                        sc.close();
                    }
//                else if (sKey.isWritable()) {
//
//                } else if (sKey.isConnectable()) {
//                    System.out.println("22222");
//                }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
