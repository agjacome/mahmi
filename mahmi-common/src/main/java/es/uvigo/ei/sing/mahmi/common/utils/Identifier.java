package es.uvigo.ei.sing.mahmi.common.utils;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import fj.Equal;
import fj.Hash;
import fj.Ord;
import fj.data.Natural;
import fj.data.Option;

import es.uvigo.ei.sing.mahmi.common.utils.annotations.VisibleForJAXB;

import static fj.Equal.longEqual;
import static fj.Equal.optionEqual;
import static fj.Hash.longHash;
import static fj.Hash.optionHash;
import static fj.Ord.longOrd;
import static fj.Ord.optionOrd;
import static fj.data.Natural.natural;
import static fj.data.Option.none;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public final class Identifier {

    public static final Hash<Identifier> hash =
        optionHash(longHash.comap(Natural::longValue)).comap(Identifier::getValue);

    public static final Equal<Identifier> equal =
        optionEqual(longEqual.comap(Natural::longValue)).comap(Identifier::getValue);

    public static final Ord<Identifier> ord =
        optionOrd(longOrd.comap(Natural::longValue)).comap(Identifier::getValue);

    private final Option<Natural> value;

    @VisibleForJAXB
    public Identifier() {
        this(none());
    }

    public static Identifier empty() {
        return new Identifier(none());
    }

    public static Identifier of(final long value) {
        return new Identifier(natural(value));
    }

    public boolean isEmpty() {
        return value.isNone();
    }

}
