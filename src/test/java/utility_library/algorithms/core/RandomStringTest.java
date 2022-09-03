/*
 * Copyright (c) 2022. The Ceragine project files, related assets and related source code
 * were created by, Ogesoka (Oge) Tasie and should not be copied or distributed without
 * consent of the creator (Ogesoka Tasie). All Rights Reserved.
 */

package utility_library.algorithms.core;

import junit.framework.TestCase;

import static org.junit.Assert.assertThrows;

/**
 * A class for testing the random generating functions in the {@link RandomString} class
 */
public class RandomStringTest extends TestCase
{

    /**
     * Tests whether generating an integer with the specified number of digits works correctly and that an exception is thrown when the specified size is out of bounds
     */
    public void testRandInt( )
    {
        String generatedInteger = String.valueOf( RandomString.randInt( 2 ) );
        assertFalse( "|Condition was not false|" , generatedInteger.isEmpty( ) );
        assertTrue( "|Conditions did not match|" , generatedInteger.length( ) <= 2 );

        //make sure that a size out of bounds throws an exception
        assertThrows( "|Did not throw an exception|" , IndexOutOfBoundsException.class , ( ) -> RandomString.randInt( 0 ) );
        assertThrows( "|Did not throw an exception|" , IndexOutOfBoundsException.class , ( ) -> RandomString.randInt( 10 ) );
    }

    /**
     * Tests whether generating a string with the specified number of characters works correctly and that an exception is thrown when the specified size is out of bounds
     */
    public void testRandStr( )
    {
        String generatedString = RandomString.randStr( 2 );
        assertFalse( "|Condition was not false|" , generatedString.isEmpty( ) );
        assertEquals( "|Values did not match|" , 2 , generatedString.length( ) );

        //make sure that a size out of bounds throws an exception
        assertThrows( "|Did not throw an exception|" , IndexOutOfBoundsException.class , ( ) -> RandomString.randStr( 0 ) );
        assertThrows( "|Did not throw an exception|" , IndexOutOfBoundsException.class , ( ) -> RandomString.randStr( 65 ) );
    }

    /**
     * Tests whether an integer of 4 digits is returned when calling {@code randInt4()}
     */
    public void testRandInt4( )
    {
        String generatedInteger = String.valueOf( RandomString.randInt4( ) );
        assertTrue( "|Conditions did not match|" , generatedInteger.length( ) <= 4 );
    }

    /**
     * Tests whether an integer of 8 digits is returned when calling {@code randInt8()}
     */
    public void testRandInt8( )
    {
        String generatedInteger = String.valueOf( RandomString.randInt8( ) );
        assertTrue( "|Conditions did not match|" , generatedInteger.length( ) <= 8 );
    }

    /**
     * Tests whether a string of 4 characters is returned when calling {@code randStr4()}
     */
    public void testRandStr4( )
    {
        String generatedString = RandomString.randStr4( );
        assertEquals( "|Values did not match|" , 4 , generatedString.length( ) );
    }

    /**
     * Tests whether a string of 8 characters is returned when calling {@code randStr8()}
     */
    public void testRandStr8( )
    {
        String generatedString = RandomString.randStr8( );
        assertEquals( "|Values did not match|" , 8 , generatedString.length( ) );
    }

}