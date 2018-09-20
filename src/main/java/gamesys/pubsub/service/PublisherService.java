package gamesys.pubsub.service;

public interface PublisherService<S> {

    void publish(S message);
}