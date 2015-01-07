package es.uvigo.ei.sing.mahmi.common.utils;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.Data;
import lombok.EqualsAndHashCode;
import es.uvigo.ei.sing.mahmi.common.utils.annotations.VisibleForJAXB;

@EqualsAndHashCode
@XmlRootElement @XmlAccessorType(XmlAccessType.FIELD)
@Data public final class Count {

    private Integer total;

    @VisibleForJAXB public Count() {
        this(0);
    }

    public Count (final int total) {
        this.total=total;
    }

}

