package es.uvigo.ei.sing.mahmi.http.wrappers;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.AllArgsConstructor;
import lombok.Getter;

import es.uvigo.ei.sing.mahmi.common.utils.annotations.VisibleForJAXB;

@Getter
@AllArgsConstructor(staticName = "wrap")
@XmlRootElement(name = "count") @XmlAccessorType(XmlAccessType.FIELD)
public final class CountWrapper {

    private final long total;

    @VisibleForJAXB public CountWrapper() {
        this.total = 0L;
    }

}
