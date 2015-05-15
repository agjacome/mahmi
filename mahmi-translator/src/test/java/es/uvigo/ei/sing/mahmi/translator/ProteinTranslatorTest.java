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
    	val pt = new ProteinTranslator("nr");
    	pt.translate(Paths.get("/home/mahmi/test.fna"));
        assertTrue( true );
    }
}
