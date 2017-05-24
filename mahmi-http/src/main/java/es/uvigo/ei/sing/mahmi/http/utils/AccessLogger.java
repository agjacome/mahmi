package es.uvigo.ei.sing.mahmi.http.utils;

import org.glassfish.grizzly.http.server.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.uvigo.ei.sing.mahmi.http.wrappers.WebLogWrapper;
import lombok.AllArgsConstructor;

@AllArgsConstructor(staticName="accessLogger")
public class AccessLogger {
	final private Logger accessLog = LoggerFactory.getLogger("access");
	public void log(final Request request) {
		accessLog.info("Incoming request from:"+
					   "\nAddress: "+request.getRemoteAddr()+
					   "\nHost: "+request.getRemoteHost()+
					   "\nPort: "+request.getRemotePort()+
					   "\nUser: "+request.getRemoteUser()+
					   "\nRequest packet: "+request.getRequest().toString());		
	}
	
	public void weblog(final WebLogWrapper log){
		accessLog.info("Incoming request from webpage:"+
					   "\nAddress: "+log.getAddress()+
					   "\nPort: "+log.getPort()+
					   "\nHostname: "+log.getHostname()+
					   "\nCity: "+log.getCity()+
					   "\nRegion: "+log.getRegion()+
					   "\nCountry: "+log.getCountry()+
					   "\nLoc: "+log.getLoc()+
					   "\nOrg: "+log.getOrg()+
					   "\nUser agent: "+log.getBrowser());	
	}
}

