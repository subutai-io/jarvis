package org.safehaus.analysis;


import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import akka.actor.Stash;
import org.safehaus.analysis.ConfluenceMetricInfo.ConfluenceMetricInfoInternal;
import org.safehaus.confluence.model.ConfluenceMetric;
import org.safehaus.model.User;
import org.safehaus.stash.model.StashMetricIssue;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.api.java.function.VoidFunction;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaPairDStream;
import org.apache.spark.streaming.api.java.JavaPairInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.kafka.KafkaUtils;

import com.google.common.base.Optional;

import kafka.serializer.StringDecoder;
import scala.Tuple2;
import scala.Tuple3;
import scala.Tuple4;

import static com.datastax.spark.connector.japi.CassandraJavaUtil.mapToRow;
import static com.datastax.spark.connector.japi.CassandraStreamingJavaUtil.javaFunctions;

import org.safehaus.analysis.StashUserMetricInfo.StashUserMetricInfoInternal;
import org.safehaus.analysis.UserMetricInfo.UserMetricInfoInternal;



/**
 * Created by neslihan on 07.08.2015.
 */
public class SparkDirectKafkaStreamSuite implements Serializable
{
    private static String brokerList = new String( "localhost:9092" );
    private static String sparkMaster = new String( "local[3]" );
    private static String appName = new String( "JarvisStreamConsumer" );

    public SparkDirectKafkaStreamSuite()
    {
    }

    public static void startStreams()
    {
        JavaStreamingContext jssc;
        SparkConf conf;
        HashSet<String> topicsSet;
        HashMap<String, String> kafkaParams;

        topicsSet = new HashSet<String>();

        kafkaParams = new HashMap<String, String>();
        kafkaParams.put( "metadata.broker.list", brokerList );
        kafkaParams.put("auto.offset.reset", "smallest");

        conf = new SparkConf().setAppName( appName );
        conf.setMaster( sparkMaster ).set("spark.cassandra.connection.host", "localhost").set("spark.driver.allowMultipleContexts", "true");
        jssc = new JavaStreamingContext( conf, Durations.seconds( 60 ) );

        //Create the stream for jira-logs
        topicsSet.add(JiraMetricIssueKafkaProducer.getTopic());

        JiraStreamFunctions.startJiraStream(jssc, topicsSet, kafkaParams);

        //Create the stream for stash-logs
        topicsSet.clear();
        topicsSet.add(StashMetricIssueKafkaProducer.getTopic());

        StashStreamFunctions.startStashStreaming(jssc, topicsSet, kafkaParams);

        //Create the stream for confluence-logs
        topicsSet.clear();
        topicsSet.add(ConfluenceMetricKafkaProducer.getTopic());

        ConfluenceStreamFunctions.startConfluenceStreaming(jssc, topicsSet, kafkaParams);

        jssc.checkpoint( "/tmp/spark_checkpoint_dir/" );
        jssc.start();
        jssc.awaitTermination();
    }
}
