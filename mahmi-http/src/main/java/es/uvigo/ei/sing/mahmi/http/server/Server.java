package es.uvigo.ei.sing.mahmi.http.server;

import static java.lang.Runtime.getRuntime;

import java.io.IOException;

public interface Server {

    public void start() throws IOException;

    public void stop();

    public default void addStopOnShutdownHook() {
        getRuntime().addShutdownHook(new Thread(() -> stop()));
    }

}
