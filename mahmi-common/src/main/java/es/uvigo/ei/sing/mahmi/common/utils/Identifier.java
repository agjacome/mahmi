package es.uvigo.ei.sing.mahmi.common.utils;

import java.util.Optional;

public final class Identifier {

    private final Optional<Long> value;

    public Identifier() {
        this.value = Optional.empty();
    }

    public Identifier(final long value) {
        this.value = Optional.of(value);
    }

    public static Identifier empty() {
        return new Identifier();
    }

    public static Identifier of(final long value) {
        return new Identifier(value);
    }

    public boolean isEmpty() {
        return !value.isPresent();
    }

    public Optional<Long> get() {
        return value;
    }

    public long unsafeGet() {
        return value.orElseThrow(() -> new Error(
            "Unsafe Operation failed: unsafeGet() on empty Identifier"
        ));
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public boolean equals(final Object that) {
        return that instanceof Identifier
            && this.value.equals(((Identifier) that).value);
    }

}
