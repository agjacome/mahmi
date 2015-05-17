package es.uvigo.ei.sing.mahmi.common.utils.extensions;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;

import es.uvigo.ei.sing.mahmi.common.utils.annotations.DisallowConstruction;

public final class FutureUtils {

    @DisallowConstruction
    private FutureUtils() { }

    public static <A> CompletableFuture<Void> combineFutures(
        final Collection<CompletableFuture<A>> futures
    ) {
        final CompletableFuture<?>[ ] fs = futures.toArray(
            new CompletableFuture<?>[futures.size()]
        );

        return CompletableFuture.allOf(fs);
    }

}
