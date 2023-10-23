package consumer;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;
import java.util.Random;
import java.util.concurrent.ExecutionException;

public class SimpleExampleProducer {

    private static final String OUR_BOOTSTRAP_SERVERS = ":9092";
    private static final String OUR_CLIENT_ID = "Producer";
    private final KafkaProducer<String, String> producer;

    public SimpleExampleProducer(){
        producer = new KafkaProducer<>(buildProducerPropsMap());
    }
    private Properties buildProducerPropsMap(){
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, OUR_BOOTSTRAP_SERVERS);
        props.put(ProducerConfig.CLIENT_ID_CONFIG, OUR_CLIENT_ID);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        props.put(ProducerConfig.BATCH_SIZE_CONFIG,16384);
        props.put(ProducerConfig.BUFFER_MEMORY_CONFIG,1048576);

        return props;
    }

    public void send(String topic, String message){
        final int number = new Random().nextInt(10);
        ProducerRecord<String, String> data = new ProducerRecord<>(topic, message);
        try {
            RecordMetadata meta = producer.send(data).get();

        }catch (InterruptedException | ExecutionException e){
            producer.flush();
        }
    }
}
