package org.myorg.heyenio;

import java.io.IOException;
import java.nio.channels.SelectionKey;

public interface NIOServer {
    void AcceptHandle(SelectionKey key) throws IOException;

    void ReadHandle(SelectionKey key) throws IOException;

    void WriteHandle(SelectionKey key) throws IOException;
}
