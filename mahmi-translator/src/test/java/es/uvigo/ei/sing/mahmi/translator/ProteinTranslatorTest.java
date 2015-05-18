package es.uvigo.ei.sing.mahmi.translator;

import java.nio.file.Paths;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import lombok.val;

public class ProteinTranslatorTest extends TestCase{
    public ProteinTranslatorTest( String testName ){
        super( testName );
    }

    public static Test suite(){
        return new TestSuite( ProteinTranslatorTest.class );
    }

    public void testApp(){   
    	
    	long time_start, time_end;
    	time_start = System.currentTimeMillis();
    	val pt = new ProteinTranslator("mh");
    	pt.translate(Paths.get("/home/mahmi/test.fna"));
        assertTrue( true );
    	time_end = System.currentTimeMillis();
    	System.out.println("The task has taken "+ ( time_end - time_start )/1000+" seconds");
    	
    }
}
