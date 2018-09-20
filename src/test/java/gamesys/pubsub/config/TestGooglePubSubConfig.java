package gamesys.pubsub.config;

import com.google.api.core.ApiService.Listener;
import com.google.api.gax.core.CredentialsProvider;
import com.google.api.gax.core.NoCredentialsProvider;
import com.google.api.gax.grpc.GrpcTransportChannel;
import com.google.api.gax.rpc.FixedTransportChannelProvider;
import com.google.api.gax.rpc.TransportChannelProvider;
import com.google.cloud.pubsub.v1.MessageReceiver;
import com.google.cloud.pubsub.v1.Publisher;
import com.google.cloud.pubsub.v1.Subscriber;
import com.google.pubsub.v1.ProjectSubscriptionName;
import com.google.pubsub.v1.ProjectTopicName;
import gamesys.pubsub.infrastructure.repository.MessageRepository;
import gamesys.pubsub.infrastructure.repository.impl.QueueMessageRepository;
import gamesys.pubsub.receiver.StringMessageReceiver;
import gamesys.pubsub.service.PublisherService;
import gamesys.pubsub.service.SubscriberService;
import gamesys.pubsub.service.impl.GoogleCloudPubSubPublisherService;
import gamesys.pubsub.service.impl.GoogleCloudPubSubSubscriberService;
import gamesys.pubsub.utils.ListenerCreator;
import gamesys.pubsub.utils.PublisherCreator;
import gamesys.pubsub.utils.SubscriberCreator;
import io.grpc.ManagedChannelBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.util.concurrent.Executors;

import static org.springframework.beans.factory.config.PropertyPlaceholderConfigurer.SYSTEM_PROPERTIES_MODE_OVERRIDE;

@PropertySource({ "classpath:application-test.properties" })
@Configuration
public class TestGooglePubSubConfig {

    private static final String HOST = System.getenv("PUBSUB_EMULATOR_HOST");

    @Value("${google.cloud.pubsub.projectId}") private String projectId;
    @Value("${google.cloud.pubsub.topicId}") private String topicId;
    @Value("${google.cloud.pubsub.subscriptionId}") private String subscriptionId;

    @Bean
    public CredentialsProvider credentialsProvider() {
        return NoCredentialsProvider.create();
    }

    @Bean
    public TransportChannelProvider transportChannelProvider() {
        return FixedTransportChannelProvider.create(
            GrpcTransportChannel.create(
                ManagedChannelBuilder.forTarget(HOST).usePlaintext().build()
            )
        );
    }

    @Bean
    public PublisherService publisherService(Publisher publisher) {
        return new GoogleCloudPubSubPublisherService(publisher);
    }

    @Bean
    public Publisher publisher(CredentialsProvider credentialsProvider, TransportChannelProvider channelProvider) {
        return PublisherCreator.create(
            ProjectTopicName.of(projectId, topicId),
            credentialsProvider,
            channelProvider
        );
    }

    @Bean
    public SubscriberService subscriberService(Subscriber subscriber, MessageRepository<String> messages) {
        return new GoogleCloudPubSubSubscriberService(subscriber, messages);
    }

    @Bean
    public Subscriber subscriber(CredentialsProvider credentialsProvider, TransportChannelProvider channelProvider, MessageReceiver messageReceiver, Listener listener) {
        return SubscriberCreator.create(
            ProjectSubscriptionName.of(projectId, subscriptionId),
            credentialsProvider,
            channelProvider,
            messageReceiver,
            listener,
            Executors.newCachedThreadPool()
        );
    }

    @Bean
    public Listener listener() {
        return ListenerCreator.create();
    }

    @Bean
    public MessageReceiver messageReceiver (MessageRepository<String> messages) {
        return new StringMessageReceiver(messages);
    }

    @Bean
    public MessageRepository<String> messageRepository() {
        return new QueueMessageRepository();
    }

    private PropertyPlaceholderConfigurer propertyPlaceholderConfigurer() {
        PropertyPlaceholderConfigurer configurer = new PropertyPlaceholderConfigurer();
        configurer.setSystemPropertiesMode(SYSTEM_PROPERTIES_MODE_OVERRIDE);
        return configurer;
    }
}