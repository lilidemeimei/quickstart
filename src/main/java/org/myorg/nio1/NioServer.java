package org.myorg.nio1;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;

public class NioServer {
    private static final Logger LOGGER = LoggerFactory.getLogger(NioServer.class);
    private ServerSocketChannel serverSocketChannel;
    private Selector selector;
    private long timeout;

    public NioServer(long timeout) {
        this.timeout = timeout;
    }

    public void start() {
        try {
            selector = Selector.open();
            serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.bind(new InetSocketAddress("127.0.0.1",8000));
            serverSocketChannel.configureBlocking(false);
            SelectionKey key = serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
            LOGGER.info("start");

            while (true) {
                int readyChannels = selector.select(this.timeout);
                if (readyChannels == 0) {
                    continue;
                }
                Iterator<SelectionKey> keyIterator = selector.selectedKeys().iterator();
                while (keyIterator.hasNext()) {
                    SelectionKey sKey = (SelectionKey) keyIterator.next();
                    keyIterator.remove();
                    if (sKey.isAcceptable()) {
                        acceptHander(serverSocketChannel, selector);
                    } else if (sKey.isReadable()) {
                        readHander(sKey, selector);
                    } else if (sKey.isWritable()) {
                        writeHandler(sKey, selector);
                    }
                    else if (sKey.isConnectable()) {
                        System.out.println("4444");
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            close(selector);
        }
    }

    private void registerChannel(Selector selector, SelectableChannel channel, int ops) throws IOException {
        if (channel == null) {
            return;
        }
        channel.configureBlocking(false);
        channel.register(selector, ops);
    }

    private void acceptHander(ServerSocketChannel serverSocketChannel, Selector selector) {
        try {
            SocketChannel socketChannel = serverSocketChannel.accept();
            socketChannel.configureBlocking(false);
            registerChannel(selector, socketChannel, SelectionKey.OP_READ);
//            socketChannel.register(selector, SelectionKey.OP_READ);
        } catch (IOException e) {
            e.printStackTrace();
        }
//        socketChannel.write(ByteBuffer.wrap("HelloWorld".getBytes()));
//        System.out.println("11111");
//        socketChannel.close();
    }

    private void readHander(SelectionKey sKey, Selector selector) {
        try {
            SocketChannel sc = (SocketChannel) sKey.channel();
            ByteBuffer buf = ByteBuffer.allocate(1000);
            sc.configureBlocking(false);
            while (sc.read(buf) > 0) {
                buf.flip();
                System.out.println(new String(buf.array()));
            }
            sc.close();
//            sc.register(selector, SelectionKey.OP_WRITE);
            System.out.println("22222");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeHandler(SelectionKey sKey, Selector selector) throws IOException {
        SocketChannel sc = (SocketChannel) sKey.channel();
//        System.out.println("3333");
        sc.close();
    }

//    public void close() throws IOException {
//        serverSocketChannel.close();
//    }
    private void close(Closeable closeable) {
        if (closeable != null) {
            try{
                closeable.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

//    public static void main(String[] args) {
//        NioHttpServer server = new NioHttpServer();
//        server.start();
//    }
}