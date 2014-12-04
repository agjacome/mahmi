package es.uvigo.ei.sing.mahmi.common.utils;

import java.net.URI;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import es.uvigo.ei.sing.mahmi.common.utils.annotations.VisibleForJAXB;

@Getter
@ToString
@EqualsAndHashCode(exclude = "name")
@RequiredArgsConstructor(staticName = "location")
@XmlRootElement @XmlAccessorType(XmlAccessType.FIELD)
public final class Location {

    private final String name;
    private final URI    uri;

    @VisibleForJAXB
    public Location() {
        this.name = "Default URI";
        this.uri  = URI.create("http://example.com");
    }

}
