package es.uvigo.ei.sing.mahmi.common.utils.extensions;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;

import es.uvigo.ei.sing.mahmi.common.utils.annotations.DisallowConstruction;

public final class FutureUtils {

    @DisallowConstruction
    private FutureUtils() { }

    public static <A> CompletableFuture<Void> sequenceFutures(
        final Collection<CompletableFuture<A>> futures
    ) {
        return CompletableFuture.allOf(
            futures.toArray(new CompletableFuture<?>[futures.size()])
        );
    }

}
