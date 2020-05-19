package org.myorg.heyenio;


import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public class SayHiServer implements NIOServer {
    private int bufferSize;
    private static String sendMsg = "Hi~";
    private static int len = sendMsg.length();

    SayHiServer(int bufferSize) {
        this.bufferSize = bufferSize;
    }

    @Override
    public void AcceptHandle(SelectionKey key) throws IOException {
        ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();
        SocketChannel clientChannel = serverSocketChannel.accept();
        clientChannel.configureBlocking(false);
        clientChannel.register(key.selector(), SelectionKey.OP_READ);
    }

    @Override
    public void ReadHandle(SelectionKey key) throws IOException {
        SocketChannel clientChannel = (SocketChannel) key.channel();
        ByteBuffer readBuffer = ByteBuffer.allocate(bufferSize);
        int readBytes = clientChannel.read(readBuffer);
        if (readBytes == -1) {
            clientChannel.close();
        } else if (readBytes > 0) {
            //readBuffer.flip();
            //byte[] recvBytes = new byte[readBuffer.remaining()];
            //readBuffer.get(recvBytes);
            //String msg = new String(recvBytes, "utf-8");
            key.interestOps(SelectionKey.OP_WRITE);
            //clientChannel.close();
        }
    }

    @Override
    public void WriteHandle(SelectionKey key) throws IOException {
        int sendBytes = 0;
        SocketChannel clientChannel = (SocketChannel) key.channel();
        while (sendBytes < len) {
            sendBytes += clientChannel.write(ByteBuffer.wrap(sendMsg.getBytes()));
        }
        clientChannel.close();
    }
}
