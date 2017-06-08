package es.uvigo.ei.sing.mahmi.common.entities;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Wither;

import es.uvigo.ei.sing.mahmi.common.utils.Identifier;
import es.uvigo.ei.sing.mahmi.common.utils.annotations.VisibleForJAXB;

/**
 * {@linkplain TableStat} is a class that represents the MAHMI statistics
 * 
 * @author Aitor Blanco-Miguez
 * 
 * @see Entity
 * @see Identifier
 *
 */
@Getter @Wither
@AllArgsConstructor(staticName = "tableStat")
@XmlRootElement(name = "tableStat") @XmlAccessorType(XmlAccessType.FIELD)
public final class TableStat implements Entity<TableStat>{

    /**
     * The statistic identifier
     */
    private final Identifier id;
    
    /**
     * The statistic name
     */
    private final String name;
    
    /**
     * The statistic counter
     */
    private final long counter;

    /**
     * {@linkplain TableStat} default constructor
     */
    @VisibleForJAXB public TableStat() {
        this(Identifier.empty(),"",0);
    }

}