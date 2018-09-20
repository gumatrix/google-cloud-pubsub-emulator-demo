package gamesys.pubsub.service.impl;

import com.google.cloud.pubsub.v1.Subscriber;
import gamesys.pubsub.infrastructure.repository.MessageRepository;
import gamesys.pubsub.service.SubscriberService;
import lombok.SneakyThrows;

import java.util.ArrayList;
import java.util.List;

public class GoogleCloudPubSubSubscriberService implements SubscriberService<String> {

    private final Subscriber subscriber;
    private final MessageRepository<String> messages;

    public GoogleCloudPubSubSubscriberService(Subscriber subscriber, MessageRepository<String> messages) {
        this.subscriber = subscriber;
        this.messages = messages;

        subscriber.startAsync();
    }

    @Override
    public List<String> pull() {
        return new ArrayList<>(messages.getAll());
    }

    @SneakyThrows
    @Override
    protected void finalize() {
        subscriber.awaitTerminated();
        subscriber.stopAsync();
    }
}