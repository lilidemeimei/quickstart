package org.myorg.heyenio;

public class Test {
    public static void main(String[] args) throws Exception {
        Server server = new Server(10);
        server.Init();
        server.run();
    }
}
