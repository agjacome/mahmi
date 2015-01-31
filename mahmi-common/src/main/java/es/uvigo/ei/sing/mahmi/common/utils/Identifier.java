package es.uvigo.ei.sing.mahmi.common.utils;

import static fj.data.Natural.natural;
import static fj.data.Option.none;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Value;
import es.uvigo.ei.sing.mahmi.common.utils.annotations.VisibleForJAXB;
import fj.data.Natural;
import fj.data.Option;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Value public final class Identifier {

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
