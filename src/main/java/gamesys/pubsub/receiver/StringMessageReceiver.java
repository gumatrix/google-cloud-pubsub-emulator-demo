package gamesys.pubsub.receiver;

import com.google.cloud.pubsub.v1.AckReplyConsumer;
import com.google.cloud.pubsub.v1.MessageReceiver;
import com.google.pubsub.v1.PubsubMessage;
import gamesys.pubsub.infrastructure.repository.MessageRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class StringMessageReceiver implements MessageReceiver {

    private final MessageRepository<String> messages;

    @Override
    public void receiveMessage(PubsubMessage message, AckReplyConsumer consumer) {
        messages.add(message.getData().toStringUtf8());
        consumer.ack();
    }
}