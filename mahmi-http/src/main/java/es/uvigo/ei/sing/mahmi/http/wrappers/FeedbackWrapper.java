package es.uvigo.ei.sing.mahmi.http.wrappers;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import es.uvigo.ei.sing.mahmi.common.utils.annotations.VisibleForJAXB;

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

	private FeedbackWrapper( String name, 
			                 String email, 
			                 String subject,
			                 String text){
		super();
		this.name = name;
		this.email = email;
		this.subject = subject;
		this.text = text;
	}

	public static FeedbackWrapper wrap( String name, 
						           		String email, 
						           		String subject,
						           		String text ){
		return new FeedbackWrapper(name, email, subject, text);
	}

	public String getName() {
		return name;
	}

	public String getEmail() {
		return email;
	}

	public String getSubject() {
		return subject;
	}

	public String getText() {
		return text;
	}
}
