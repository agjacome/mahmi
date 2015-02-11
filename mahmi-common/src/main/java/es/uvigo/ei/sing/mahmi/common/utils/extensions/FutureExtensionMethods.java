package es.uvigo.ei.sing.mahmi.common.utils.extensions;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class FutureExtensionMethods {

    public static CompletableFuture<Void> sequence(
        final Collection<CompletableFuture<Void>> futures
    ) {
        return CompletableFuture.allOf(
            futures.toArray(new CompletableFuture<?>[futures.size()])
        );
    }

}
