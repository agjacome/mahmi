package es.uvigo.ei.sing.mahmi.http.wrappers;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.AllArgsConstructor;
import lombok.Getter;

import es.uvigo.ei.sing.mahmi.common.utils.annotations.VisibleForJAXB;

@Getter
@AllArgsConstructor(staticName = "wrap")
@XmlRootElement(name = "bool") @XmlAccessorType(XmlAccessType.FIELD)
public final class BooleanWrapper {

    private final boolean bool;

    @VisibleForJAXB public BooleanWrapper() {
        this.bool = false;
    }

}