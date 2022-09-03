/*
 * Copyright (c) 2022. The Ceragine project files, related assets and related source code
 * were created by, Ogesoka (Oge) Tasie and should not be copied or distributed without
 * consent of the creator (Ogesoka Tasie). All Rights Reserved.
 */

package routine_handler.routines.standard;

import game_debugger.GameDebugger;
import junit.framework.TestCase;
import routine_handler.routines.api.Launchable;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

/**
 * A test class for the abstract thread routine that provides a framework for a thread runnable routine class
 */
public class ThreadRoutineTest extends TestCase
{

    /**
     * Tests whether the internal perform method performs all subroutines by default
     */
    public void testPerformSubroutines( )
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

            ThreadRoutine test = new TestRoutine1( );
            test.attach( ( ) -> logger.log( Level.SEVERE , "severe log message one" ) );
            test.attach( ( ) -> logger.log( Level.SEVERE , "severe log message two" ) );
            test.attach( ( ) -> logger.log( Level.SEVERE , "severe log message three" ) );

            //Test log records are populated with logging message routines
            Method performSubroutines = test.getClass( ).getSuperclass( ).getDeclaredMethod( "performSubroutines" );
            performSubroutines.setAccessible( true );
            performSubroutines.invoke( test );
            assertEquals( "|Size of log records did not match|" , 3 , logRecords.size( ) );

            //Test each log record message was recorded
            ArrayList< String > logMessages = new ArrayList<>( )
            {
                {
                    for ( LogRecord record : logRecords )
                    {
                        add( record.getMessage( ) );
                    }
                }
            };
            assertTrue( "|Logged message 1 did not match|" , logMessages.contains( "severe log message one".toLowerCase( ) ) );
            assertTrue( "|Logged message 2 did not match|" , logMessages.contains( "severe log message two".toLowerCase( ) ) );
            assertTrue( "|Logged message 3 did not match|" , logMessages.contains( "severe log message three".toLowerCase( ) ) );
        }
        catch ( IllegalAccessException | InvocationTargetException | NoSuchMethodException e )
        {
            throw new RuntimeException( e );
        }
    }

    /**
     * Tests whether launch method successfully executes each step in the routine
     */
    public void testLaunch( )
    {
        TestRoutine2 test = new TestRoutine2( );
        try
        {
            Field canLaunch = test.getClass( ).getSuperclass( ).getDeclaredField( "launch" );
            Field canInitialize = test.getClass( ).getSuperclass( ).getDeclaredField( "initialize" );
            canLaunch.setAccessible( true );
            canInitialize.setAccessible( true );
            canLaunch.set( test , false );
            canInitialize.set( test , true );
            test.run( );
            assertEquals( "|Result did not match|" , 3.0 , test.getResult( ) );
        }
        catch ( NoSuchFieldException | IllegalAccessException e )
        {
            throw new RuntimeException( e );
        }
    }

    /**
     * Tests whether the call to initialize all subroutines correctly works if the subroutine is launchable
     */
    public void testInitializeSubroutines( )
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

            ThreadRoutine test = new TestRoutine1( );

            Launchable initializeSubroutine1 = new Launchable( )
            {
                @Override
                public void initialize( )
                {
                    logger.log( Level.SEVERE , "severe log message one" );
                }

                @Override
                public void terminate( )
                {
                }
            };
            Launchable initializeSubroutine2 = new Launchable( )
            {
                @Override
                public void initialize( )
                {
                    logger.log( Level.SEVERE , "severe log message two" );
                }

                @Override
                public void terminate( )
                {
                }
            };
            Launchable initializeSubroutine3 = new Launchable( )
            {
                @Override
                public void initialize( )
                {
                    logger.log( Level.SEVERE , "severe log message three" );
                }

                @Override
                public void terminate( )
                {
                }
            };

            test.attach( initializeSubroutine1 );
            test.attach( initializeSubroutine2 );
            test.attach( initializeSubroutine3 );
            test.attach( ( ) -> logger.log( Level.SEVERE , "severe log message four" ) );

            //Test log records are populated with logging message routine on subroutine initialize call
            Method initializeSubroutines = test.getClass( ).getSuperclass( ).getDeclaredMethod( "initializeSubroutines" );
            initializeSubroutines.setAccessible( true );
            initializeSubroutines.invoke( test );
            assertEquals( "|Size of log records did not match|" , 3 , logRecords.size( ) );

            //Test each log record message was recorded
            ArrayList< String > logMessages = new ArrayList<>( )
            {
                {
                    for ( LogRecord record : logRecords )
                    {
                        add( record.getMessage( ) );
                    }
                }
            };
            assertTrue( "|Logged message 1 did not match|" , logMessages.contains( "severe log message one".toLowerCase( ) ) );
            assertTrue( "|Logged message 2 did not match|" , logMessages.contains( "severe log message two".toLowerCase( ) ) );
            assertTrue( "|Logged message 3 did not match|" , logMessages.contains( "severe log message three".toLowerCase( ) ) );
        }
        catch ( IllegalAccessException | NoSuchMethodException | InvocationTargetException e )
        {
            throw new RuntimeException( e );
        }
    }

    /**
     * Tests whether the initialize method cannot be used within itself, through an internal method or after the first call
     */
    public void testInitialize( )
    {
        try
        {
            TestRoutine2 test = new TestRoutine2( );

            //Test that initialize function cannot be executed directly
            test.initialize( );
            assertEquals( "|Result did not match|" , 0.0 , test.getResult( ) );

            //Test that initialize cannot be executed directly through internal method
            test.internalInitialize( );
            assertEquals( "|Result did not match|" , 1.0 , test.getResult( ) );

            //Test that initialize runs once and becomes unavailable after performing its operation
            Field canLaunch = test.getClass( ).getSuperclass( ).getDeclaredField( "launch" );
            Field canInitialize = test.getClass( ).getSuperclass( ).getDeclaredField( "initialize" );
            canLaunch.setAccessible( true );
            canInitialize.setAccessible( true );
            canLaunch.set( test , false );
            canInitialize.set( test , true );
            test.setResult( 0 );
            test.initialize( );
            assertEquals( "|Result did not match|" , 1.0 , test.getResult( ) );
        }
        catch ( NoSuchFieldException | IllegalAccessException e )
        {
            throw new RuntimeException( e );
        }
    }

    /**
     * Tests whether the perform method cannot be used within itself, through an internal method or after the first call
     */
    public void testPerform( )
    {
        try
        {
            TestRoutine2 test = new TestRoutine2( );

            //Test that perform function cannot be executed directly
            test.perform( );
            assertEquals( "|Result did not match|" , 0.0 , test.getResult( ) );

            //Test that perform cannot be executed directly through internal method
            test.internalPerform( );
            assertEquals( "|Result did not match|" , 1.0 , test.getResult( ) );

            //Test that perform runs once and becomes unavailable after performing its operation
            Field canLaunch = test.getClass( ).getSuperclass( ).getDeclaredField( "launch" );
            Field canPerform = test.getClass( ).getSuperclass( ).getDeclaredField( "perform" );
            canLaunch.setAccessible( true );
            canPerform.setAccessible( true );
            canLaunch.set( test , false );
            canPerform.set( test , true );
            test.setResult( 0 );
            test.perform( );
            assertEquals( "|Result did not match|" , 1.0 , test.getResult( ) );
        }
        catch ( NoSuchFieldException | IllegalAccessException e )
        {
            throw new RuntimeException( e );
        }
    }

    /**
     * Tests whether the call to terminate all subroutines works correctly if a subroutine is a launchable
     */
    public void testTerminateSubroutines( )
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

            ThreadRoutine test = new TestRoutine1( );

            Launchable terminateLaunchable1 = new Launchable( )
            {
                @Override
                public void initialize( )
                {

                }

                @Override
                public void terminate( )
                {
                    logger.log( Level.SEVERE , "severe log message one" );
                }
            };
            Launchable terminateLaunchable2 = new Launchable( )
            {
                @Override
                public void initialize( )
                {

                }

                @Override
                public void terminate( )
                {
                    logger.log( Level.SEVERE , "severe log message two" );
                }
            };
            Launchable terminateLaunchable3 = new Launchable( )
            {
                @Override
                public void initialize( )
                {

                }

                @Override
                public void terminate( )
                {
                    logger.log( Level.SEVERE , "severe log message three" );
                }
            };

            test.attach( terminateLaunchable1 );
            test.attach( terminateLaunchable2 );
            test.attach( terminateLaunchable3 );
            test.attach( ( ) -> logger.log( Level.SEVERE , "severe log message four" ) );

            //Test log records are populated with logging message routine on subroutine terminate call
            Method terminateSubroutines = test.getClass( ).getSuperclass( ).getDeclaredMethod( "terminateSubroutines" );
            terminateSubroutines.setAccessible( true );
            terminateSubroutines.invoke( test );
            assertEquals( "|Size of log records did not match|" , 3 , logRecords.size( ) );

            //Test each log record message was recorded
            ArrayList< String > logMessages = new ArrayList<>( )
            {
                {
                    for ( LogRecord record : logRecords )
                    {
                        add( record.getMessage( ) );
                    }
                }
            };
            assertTrue( "|Logged message 1 did not match|" , logMessages.contains( "severe log message one".toLowerCase( ) ) );
            assertTrue( "|Logged message 2 did not match|" , logMessages.contains( "severe log message two".toLowerCase( ) ) );
            assertTrue( "|Logged message 3 did not match|" , logMessages.contains( "severe log message three".toLowerCase( ) ) );
        }
        catch ( IllegalAccessException | NoSuchMethodException | InvocationTargetException e )
        {
            throw new RuntimeException( e );
        }
    }

    /**
     * Tests whether the terminate method cannot be used within itself, through an internal method or after the first call
     */
    public void testTerminate( )
    {
        try
        {
            TestRoutine2 test = new TestRoutine2( );

            //Test that terminate function cannot be executed directly
            test.terminate( );
            assertEquals( "|Result did not match|" , 0.0 , test.getResult( ) );

            //Test that terminate cannot be executed directly through internal method
            test.internalTerminate( );
            assertEquals( "|Result did not match|" , 1.0 , test.getResult( ) );

            //Test that terminate runs once and becomes unavailable after performing its operation
            Field canLaunch = test.getClass( ).getSuperclass( ).getDeclaredField( "launch" );
            Field canTerminate = test.getClass( ).getSuperclass( ).getDeclaredField( "terminate" );
            canLaunch.setAccessible( true );
            canTerminate.setAccessible( true );
            canLaunch.set( test , false );
            canTerminate.set( test , true );
            test.setResult( 0 );
            test.terminate( );
            assertEquals( "|Result did not match|" , 1.0 , test.getResult( ) );
        }
        catch ( NoSuchFieldException | IllegalAccessException e )
        {
            throw new RuntimeException( e );
        }
    }

    /**
     * Dummy basic routine class
     */
    private static class TestRoutine1 extends ThreadRoutine
    {

        @Override
        protected void internalInitialize( )
        {

        }

        @Override
        protected void internalTerminate( )
        {

        }

    }

    /**
     * Dummy basic routine class
     */
    private static class TestRoutine2 extends ThreadRoutine
    {

        private int executionCount = 0;

        private long result = 0;

        @Override
        protected void internalInitialize( )
        {
            //prevents an infinite loop
            if ( executionCount > 3 )
            {
                return;
            }
            setResult( Math.addExact( result , 1 ) );
            executionCount++;
            initialize( );
        }

        @Override
        protected void internalPerform( )
        {
            //prevents an infinite loop
            if ( executionCount > 3 )
            {
                return;
            }
            setResult( Math.addExact( result , 1 ) );
            executionCount++;
            perform( );
        }

        @Override
        protected void internalTerminate( )
        {
            //prevents an infinite loop
            if ( executionCount > 3 )
            {
                return;
            }
            setResult( Math.addExact( result , 1 ) );
            executionCount++;
            terminate( );
        }

        public double getResult( )
        {
            return result;
        }

        private void setResult( long result )
        {
            this.result = result;
        }

    }

}