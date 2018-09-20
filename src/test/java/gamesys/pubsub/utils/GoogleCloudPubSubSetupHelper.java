package gamesys.pubsub.utils;

import com.google.api.gax.core.CredentialsProvider;
import com.google.api.gax.rpc.ApiException;
import com.google.api.gax.rpc.TransportChannelProvider;
import com.google.cloud.pubsub.v1.SubscriptionAdminClient;
import com.google.cloud.pubsub.v1.SubscriptionAdminSettings;
import com.google.cloud.pubsub.v1.TopicAdminClient;
import com.google.cloud.pubsub.v1.TopicAdminSettings;
import com.google.pubsub.v1.ProjectSubscriptionName;
import com.google.pubsub.v1.ProjectTopicName;
import lombok.SneakyThrows;

import static com.google.pubsub.v1.PushConfig.getDefaultInstance;
import static java.util.concurrent.TimeUnit.SECONDS;

public class GoogleCloudPubSubSetupHelper {

    private static final int ACK_DEADLINE_SECONDS = 10;

    private final String projectId;
    private final TopicAdminSettings topicAdminSettings;
    private final SubscriptionAdminSettings subscriptionAdminSettings;
    private final TransportChannelProvider channelProvider;

    @SneakyThrows
    private GoogleCloudPubSubSetupHelper(CredentialsProvider credentialsProvider, TransportChannelProvider channelProvider, String projectId) {
        this.projectId = projectId;
        this.channelProvider = channelProvider;

        this.topicAdminSettings = TopicAdminSettings.newBuilder()
            .setTransportChannelProvider(channelProvider)
            .setCredentialsProvider(credentialsProvider)
            .build();

        this.subscriptionAdminSettings = SubscriptionAdminSettings.newBuilder()
            .setTransportChannelProvider(channelProvider)
            .setCredentialsProvider(credentialsProvider)
            .build();
    }

    @SneakyThrows
    public GoogleCloudPubSubSetupHelper createTopic(String name) {
        ProjectTopicName topic = ProjectTopicName.of(projectId, name);

        try (TopicAdminClient topicAdminClient = TopicAdminClient.create(topicAdminSettings)) {
            topicAdminClient.createTopic(topic);
        } catch (ApiException e) {
            System.out.println(e.getStatusCode().getCode());
            System.out.println(e.isRetryable());
        }

        System.out.printf("Topic %s:%s created.\n", topic.getProject(), topic.getTopic());

        return this;
    }

    @SneakyThrows
    public GoogleCloudPubSubSetupHelper createSubscription(String subscriptionName, String topicName) {
        ProjectSubscriptionName subscription = ProjectSubscriptionName.of(projectId, subscriptionName);
        ProjectTopicName topic = ProjectTopicName.of(projectId, topicName);

        try (SubscriptionAdminClient subscriptionAdminClient = SubscriptionAdminClient.create(subscriptionAdminSettings)) {
            subscriptionAdminClient.createSubscription(subscription, topic, getDefaultInstance(), ACK_DEADLINE_SECONDS);
        } catch (ApiException e) {
            System.out.println(e.getStatusCode().getCode());
            System.out.println(e.isRetryable());
        }

        System.out.printf("Subscription %s:%s created.\n", subscription.getProject(), subscription.getSubscription());

        return this;
    }

    public static GoogleCloudPubSubSetupHelper getInstance(CredentialsProvider credentialsProvider, TransportChannelProvider channelProvider, String projectId) {
        return new GoogleCloudPubSubSetupHelper(credentialsProvider, channelProvider, projectId);
    }

    @SneakyThrows
    @Override
    protected void finalize() {
        channelProvider.getTransportChannel().awaitTermination(30, SECONDS);
        channelProvider.getTransportChannel().shutdown();
    }
}