package es.uvigo.ei.sing.mahmi.common.entities.compounds;

import fj.Equal;
import fj.Hash;

public interface Compound {

    public static final Hash<? extends Compound>  hash  = Hash.anyHash();    //charHash.comap(toUpperCase).comap(Compound::getCode);
    public static final Equal<? extends Compound> equal = Equal.anyEqual();  // charEqual.comap(toUpperCase).comap(Compound::getCode);

    public char getCode();

    public String getFullName();

}
