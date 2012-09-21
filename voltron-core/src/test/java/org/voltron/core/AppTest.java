package org.voltron.core;

import java.text.ParseException;

import org.apache.commons.pool.impl.GenericObjectPool;

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
     * @throws ClassNotFoundException 
     */
    public void testApp() throws ParseException, ClassNotFoundException
    {
        assertTrue( true );
        Class.forName("com.mysql.jdbc.Driver");
        GenericObjectPool pool = new GenericObjectPool(null);
        
        
    }
}
