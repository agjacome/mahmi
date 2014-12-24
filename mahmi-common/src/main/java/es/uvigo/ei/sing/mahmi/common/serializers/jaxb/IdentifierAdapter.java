package es.uvigo.ei.sing.mahmi.common.serializers.jaxb;

import static java.util.Objects.isNull;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import es.uvigo.ei.sing.mahmi.common.utils.Identifier;

public final class IdentifierAdapter extends XmlAdapter<Integer, Identifier> {

    @Override
    public Identifier unmarshal(final Integer id) throws Exception {
        return isNull(id) ? Identifier.empty() : Identifier.of(id);
    }

    @Override
    public Integer marshal(final Identifier id) throws Exception {
        return id.get().orSome(-1);
    }

}
