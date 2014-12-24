package es.uvigo.ei.sing.mahmi.common.utils.extensions;

import static java.util.concurrent.CompletableFuture.allOf;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class FutureExtensionMethods {

    public static CompletableFuture<Void> sequenceFutures(
        final Collection<CompletableFuture<Void>> futures
    ) {
        return allOf(futures.toArray(new CompletableFuture<?>[futures.size()]));
    }

}
