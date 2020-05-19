package org.myorg.nio1;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class NioClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(NioClient.class);
    private SocketChannel socketChannel;
    private String hostname;
    private int port;

    public NioClient() {
//        Properties properties = new Properties();
//        try {
//            properties.load(Resources.getResource("config.properties").openStream());
//        } catch (Exception e) {
//            throw new RuntimeException("Load config fail.", e);
//        }
//        this.hostname = properties.getProperty("hostname");
//        this.port = Integer.parseInt(properties.getProperty("port"));
//        System.out.println(this.hostname);
        this.hostname = "127.0.0.1";
        this.port = 8000;
    }

    public void open() throws IOException {
        socketChannel = SocketChannel.open(new InetSocketAddress(this.hostname, this.port));
        socketChannel.configureBlocking(false);
//        socketChannel.connect(new InetSocketAddress(this.hostname, this.port));
        if (socketChannel.isConnected()) {
            LOGGER.info("connect success to " + this.hostname + ":" + this.port);
        } else {
            LOGGER.info("connect fail");
        }
    }

    public void close() throws IOException {
        socketChannel.close();
        LOGGER.info("connect closed");
    }

    public void send(String data) throws IOException {
        ByteBuffer buf = ByteBuffer.allocate(1000);
        buf.clear();
        buf.put(data.getBytes());
//        System.out.println("~~~~"+buf);
        buf.flip();
        socketChannel.write(buf);
    }

    public void recieve() throws IOException {
        ByteBuffer buf = ByteBuffer.allocate(1000);
        int byteRead = socketChannel.read(buf);
        //等于-1表示读到流的末尾，连接关闭
        while (byteRead != -1) {
            buf.flip();
            while (buf.hasRemaining()) {
                System.out.println((char) buf.get());
            }
            buf.clear();
            byteRead = socketChannel.read(buf);
        }
        System.out.println();
    }

    public static void main(String []args) throws InterruptedException {
        try {
            NioClient nioClient = new NioClient();
            nioClient.open();
            nioClient.send("lele");
            nioClient.close();
//            Thread.sleep(5000);
//            nioClient.recieve();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
