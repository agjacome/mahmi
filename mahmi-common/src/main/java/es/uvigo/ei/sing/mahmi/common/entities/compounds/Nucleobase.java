package es.uvigo.ei.sing.mahmi.common.entities.compounds;

import static es.uvigo.ei.sing.mahmi.common.utils.extensions.CollectionsExtensionMethods.mapKeys;
import static es.uvigo.ei.sing.mahmi.common.utils.extensions.CollectionsExtensionMethods.setToIdentityMap;
import static java.lang.Character.toLowerCase;
import static java.util.Collections.unmodifiableMap;
import static java.util.EnumSet.allOf;

import java.util.Map;

import lombok.Getter;
import fj.data.Stream;

@Getter
public enum Nucleobase implements ChemicalCompound {

    A("Adenine"  , 'A'),
    C("Cytosine" , 'C'),
    G("Guanine"  , 'G'),
    T("Thymine"  , 'T'),
    U("Uracil"   , 'U'),

    N("Unknown"  , 'N');

    public static final Map<Character, Nucleobase> codeMapper =
        unmodifiableMap(mapKeys(setToIdentityMap(allOf(Nucleobase.class)), aa -> toLowerCase(aa.code)));

    private final char   code;
    private final String fullName;

    private Nucleobase(final String fullName, final char code) {
        this.fullName = fullName;
        this.code     = code;
    }

    public static Nucleobase fromCode(final char code) {
        return codeMapper.getOrDefault(code, N);
    }

    @Override
    public Iterable<Character> getShortName() {
        return Stream.single(code);
    }

    @Override
    public String toString() {
        return fullName;
    }

}

