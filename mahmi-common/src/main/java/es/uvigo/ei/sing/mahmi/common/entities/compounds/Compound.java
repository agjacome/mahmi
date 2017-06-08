package es.uvigo.ei.sing.mahmi.common.entities.compounds;

import fj.Equal;
import fj.Hash;

/**
 * {@linkplain Compound} is an interface for chemical compounds
 * 
 * @author Alberto Gutierrez-Jacome
 *
 */
public interface Compound {

    public static final Hash<? extends Compound>  hash  = Hash.anyHash();    //charHash.comap(toUpperCase).comap(Compound::getCode);
    public static final Equal<? extends Compound> equal = Equal.anyEqual();  // charEqual.comap(toUpperCase).comap(Compound::getCode);

    /**
     * Gets the code of a compound
     * 
     * @return The code of a compound
     */
    public char getCode();

    /**
     * Gets the full name of a compound
     * 
     * @return The full name of a compound
     */
    public String getFullName();

}
