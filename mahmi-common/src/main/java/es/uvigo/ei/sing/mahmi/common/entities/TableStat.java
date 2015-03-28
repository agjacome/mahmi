package es.uvigo.ei.sing.mahmi.common.entities;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Wither;

import es.uvigo.ei.sing.mahmi.common.utils.Identifier;
import es.uvigo.ei.sing.mahmi.common.utils.annotations.VisibleForJAXB;

@Getter @Wither
@AllArgsConstructor(staticName = "tableStat")
@XmlRootElement(name = "tableStat") @XmlAccessorType(XmlAccessType.FIELD)
public final class TableStat implements Entity<TableStat>{

    private final Identifier id;
    private final String name;
    private final long counter;

    @VisibleForJAXB public TableStat() {
        this(Identifier.empty(),"",0);
    }

}