package es.uvigo.ei.sing.mahmi.common.entities.compounds;

import fj.Equal;
import fj.Hash;

import static fj.Equal.charEqual;
import static fj.Hash.charHash;
import static fj.function.Characters.toUpperCase;

public interface Compound {

    public static final Hash<Compound>  hash  = charHash.comap(toUpperCase).comap(Compound::getCode);
    public static final Equal<Compound> equal = charEqual.comap(toUpperCase).comap(Compound::getCode);

    public char getCode();

    public String getFullName();

}
