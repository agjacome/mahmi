package es.uvigo.ei.sing.mahmi.common.entities.compounds;

/**
 * <p>
 * ChemicalCompound represents an interface to which all compounds in the system
 * will adhere to. Every compound has a name ({@link #getFullName}), and a short
 * name ({@link #getShortName}).
 * </p>
 */
public interface ChemicalCompound {

    public String getFullName();

    public Iterable<Character> getShortName();

}
