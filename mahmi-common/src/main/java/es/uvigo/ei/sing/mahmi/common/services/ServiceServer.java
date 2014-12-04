package es.uvigo.ei.sing.mahmi.common.services;

import java.io.IOException;

public interface ServiceServer extends AutoCloseable {

    public boolean isStarted();

    public ServiceServer start() throws IOException;

    public ServiceServer stop() throws IOException;

    public default ServiceServer restart() throws IOException {
        return stop().start();
    }

    @Override
    public default void close() throws IOException {
        stop();
    }

}
