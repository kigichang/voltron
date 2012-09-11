package org.voltron.core;

import java.text.ParseException;

import xv.voltron.constant.ColumnType;
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
     * @throws ParseException 
     */
    public void testApp() throws ParseException
    {
        assertTrue( true );
        ColumnType ct = ColumnType.LONG;
        
        Long t = (Long) ct.parseValue("123456789");
        System.out.println("t = " + t);
        
        for(ColumnType ct2 : ColumnType.values()) {
        	System.out.println(ct2.toString());
        }
    }
}
