package es.uvigo.ei.sing.mahmi.browser;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import es.uvigo.ei.sing.mahmi.browser.utils.BlastAligment;
import es.uvigo.ei.sing.mahmi.common.entities.sequences.AminoAcidSequence;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class BrowserTest 
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public BrowserTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( BrowserTest.class );
    }

    /**
     * Rigourous Test :-)
     */
    public void testApp()
    {
    	final Browser browser = new Browser();
    	final String[] dbs = {"refdb","posdb"};
    	final String[] bioactivity = {"ANT"};
    	final Path path = Paths.get("/home/mahmi/test");
    	
    	final List<BlastAligment> aligments = browser.search(AminoAcidSequence.fromString("MRLGEKIMRLGKKTSRAISIADKD").some(), dbs, 60, bioactivity, path);
    	
    	aligments.forEach(a -> { 
    		System.out.println(a.getDescription());
    		System.out.println(a.getSequence().asString());
    		System.out.println(a.getScore());
    		System.out.println(a.geteValue());
    		System.out.println(a.getIdentities());
    		System.out.println(a.getPositives());
    		System.out.println(a.getGaps());
    		System.out.println(a.getLength());
    	});
    	
        assertTrue( true );
    }
}
