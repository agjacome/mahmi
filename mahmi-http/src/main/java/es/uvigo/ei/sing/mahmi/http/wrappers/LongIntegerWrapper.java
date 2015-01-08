package es.uvigo.ei.sing.mahmi.http.wrappers;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.AllArgsConstructor;
import lombok.Value;
import es.uvigo.ei.sing.mahmi.common.utils.annotations.VisibleForJAXB;

@AllArgsConstructor(staticName = "longWrapper")
@XmlRootElement(name = "count") @XmlAccessorType(XmlAccessType.FIELD)
@Value public final class LongIntegerWrapper {

    private final long total;

    @VisibleForJAXB public LongIntegerWrapper() {
        this(0);
    }

}
