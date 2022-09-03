/*
 * Copyright (c) 2022. The Ceragine project files, related assets and related source code
 * were created by, Ogesoka (Oge) Tasie and should not be copied or distributed without
 * consent of the creator (Ogesoka Tasie). All Rights Reserved.
 */

package game_debugger;

import org.junit.Test;

import java.util.logging.Level;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

/**
 * A test class for the alert enum class with severity levels
 */
public class AlertTest
{

    /**
     * Test that the alert values are correctly mapped to the logger level values
     */
    @Test
    public void getLevel( )
    {
        assertEquals( "|Alert levels did not match|" , Level.INFO , Alert.INFO.getLevel( ) );
        assertEquals( "|Alert levels did not match|" , Level.WARNING , Alert.WARN.getLevel( ) );
        assertEquals( "|Alert levels did not match|" , Level.SEVERE , Alert.DIRE.getLevel( ) );

    }

    /**
     * Test that an exception is not thrown when getting all values
     */
    @Test
    public void values( )
    {
        assertArrayEquals( "|Alert level arrays did not match|" , new Alert[]{ Alert.INFO , Alert.WARN , Alert.DIRE } , Alert.values( ) );
    }

    /**
     * Test the case sensitivity of the {@code valueOf} method
     */
    @Test
    public void valueOf( )
    {
        assertEquals( "|Alert levels did not match|" , Alert.INFO , Alert.valueOf( "INFO" ) );
        assertThrows( "|Illegal argument exception was not thrown|" , IllegalArgumentException.class , ( ) -> Alert.valueOf( "info" ) );
    }

}