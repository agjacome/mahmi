package es.uvigo.ei.sing.mahmi.common.entities;

import static fj.Equal.p2Equal;
import static fj.Equal.stringEqual;
import static fj.Hash.p2Hash;
import static fj.Hash.stringHash;
import static fj.P.p;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import es.uvigo.ei.sing.mahmi.common.utils.Identifier;
import es.uvigo.ei.sing.mahmi.common.utils.annotations.VisibleForJAXB;
import fj.Equal;
import fj.Hash;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Wither;

/**
 * {@linkplain Project} is a class that represents a project
 * 
 * @author Alberto Gutierrez-Jacome
 * 
 * @see Entity
 * @see Identifier
 */
@Getter
@Wither
@AllArgsConstructor(staticName = "project")
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public final class Project implements Entity<Project> {

	public static final Hash<Project> hash = p2Hash(stringHash, stringHash)
			.contramap(p -> p(p.name, p.repository));

	public static final Equal<Project> equal = p2Equal(stringEqual, stringEqual)
			.contramap(p -> p(p.name, p.repository));

	/**
	 * The project identifier
	 */
	private final Identifier id;

	/**
	 * The project name
	 */
	private final String name;

	/**
	 * The project repository
	 */
	private final String repository;

	/**
	 * {@linkplain Project} default constructor
	 */
	@VisibleForJAXB
	public Project() {
		this(new Identifier(), "", "");
	}

	/**
	 * Constructs a new instance of {@linkplain Project} without {@link Identifier}
	 * 
	 * @param name
	 *            The project name
	 * @param repository
	 *            The project repository
	 * @return A new instance of {@linkplain Project}
	 */
	public static Project project(final String name, final String repository) {
		return project(Identifier.empty(), name, repository);
	}

}
