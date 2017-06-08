package es.uvigo.ei.sing.mahmi.common.entities.compounds;

import java.util.EnumSet;

import fj.data.HashMap;
import fj.data.Option;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.val;

import es.uvigo.ei.sing.mahmi.common.utils.annotations.VisibleForTesting;

import static fj.Equal.charEqual;
import static fj.Hash.charHash;
import static fj.P.p;
import static fj.data.Stream.iterableStream;
import static fj.function.Characters.toLowerCase;

/**
 * {@linkplain Nucleobase} is a enum that represents an nucleo base {@link Compound}
 * 
 * @author Alberto Gutierrez-Jacome
 * 
 * @see Compound
 *
 */
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum Nucleobase implements Compound {

    A("Adenine" , 'A'),
    C("Cytosine", 'C'),
    G("Guanine" , 'G'),
    T("Thymine" , 'T'),
    U("Uracil"  , 'U'),
    N("Unknown" , 'N');

    @VisibleForTesting
    static final HashMap<Character, Nucleobase> codes;

    static {
        val all = iterableStream(EnumSet.allOf(Nucleobase.class));
        codes = HashMap.from(
            all.map(nb -> p(nb.code, nb)),
            charEqual.contramap(toLowerCase),
            charHash.contramap(toLowerCase)
        );
    }

    /**
     * The full name of the nucleo base
     */
    private final String fullName;
    
    /**
     * The code of the nucleo base
     */
    private final char   code;

    /**
     * Gets an nucleo base from code
     * 
     * @param code The code of the nucleo base
     * @return The nucleo base as an {@code Option}
     */
    public static Option<Nucleobase> fromCode(final char code) {
        return codes.get(code);
    }

}
