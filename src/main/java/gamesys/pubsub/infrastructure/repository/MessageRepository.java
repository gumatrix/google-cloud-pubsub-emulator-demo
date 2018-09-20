package gamesys.pubsub.infrastructure.repository;

import java.util.List;

public interface MessageRepository<T> {

    void add(T message);

    List<T> getAll();
}