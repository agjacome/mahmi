package es.uvigo.ei.sing.mahmi.http.wrappers;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import es.uvigo.ei.sing.mahmi.common.utils.annotations.VisibleForJAXB;

@XmlRootElement(name = "weblog") @XmlAccessorType(XmlAccessType.FIELD)
public class WebLogWrapper {
    private String address;
    private String port;
    private String hostname;
    private String city;
    private String region;
    private String country;
    private String loc;
    private String org;
    private String browser;
        
    @VisibleForJAXB
    public WebLogWrapper() {
    	this.address    = "";
    	this.port   = "";
    	this.hostname   = "";
    	this.city   = "";
    	this.region   = "";
    	this.country   = "";
    	this.loc   = "";
    	this.org   = "";
    	this.browser = "";
    } 

	private WebLogWrapper( String address, 
			               String port, 
			               String hostname,
			               String city,
			               String region,
			               String country,
			               String loc,
			               String org,
			               String browser){
		super();
		this.address = address;
		this.port = port;
		this.hostname = hostname;
    	this.city   = city;
    	this.region   = region;
    	this.country   = country;
    	this.loc   = loc;
    	this.org   = org;
		this.browser = browser;
	}

	public static WebLogWrapper wrap( String address, 
						           	  String port, 
						              String hostname,
						              String city,
						              String region,
						              String country,
						              String loc,
						              String org,
						           	  String browser){
		return new WebLogWrapper(address, port, hostname, city, region, country, loc, org, browser);
	}

	public String getAddress() {
		return address;
	}

	public String getPort() {
		return port;
	}

	public String getHostname() {
		return hostname;
	}

	public String getCity() {
		return city;
	}

	public String getRegion() {
		return region;
	}

	public String getCountry() {
		return country;
	}

	public String getLoc() {
		return loc;
	}

	public String getOrg() {
		return org;
	}

	public String getBrowser() {
		return browser;
	}
}
