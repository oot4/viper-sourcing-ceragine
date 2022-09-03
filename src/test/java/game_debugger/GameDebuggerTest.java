/*
 * Copyright (c) 2022. The Ceragine project files, related assets and related source code
 * were created by, Ogesoka (Oge) Tasie and should not be copied or distributed without
 * consent of the creator (Ogesoka Tasie). All Rights Reserved.
 */

package game_debugger;

import org.junit.Test;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import static org.junit.Assert.assertEquals;

/**
 * A test class for the logger that handles console and file logging
 */
public class GameDebuggerTest
{

    /**
     * Test whether setting debug mode works correctly for the logger
     */
    private void assertDebugMode( boolean debugOn )
    {
        GameDebugger.setDebugMode( debugOn );
        try
        {
            Object empty = new Object( );
            Method method = GameDebugger.class.getDeclaredMethod( "getInstance" );
            method.setAccessible( true );
            GameDebugger debugger = ( GameDebugger ) method.invoke( empty );
            Field field = debugger.getClass( ).getDeclaredField( "debugMode" );
            field.setAccessible( true );
            assertEquals( "|Fields did not match|" , field.get( debugger ) , debugOn );
        }
        catch ( NoSuchMethodException | InvocationTargetException | IllegalAccessException | NoSuchFieldException e )
        {
            throw new RuntimeException( e );
        }
    }

    /**
     * Test whether an exception is thrown when turning debug mode on or off
     */
    @Test
    public void setDebugMode( )
    {
        assertDebugMode( true );
        assertDebugMode( false );
    }

    /**
     * Test whether the logging call is correctly processed and output to the console is recorded when using a single string
     */
    @Test
    public void logWithString( )
    {
        GameDebugger.setDebugMode( true );
        Logger logger = Logger.getLogger( GameDebugger.class.getSimpleName( ) );
        List< LogRecord > logRecords = new ArrayList<>( );
        logger.setFilter( record ->
        {
            if ( record.getLevel( ).equals( Level.SEVERE ) )
            {
                logRecords.add( record );
            }
            return false;
        } );
        GameDebugger.log( Alert.DIRE , "this is a logged severe/dire message using string" );
        assertEquals( "|Size of log record did not match|" , 1 , logRecords.size( ) );
        assertEquals( "|Logged message did not match|" , "this is a logged severe/dire message using string".toLowerCase( ) , logRecords.get( 0 ).getMessage( ) );
    }

    /**
     * Test whether the logging call is correctly processed and output to the console is recorded when using a message supplier
     */
    @Test
    public void logWithSupplier( )
    {
        GameDebugger.setDebugMode( true );
        Logger logger = Logger.getLogger( GameDebugger.class.getSimpleName( ) );
        List< LogRecord > logRecords = new ArrayList<>( );
        logger.setFilter( record ->
        {
            if ( record.getLevel( ).equals( Level.SEVERE ) )
            {
                logRecords.add( record );
            }
            return false;
        } );
        GameDebugger.log( Alert.DIRE , ( ) -> "this is a logged severe/dire message using a supplier" );
        assertEquals( "|Size of log record did not match|" , 1 , logRecords.size( ) );
        assertEquals( "|Logged message did not match|" , "this is a logged severe/dire message using a supplier".toLowerCase( ) , logRecords.get( 0 ).getMessage( ) );
    }

    /**
     * Test whether the logging call is correctly processed and output to the console is recorded when using a string and throwable
     */
    @Test
    public void logWithStringAndThrowable( )
    {
        GameDebugger.setDebugMode( true );
        Logger logger = Logger.getLogger( GameDebugger.class.getSimpleName( ) );
        List< LogRecord > logRecords = new ArrayList<>( );
        logger.setFilter( record ->
        {
            if ( record.getLevel( ).equals( Level.SEVERE ) )
            {
                logRecords.add( record );
            }
            return false;
        } );
        GameDebugger.log( Alert.DIRE , "this is a logged severe/dire message using a string and a throwable" , new IllegalStateException( ) );
        assertEquals( "|Size of log record did not match|" , 1 , logRecords.size( ) );
        assertEquals( "|Logged message did not match|" , "this is a logged severe/dire message using a string and a throwable".toLowerCase( ) , logRecords.get( 0 ).getMessage( ) );
        assertEquals( "|Logged exception did not match|" , IllegalStateException.class , logRecords.get( 0 ).getThrown( ).getClass( ) );
    }

    /**
     * Test whether the logging call is correctly processed and output to the console is recorded when using a supplier and throwable
     */
    @Test
    public void logWithSupplierAndThrowable( )
    {
        GameDebugger.setDebugMode( true );
        Logger logger = Logger.getLogger( GameDebugger.class.getSimpleName( ) );
        List< LogRecord > logRecords = new ArrayList<>( );
        logger.setFilter( record ->
        {
            if ( record.getLevel( ).equals( Level.SEVERE ) )
            {
                logRecords.add( record );
            }
            return false;
        } );
        GameDebugger.log( Alert.DIRE , ( ) -> "this is a logged severe/dire message using a supplier and a throwable" , new IllegalStateException( ) );
        assertEquals( "|Size of log record did not match|" , 1 , logRecords.size( ) );
        assertEquals( "|Logged message did not match|" , "this is a logged severe/dire message using a supplier and a throwable".toLowerCase( ) , logRecords.get( 0 ).getMessage( ) );
        assertEquals( "|Logged exception did not match|" , IllegalStateException.class , logRecords.get( 0 ).getThrown( ).getClass( ) );
    }

}