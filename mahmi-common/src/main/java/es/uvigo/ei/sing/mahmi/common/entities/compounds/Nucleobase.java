package es.uvigo.ei.sing.mahmi.common.entities.compounds;

import static es.uvigo.ei.sing.mahmi.common.utils.Contracts.require;
import static es.uvigo.ei.sing.mahmi.common.utils.extensions.CollectionExtensionMethods.enumToMap;
import static es.uvigo.ei.sing.mahmi.common.utils.extensions.CollectionExtensionMethods.transformKeys;
import static fj.Function.identity;

import java.util.Map;

import lombok.Getter;
import fj.data.List;

@Getter
public enum Nucleobase implements ChemicalCompound {

    A("Adenine" , 'A'),
    C("Cytosine", 'C'),
    G("Guanine" , 'G'),
    T("Thymine" , 'T'),
    U("Uracil"  , 'U'),
    N("Unknown" , 'N');


    private static final Map<Character, Nucleobase> codes;

    static {
        // both lowercase and uppercase char codes
        codes = enumToMap(Nucleobase.class, Nucleobase::getCode, identity());
        codes.putAll(transformKeys(codes, Character::toLowerCase));
    }


    private final char            code;
    private final String          fullName;
    private final List<Character> shortName;

    private Nucleobase(final String fullName, final char code) {
        this.code      = code;
        this.fullName  = fullName;
        this.shortName = List.single(code);
    }

    public static Nucleobase fromCode(final char code) {
        require(codes.containsKey(code), "Invalid nucleobase code %c", code);
        return codes.get(code);
    }

}

