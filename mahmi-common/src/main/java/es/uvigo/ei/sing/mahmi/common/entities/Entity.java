package es.uvigo.ei.sing.mahmi.common.entities;

import es.uvigo.ei.sing.mahmi.common.utils.Identifier;

/**
 * {@linkplain Entity} is an interface for MAHMI database entities
 * 
 * @author Alberto Gutierrez-Jacome
 *
 * @param <A>
 *            The entity class
 */
public interface Entity<A extends Entity<?>> {

	/**
	 * Gets the identifier of the entity
	 * 
	 * @return The identifier
	 */
	public Identifier getId();

	/**
	 * Constructs a entity with identifier
	 * 
	 * @param id
	 *            The identifier
	 * @return A new entity with identifier
	 */
	public A withId(final Identifier id);

}
