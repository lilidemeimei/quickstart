package org.myorg.quickstart;

import org.apache.flink.api.java.tuple.Tuple4;

public class KafkaMsg extends Tuple4<String, Integer, Long, byte[]> {

    public KafkaMsg() {
        super();
    }

    public KafkaMsg(String topic, Integer partition, Long offset, byte[] message) {
        super(topic, partition, offset, message);
    }

    public String getTopic() {
        return f0;
    }

    public void setTopic(String topic) {
        this.f0 = topic;
    }

    public int getPartition() {
        return f1;
    }

    public void setPartition(int partition) {
        this.f1 = partition;
    }

    public long getOffset() {
        return f2;
    }

    public void setOffset(long offset) {
        this.f2 = offset;
    }

    public byte[] getMessage() {
        return f3;
    }

    public void setMessage(byte[] message) {
        this.f3 = message;
    }
}
