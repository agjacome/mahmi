package es.uvigo.ei.sing.mahmi.common.serializers.jaxb;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import es.uvigo.ei.sing.mahmi.common.entities.compounds.AminoAcid;

public final class AminoAcidAdapter extends XmlAdapter<Character, AminoAcid> {

    @Override
    public AminoAcid unmarshal(final Character code) throws Exception {
        return AminoAcid.fromCode(code);
    }

    @Override
    public Character marshal(final AminoAcid aminoacid) throws Exception {
        return aminoacid.getCode();
    }

}
