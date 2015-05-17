package es.uvigo.ei.sing.mahmi.common.serializers.jaxb;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import es.uvigo.ei.sing.mahmi.common.utils.Identifier;

public final class IdentifierAdapter extends XmlAdapter<Long, Identifier> {

    @Override
    public Identifier unmarshal(final Long id) throws Exception {
        return id == null ? Identifier.empty() : Identifier.of(id);
    }

    @Override
    public Long marshal(final Identifier id) throws Exception {
        return id.get().orElse(-1L);
    }

}
