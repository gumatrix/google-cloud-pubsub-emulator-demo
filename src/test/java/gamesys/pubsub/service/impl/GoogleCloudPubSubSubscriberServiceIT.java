package gamesys.pubsub.service.impl;

import com.google.api.core.ApiService.Listener;
import com.google.api.gax.core.CredentialsProvider;
import com.google.api.gax.rpc.TransportChannelProvider;
import gamesys.pubsub.config.TestGooglePubSubConfig;
import gamesys.pubsub.service.PublisherService;
import gamesys.pubsub.service.SubscriberService;
import gamesys.pubsub.utils.GoogleCloudPubSubSetupHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.assertLinesMatch;

@SuppressWarnings("all")
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { TestGooglePubSubConfig.class })
class GoogleCloudPubSubSubscriberServiceIT {

    @Autowired private PublisherService publisherService;
    @Autowired private SubscriberService classUnderTest;
    @Autowired private CredentialsProvider credentialsProvider;
    @Autowired private TransportChannelProvider channelProvider;
    @Autowired private Listener listener;

    @Value("${google.cloud.pubsub.projectId}") private String projectId;
    @Value("${google.cloud.pubsub.topicId}") private String topicId;
    @Value("${google.cloud.pubsub.subscriptionId}") private String subscriptionId;

    private boolean initialised = false;

    @BeforeEach
    void setUp() {
        if (!initialised) {
            GoogleCloudPubSubSetupHelper.getInstance(credentialsProvider, channelProvider, projectId)
                .createTopic(topicId)
                .createSubscription(subscriptionId, topicId);

            initialised = true;
        }
    }

    @Test
    void should_return_an_empty_list() {
        assertLinesMatch(emptyList(), classUnderTest.pull());
    }

    @Test
    void should_return_a_single_message() throws Exception {
        String message = "Hello World";
        List<String> actual = new ArrayList<>();

        publisherService.publish(message);

        synchronized (listener) {
            listener.wait(1000);
        }

        assertLinesMatch(singletonList(message), classUnderTest.pull());
    }
}