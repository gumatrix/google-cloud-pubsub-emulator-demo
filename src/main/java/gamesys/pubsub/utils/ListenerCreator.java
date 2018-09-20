package gamesys.pubsub.utils;

import com.google.api.core.ApiService;
import com.google.api.core.ApiService.Listener;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public class ListenerCreator {

    public static Listener create() {
        return new Listener() {

            @Override
            public void running() {
                super.running();
            }

            @Override
            public void failed(ApiService.State from, Throwable failure) {
                synchronized (this) {
                    notifyAll();
                }

                super.failed(from, failure);
            }
        };
    }
}