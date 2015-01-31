package es.uvigo.ei.sing.mahmi.common.serializers.jaxb;

import static java.util.Objects.isNull;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import es.uvigo.ei.sing.mahmi.common.utils.Identifier;
import fj.data.Natural;

public final class IdentifierAdapter extends XmlAdapter<Long, Identifier> {

    @Override
    public Identifier unmarshal(final Long id) throws Exception {
        return isNull(id) ? Identifier.empty() : Identifier.of(id);
    }

    @Override
    public Long marshal(final Identifier id) throws Exception {
        return id.getValue().map(Natural::longValue).orSome(-1L);
    }

}
