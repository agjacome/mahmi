package es.uvigo.ei.sing.mahmi.common.entities.compounds;

public interface ChemicalCompound {

    public char getCode();

    public String getFullName();

    public Iterable<Character> getShortName();

}
