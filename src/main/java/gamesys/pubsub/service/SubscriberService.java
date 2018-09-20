package gamesys.pubsub.service;

import java.util.List;

public interface SubscriberService<T> {

    List<T> pull();
}