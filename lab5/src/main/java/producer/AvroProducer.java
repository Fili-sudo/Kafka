package producer;

import io.confluent.kafka.serializers.KafkaJsonSerializer;
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;
import java.util.concurrent.ExecutionException;

public class AvroProducer {
    private static final Logger LOG = LoggerFactory.getLogger(AvroProducer.class);

    private static final String OUR_BOOTSTRAP_SERVERS = ":9092";
  // private static final String OUR_BOOTSTRAP_SERVERS = "localhost:9092, localhost:9093, localhost:9094";
    private static final String OUR_CLIENT_ID = "firstProducer";

    //private static Producer<String, Company> producer;
    private static Producer<String, AvroCompany> producer;

    public static Properties buildProducerPropsMap(){
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, OUR_BOOTSTRAP_SERVERS);
        props.put(ProducerConfig.CLIENT_ID_CONFIG, OUR_CLIENT_ID);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,  KafkaJsonSerializer.class.getName());
        return props;
    }
    public static void main(String[] args){

        producer = new KafkaProducer<>(buildProducerPropsMap());

        // create Company item for topic events2 with key
        Company data1 = new Company("INtel", 2, "Intel industries");
        AvroCompany data = new AvroCompany( 2, "Intel industries");

        // send messages(records) synchronous
        send("events2", data);

        producer.close();
    }

    public static void send(String topic, AvroCompany comp){
        ProducerRecord<String, AvroCompany> data = new ProducerRecord<>(topic, comp);
        try {
            RecordMetadata meta = producer.send(data).get();
            LOG.info("key = {}, value = {} ==> partition = {}, offset = {}", data.key(), data.value(), meta.partition(), meta.offset());
        }catch (InterruptedException | ExecutionException e){
            producer.flush();
        }
    }
}