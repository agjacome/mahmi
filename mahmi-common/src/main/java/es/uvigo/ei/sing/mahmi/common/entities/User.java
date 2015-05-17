package es.uvigo.ei.sing.mahmi.common.entities;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import es.uvigo.ei.sing.mahmi.common.utils.Identifier;

import static es.uvigo.ei.sing.mahmi.common.utils.Contracts.requireNonNull;

@XmlRootElement @XmlAccessorType(XmlAccessType.FIELD)
public final class User extends Entity {

    // FIXME: use more adequate attribute types and names (clash name-userName),
    // define what attributes are actually necessary and create a valid equality
    // strategy for them

    private final String name;
    private final String organization;
    private final String userName;
    private final String password;

    public User() {
        this(Identifier.empty(), "", "", "", "");
    }

    public User(
        final Identifier id,
        final String     name,
        final String     organization,
        final String     userName,
        final String     password
    ) {
        super(id);

        this.name         = requireNonNull(name, "User name cannot be NULL");
        this.organization = requireNonNull(organization, "User organization cannot be NULL");
        this.userName     = requireNonNull(userName, "User username cannot be NULL");
        this.password     = requireNonNull(password, "User password cannot be NULL");
    }

    public static User User(
        final Identifier id,
        final String     name,
        final String     organization,
        final String     userName,
        final String     password
    ) {
        return new User(id, name, organization, userName, password);
    }

    public String getName() {
        return name;
    }

    public String getOrganization() {
        return organization;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

}
