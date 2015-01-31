package es.uvigo.ei.sing.mahmi.common.entities.compounds;

import static fj.Equal.charEqual;
import static fj.Hash.charHash;
import static fj.Show.charShow;
import static fj.function.Characters.toUpperCase;
import fj.Equal;
import fj.Hash;
import fj.Show;

public interface Compound {

    public static final Hash<Compound>  hash  = charHash.comap(toUpperCase).comap(Compound::getCode);
    public static final Equal<Compound> equal = charEqual.comap(toUpperCase).comap(Compound::getCode);
    public static final Show<Compound>  show  = charShow.comap(toUpperCase).comap(Compound::getCode);

    public char getCode();

    public String getFullName();

}
