package gamesys.pubsub.utils;

import com.google.api.core.ApiService.Listener;
import com.google.api.gax.core.CredentialsProvider;
import com.google.api.gax.rpc.TransportChannelProvider;
import com.google.cloud.pubsub.v1.MessageReceiver;
import com.google.cloud.pubsub.v1.Subscriber;
import com.google.pubsub.v1.ProjectSubscriptionName;
import lombok.NoArgsConstructor;

import java.util.concurrent.ExecutorService;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public class SubscriberCreator {

    public static Subscriber create(ProjectSubscriptionName subscription, CredentialsProvider credentialsProvider, TransportChannelProvider channelProvider, MessageReceiver messageReceiver, Listener listener, ExecutorService executorService) {
        Subscriber subscriber = Subscriber.newBuilder(subscription, messageReceiver)
            .setCredentialsProvider(credentialsProvider)
            .setChannelProvider(channelProvider)
            .build();

        subscriber.addListener(listener, executorService);

        return subscriber;
    }
}