package es.uvigo.ei.sing.mahmi.common.serializers.jaxb.entities.compounds;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import es.uvigo.ei.sing.mahmi.common.entities.compounds.Nucleobase;

public final class NucleobaseAdapter extends XmlAdapter<Character, Nucleobase> {

    @Override
    public Nucleobase unmarshal(final Character code) throws Exception {
        return Nucleobase.fromCode(code);
    }

    @Override
    public Character marshal(final Nucleobase nucleobase) throws Exception {
        return nucleobase.getCode();
    }

}
