package es.uvigo.ei.sing.mahmi.common.serializers.jaxb;

import java.util.Objects;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import lombok.experimental.ExtensionMethod;

import fj.data.Natural;

import es.uvigo.ei.sing.mahmi.common.utils.Identifier;

@ExtensionMethod(Objects.class)
public final class IdentifierAdapter extends XmlAdapter<Long, Identifier> {

    @Override
    public Identifier unmarshal(final Long id) throws Exception {
        return id.isNull() ? Identifier.empty() : Identifier.of(id);
    }

    @Override
    public Long marshal(final Identifier id) throws Exception {
        return id.getValue().map(Natural::longValue).orSome(-1L);
    }

}
