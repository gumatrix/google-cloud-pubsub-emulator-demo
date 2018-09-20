package gamesys.pubsub.utils;

import com.google.api.gax.core.CredentialsProvider;
import com.google.api.gax.rpc.TransportChannelProvider;
import com.google.cloud.pubsub.v1.Publisher;
import com.google.pubsub.v1.ProjectTopicName;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public class PublisherCreator {

    @SneakyThrows
    public static Publisher create(ProjectTopicName topic, CredentialsProvider credentialsProvider, TransportChannelProvider channelProvider) {
        return Publisher.newBuilder(topic)
            .setCredentialsProvider(credentialsProvider)
            .setChannelProvider(channelProvider)
            .build();
    }
}