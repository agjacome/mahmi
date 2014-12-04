package es.uvigo.ei.sing.mahmi.common.entities;

import static es.uvigo.ei.sing.mahmi.common.utils.extensions.OptionExtensionMethods.optToString;
import static fj.Equal.intEqual;
import static fj.Equal.optionEqual;
import static fj.Hash.intHash;
import static fj.Hash.optionHash;
import static fj.data.Option.none;
import static fj.data.Option.some;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import es.uvigo.ei.sing.mahmi.common.utils.annotations.VisibleForJAXB;
import fj.data.Option;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public final class Identifier {

    private final Option<Integer> value;

    @VisibleForJAXB
    public Identifier() {
        this(none());
    }

    public static Identifier empty() {
        return new Identifier(none());
    }

    public static Identifier of(final int value) {
        return new Identifier(some(value));
    }

    @Override
    public boolean equals(final Object that) {
        return that instanceof Identifier
            && optionEqual(intEqual).eq(value, ((Identifier) that).value);
    }

    @Override
    public int hashCode() {
        return optionHash(intHash).hash(value);
    }

    @Override
    public String toString() {
        return optToString(value, "undefined ID");
    }

}
