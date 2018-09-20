package gamesys.pubsub.infrastructure.repository.impl;

import gamesys.pubsub.infrastructure.repository.MessageRepository;

import java.util.List;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.Collectors;

import static java.util.stream.Stream.generate;

public class QueueMessageRepository implements MessageRepository<String> {

    private final Queue<String> messages;

    public QueueMessageRepository() {
        this.messages = new LinkedBlockingQueue<>();
    }

    @Override
    public void add(String message) {
        messages.add(message);
    }

    @Override
    public List<String> getAll() {
        return generate(messages::poll)
            .limit(messages.size())
            .collect(Collectors.toList());
    }
}