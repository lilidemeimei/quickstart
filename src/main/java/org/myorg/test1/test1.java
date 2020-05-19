package org.myorg.test1;

import org.apache.flink.table.api.EnvironmentSettings;

import net.qihoo.ads.flink.env.scala.FlinkStreamingEnv;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

public class test1 {
    public static void main(String[] args) {

//        StreamExecutionEnvironment env = (StreamExecutionEnvironment) FlinkStreamingEnv.init(args, "max-report.properties");
//        ParameterTool conf = ParameterTool.fromMap(env.getConfig().getGlobalJobParameters().toMap());
//        EnvironmentSettings settings = EnvironmentSettings.newInstance().useOldPlanner().inStreamingMode().build();
//        new CSRAvro()
//                .registryUrl()
//                .registrySubject(subject)
//                .avroSchema(avroSchemaStr);

    }
}
