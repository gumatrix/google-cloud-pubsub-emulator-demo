package gamesys.pubsub.service.impl;

import com.google.cloud.pubsub.v1.Publisher;
import gamesys.pubsub.service.PublisherService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import static com.google.protobuf.ByteString.copyFromUtf8;
import static com.google.pubsub.v1.PubsubMessage.newBuilder;

@RequiredArgsConstructor
public class GoogleCloudPubSubPublisherService implements PublisherService<String> {

    private final Publisher publisher;

    @Override
    public void publish(String message) {
        publisher.publish(newBuilder().setData(copyFromUtf8(message)).build());
    }

    @SneakyThrows
    @Override
    protected void finalize() {
        publisher.shutdown();
    }
}