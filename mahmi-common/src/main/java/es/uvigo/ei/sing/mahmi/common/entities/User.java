package es.uvigo.ei.sing.mahmi.common.entities;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.Wither;
import es.uvigo.ei.sing.mahmi.common.utils.Identifier;
import es.uvigo.ei.sing.mahmi.common.utils.annotations.VisibleForJAXB;


@ToString(exclude = "password")
@Getter @Wither
@AllArgsConstructor(staticName = "user")
@XmlRootElement(name = "user") @XmlAccessorType(XmlAccessType.FIELD)
public final class User implements Entity<User>{

    private final Identifier id;
    private final String name;
    private final String organization;
    private final String userName;    
    private final String password;

    @VisibleForJAXB public User() {
        this(Identifier.empty(),"","","","");
    }
}