package es.uvigo.ei.sing.mahmi.http.server;

import java.io.IOException;

import static java.lang.Runtime.getRuntime;

public interface Server {

    public void start() throws IOException;

    public void stop();

    public default void addStopOnShutdownHook() {
        getRuntime().addShutdownHook(new Thread(() -> stop()));
    }

}
