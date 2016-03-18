package es.uvigo.ei.sing.mahmi.http.wrappers;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.AllArgsConstructor;
import lombok.Getter;
import es.uvigo.ei.sing.mahmi.common.utils.annotations.VisibleForJAXB;

@Getter
@AllArgsConstructor(staticName = "wrap")
@XmlRootElement(name = "feedback") @XmlAccessorType(XmlAccessType.FIELD)
public final class FeedbackWrapper {	

    private String name;
    private String email;
    private String subject;
    private String text;
    
    @VisibleForJAXB
    public FeedbackWrapper() {
    	this.name    = "";
    	this.email   = "";
    	this.subject = "";
    	this.text    = "";
    }
}
