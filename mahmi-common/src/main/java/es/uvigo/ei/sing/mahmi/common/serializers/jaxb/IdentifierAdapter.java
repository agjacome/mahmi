package es.uvigo.ei.sing.mahmi.common.serializers.jaxb;

import java.util.Objects;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import lombok.experimental.ExtensionMethod;

import fj.data.Natural;

import es.uvigo.ei.sing.mahmi.common.utils.Identifier;

/**
 * {@linkplain IdentifierAdapter} is a class that extends {@link XmlAdapter}
 * with {@link Identifier} parsing methods
 * 
 * @author Alberto Gutierrez-Jacome
 * 
 * @see Identifier
 *
 */
@ExtensionMethod(Objects.class)
public final class IdentifierAdapter extends XmlAdapter<Long, Identifier> {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.xml.bind.annotation.adapters.XmlAdapter#unmarshal(java.lang.Object)
	 */
	@Override
	public Identifier unmarshal(final Long id) throws Exception {
		return id.isNull() ? Identifier.empty() : Identifier.of(id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.xml.bind.annotation.adapters.XmlAdapter#marshal(java.lang.Object)
	 */
	@Override
	public Long marshal(final Identifier id) throws Exception {
		return id.getValue().map(Natural::longValue).orSome(-1L);
	}

}
