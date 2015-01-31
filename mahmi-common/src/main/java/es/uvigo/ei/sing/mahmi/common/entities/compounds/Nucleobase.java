package es.uvigo.ei.sing.mahmi.common.entities.compounds;

import static fj.Equal.charEqual;
import static fj.Hash.charHash;
import static fj.P.p;
import static fj.data.Stream.iterableStream;
import static fj.function.Characters.toLowerCase;

import java.util.EnumSet;

import lombok.Getter;
import lombok.val;
import fj.data.HashMap;
import fj.data.Option;

@Getter
public enum Nucleobase implements Compound {

    A("Adenine" , 'A'),
    C("Cytosine", 'C'),
    G("Guanine" , 'G'),
    T("Thymine" , 'T'),
    U("Uracil"  , 'U'),
    N("Unknown" , 'N');

    private static final HashMap<Character, Nucleobase> codes;

    static {
        val all = iterableStream(EnumSet.allOf(Nucleobase.class));
        codes = HashMap.from(
            all.map(nb -> p(nb.code, nb)),
            charEqual.comap(toLowerCase),
            charHash.comap(toLowerCase)
        );
    }

    private final char   code;
    private final String fullName;

    private Nucleobase(final String fullName, final char code) {
        this.code      = code;
        this.fullName  = fullName;
    }

    public static Option<Nucleobase> fromCode(final char code) {
        return codes.get(code);
    }

}
