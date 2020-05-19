package org.myorg.heyenio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

public class Server {
    private AtomicBoolean running;
    private long timeOut;
    private long count;
    Selector selector = null;

    public Server(long timeOut) {
        this.timeOut = timeOut;
    }

    public void Init() throws IOException {
        running = new AtomicBoolean(true);
        selector = Selector.open();
    }

    public void run() throws IOException {
        ServerSocketChannel acceptChannel = ServerSocketChannel.open();
        acceptChannel.configureBlocking(false);
        acceptChannel.bind(new InetSocketAddress(8080));
        acceptChannel.register(selector, SelectionKey.OP_ACCEPT);
        SayHiServer sayHiServer = new SayHiServer(16);
        while (true) {
            if (selector.select() == 0) {
                continue;
            }
            Set<SelectionKey> keys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = keys.iterator();
            while (iterator.hasNext()) {
                SelectionKey key = iterator.next();
                if (key.isAcceptable()) {
                    sayHiServer.AcceptHandle(key);
                } else if (key.isReadable()) {
                    sayHiServer.ReadHandle(key);
                } else if (key.isValid() && key.isWritable()) {
                    sayHiServer.WriteHandle(key);
                }
                iterator.remove();
            }
        }
    }

    public void close() {
        running.set(false);
    }
}
