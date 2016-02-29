package es.uvigo.ei.sing.mahmi.http.wrappers;

import java.util.LinkedList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.AllArgsConstructor;
import lombok.Getter;
import es.uvigo.ei.sing.mahmi.common.utils.annotations.VisibleForJAXB;

@Getter
@AllArgsConstructor(staticName = "wrap")
@XmlRootElement(name = "browserSearch") @XmlAccessorType(XmlAccessType.FIELD)
public final class SearchWrapper {	

    private final String sequence;

    @XmlElementWrapper(name = "databases")
    @XmlElements({@XmlElement(name = "database", type = String.class)})
    private final List<String> databases;

    private final int threshold;   

    @XmlElementWrapper(name = "bioactivities")
    @XmlElements({@XmlElement(name = "bioactivity", type = String.class)})
    private final List<String> bioactivity;
    
    private final String path;

    @VisibleForJAXB
    public SearchWrapper() {
    	this.sequence = "";
    	this.databases = new LinkedList<String>();
    	this.threshold = 60;
    	this.bioactivity = new LinkedList<String>();
    	this.path = "";
    }

}
