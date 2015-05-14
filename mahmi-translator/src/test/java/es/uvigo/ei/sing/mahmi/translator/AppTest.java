package es.uvigo.ei.sing.mahmi.translator;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class AppTest 
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( AppTest.class );
    }

    /**
     * Rigourous Test :-)
     */
    public void testApp()
    {
    	ProteinTranslator pt=new ProteinTranslator("nr");
//    	pt.runBlastX(Paths.get("/home/mahmi/input.txt"));
    	
    	try {
			List<String> list = 
			    	Files.readAllLines(Paths.get("/home/mahmi/blastx.out"));
			pt.runBlastDbCmd(list.get(21).split("\\|",-1)[1],Paths.get("/home/mahmi/cmd.out"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	
    	
    	
        assertTrue( true );
    }
}
