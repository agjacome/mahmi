package es.uvigo.ei.sing.mahmi.browser;

import static es.uvigo.ei.sing.mahmi.browser.Browser.browser;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import es.uvigo.ei.sing.mahmi.browser.utils.BlastAligment;
import es.uvigo.ei.sing.mahmi.common.entities.sequences.AminoAcidSequence;

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
		final Browser browser = browser();
		final List<String> dbs = new LinkedList<String>();
		dbs.add("refdb");
		//dbs.add("posdb");
		final List<String> bioactivity = new LinkedList<String>();
		bioactivity.add("ANT");
		final Path path = Paths.get("/srv/http/mahmi_tmp");
		
		final List<BlastAligment> aligments = browser.search(AminoAcidSequence.fromString("MRLGEKIMRLGKKTSRAISIADKD").some(), dbs, 60, bioactivity, path);
		
		aligments.forEach(a -> { 
			System.out.println(a.getDescription());
			System.out.println(a.getSequence());
			System.out.println(a.getScore());
			System.out.println(a.geteValue());
			System.out.println(a.getIdentities());
			System.out.println(a.getPositives());
			System.out.println(a.getGaps());
			System.out.println(a.getLength());
			System.out.println(a.getQuery());
			System.out.println(a.getComparation());
			System.out.println(a.getSubject());
			System.out.println(a.getPath());
		});
    	    	
        assertTrue( true );
    }
}
