package es.uvigo.ei.sing.mahmi.calculator;

import junit.framework.TestCase;

/**
 * Unit test for simple App.
 */
public class PeptideCalculatorTest 
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    

    /**
     * Rigourous Test :-)
     */
    
    public void testApp()
    {
    	BacteriaCalculator bc=new BacteriaCalculator();
    	bc.CreateMWFiles("/home/sing/Escritorio/Bacterias/Estudio/Dataset");
        assertTrue( true );
    }
}
