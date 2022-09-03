/*
 * Copyright (c) 2022. The Ceragine project files, related assets and related source code
 * were created by, Ogesoka (Oge) Tasie and should not be copied or distributed without
 * consent of the creator (Ogesoka Tasie). All Rights Reserved.
 */

package routine_handler.routines.standard;

import game_debugger.GameDebugger;
import junit.framework.TestCase;
import routine_handler.routines.api.Subroutine;
import routine_handler.routines.core.BasicRoutine;

import java.lang.reflect.Field;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

/**
 * A test class for the abstract basic routine that provides a framework for a routine class
 */
public class BasicRoutineTest extends TestCase
{

    /**
     * Test whether getting the set of subroutines from a routine works correctly
     */
    public void testGetSubroutines( )
    {
        try
        {
            Set< Subroutine > subroutineSet = ConcurrentHashMap.newKeySet( );
            BasicRoutine test = new TestRoutine( );
            Field subroutines = test.getClass( ).getSuperclass( ).getDeclaredField( "subroutines" );
            subroutines.setAccessible( true );
            subroutines.set( test , subroutineSet );

            //Compare the set of subroutines
            assertEquals( "|Objects did not match|" , subroutineSet , test.getSubroutines( ) );
        }
        catch ( Exception e )
        {
            throw new RuntimeException( e );
        }
    }

    /**
     * Tests whether attaching a null subroutine or attaching an existing subroutine throws an exception. Also test that the method correctly attaches a subroutine
     */
    public void testAttach( )
    {
        try
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

            Set< Subroutine > subroutineSet = ConcurrentHashMap.newKeySet( );
            Subroutine subroutine1 = ( ) -> logger.log( Level.INFO , "detach test 1" );
            Subroutine subroutine2 = ( ) -> { };
            subroutineSet.add( subroutine1 );
            BasicRoutine test = new TestRoutine( );
            Field subroutines = test.getClass( ).getSuperclass( ).getDeclaredField( "subroutines" );
            subroutines.setAccessible( true );
            subroutines.set( test , subroutineSet );

            //Test null value throws
            test.attach( null );
            assertEquals( "|Logged message did not match|" , MessageFormat.format( "cannot attach {0}" , ( Subroutine ) null ).toLowerCase( ) , logRecords.get( 0 ).getMessage( ) );

            //Test illegal value throws, duplicate value
            test.attach( subroutine1 );
            assertEquals( "|Logged message did not match|" , MessageFormat.format( "cannot attach {0}" , subroutine1 ).toLowerCase( ) , logRecords.get( 1 ).getMessage( ) );

            //Test function successfully adds subroutine
            test.attach( subroutine2 );
            Object testSubroutines = subroutines.get( test );
            assert testSubroutines instanceof Set< ? >;
            assertTrue( "|Attach was not successful|" , ( ( Set< ? > ) testSubroutines ).contains( subroutine2 ) );

            //Trying to attach the subroutine again should fail
            test.attach( subroutine2 );
            assertEquals( "|Logged message did not match|" , MessageFormat.format( "cannot attach {0}" , subroutine2 ).toLowerCase( ) , logRecords.get( 2 ).getMessage( ) );
        }
        catch ( NoSuchFieldException | IllegalAccessException e )
        {
            throw new RuntimeException( e );
        }
    }

    /**
     * Tests whether detaching a null subroutine or detaching an illegal subroutine throws an exception. Also test that the method correctly detaches an existing subroutine
     */
    public void testDetach( )
    {
        try
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

            Set< Subroutine > subroutineSet = ConcurrentHashMap.newKeySet( );
            Subroutine subroutine = ( ) -> logger.log( Level.INFO , "detach test 1" );
            subroutineSet.add( ( ) -> logger.log( Level.INFO , "detach test 2" ) );
            subroutineSet.add( subroutine );
            BasicRoutine test = new TestRoutine( );
            Field subroutines = test.getClass( ).getSuperclass( ).getDeclaredField( "subroutines" );
            subroutines.setAccessible( true );
            subroutines.set( test , subroutineSet );

            //Test null value throws
            test.detach( null );
            assertEquals( "|Logged message did not match|" , MessageFormat.format( "cannot detach {0}" , ( Subroutine ) null ).toLowerCase( ) , logRecords.get( 0 ).getMessage( ) );

            //Test illegal value throws
            Subroutine illegal = ( ) -> { };
            test.detach( illegal );
            assertEquals( "|Logged message did not match|" , MessageFormat.format( "cannot detach {0}" , illegal ).toLowerCase( ) , logRecords.get( 1 ).getMessage( ) );

            //Test function successfully removes subroutine
            test.detach( subroutine );
            Object testSubroutines = subroutines.get( test );
            assert testSubroutines instanceof Set< ? >;
            assertFalse( "|Detach was not successful|" , ( ( Set< ? > ) testSubroutines ).contains( subroutine ) );

            //Trying to detach the subroutine again should fail
            test.detach( subroutine );
            assertEquals( "|Logged message did not match|" , MessageFormat.format( "cannot detach {0}" , subroutine ).toLowerCase( ) , logRecords.get( 2 ).getMessage( ) );
        }
        catch ( NoSuchFieldException | IllegalAccessException e )
        {
            throw new RuntimeException( e );
        }
    }

    /**
     * Tests whether the correct size is returned when routine is populated with subroutines
     */
    public void testSize( )
    {
        try
        {
            Logger logger = Logger.getLogger( GameDebugger.class.getSimpleName( ) );

            Set< Subroutine > subroutineSet = ConcurrentHashMap.newKeySet( );
            subroutineSet.add( ( ) -> logger.log( Level.INFO , "size test 1" ) );
            subroutineSet.add( ( ) -> logger.log( Level.INFO , "size test 2" ) );
            BasicRoutine test = new TestRoutine( );
            Field subroutines = test.getClass( ).getSuperclass( ).getDeclaredField( "subroutines" );
            subroutines.setAccessible( true );
            subroutines.set( test , subroutineSet );
            assertEquals( "|Size did not match|" , 2 , test.size( ) );
        }
        catch ( NoSuchFieldException | IllegalAccessException e )
        {
            throw new RuntimeException( e );
        }
    }

    /**
     * Dummy basic routine class
     */
    private static class TestRoutine extends BasicRoutine
    {

        @Override
        public void initialize( )
        {

        }

        @Override
        public void terminate( )
        {

        }

    }

}