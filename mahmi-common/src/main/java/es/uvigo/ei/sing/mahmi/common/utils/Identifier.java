package es.uvigo.ei.sing.mahmi.common.utils;

import static es.uvigo.ei.sing.mahmi.common.utils.extensions.OptionExtensionMethods.getOrThrow;
import static es.uvigo.ei.sing.mahmi.common.utils.extensions.OptionExtensionMethods.optToString;
import static fj.Equal.intEqual;
import static fj.Equal.optionEqual;
import static fj.Hash.intHash;
import static fj.Hash.optionHash;
import static fj.data.Option.iif;
import static fj.data.Option.none;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import es.uvigo.ei.sing.mahmi.common.utils.annotations.VisibleForJAXB;
import fj.data.Option;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public final class Identifier {

    private final Option<Integer> id;

    @VisibleForJAXB public Identifier() {
        this(none());
    }

    public static Identifier empty() {
        return new Identifier(none());
    }

    public static Identifier of(final int value) {
        return new Identifier(iif(value >= 0, value));
    }

    public boolean isEmpty() {
        return id.isNone();
    }

    public Option<Integer> get() {
        return id;
    }

    public <E extends Throwable> int get(final E err) throws E {
        return getOrThrow(id, err);
    }

    @Override
    public boolean equals(final Object that) {
        return that instanceof Identifier
            && optionEqual(intEqual).eq(id, ((Identifier) that).id);
    }

    @Override
    public int hashCode() {
        return optionHash(intHash).hash(id);
    }

    @Override
    public String toString() {
        return optToString(id);
    }

}
