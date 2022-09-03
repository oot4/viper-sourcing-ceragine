/*
 * Copyright (c) 2022. The Ceragine project files, related assets and related source code
 * were created by, Ogesoka (Oge) Tasie and should not be copied or distributed without
 * consent of the creator (Ogesoka Tasie). All Rights Reserved.
 */

package routine_handler.routines.standard;

import junit.framework.TestCase;

import java.lang.reflect.Field;

/**
 * A test class for the abstract loop routine that provides a way to repeat a routine consecutively
 */
public class LoopRoutineTest extends TestCase
{

    /**
     * Tests whether the correct overhead value is returned
     */
    public void testGetOverhead( )
    {
        try
        {
            LoopRoutine test = new TestLoopRoutine( );
            Field overhead = test.getClass( ).getSuperclass( ).getDeclaredField( "overhead" );
            overhead.setAccessible( true );
            overhead.set( test , 3L );
            assertEquals( "|Values did not match|" , 3L , test.getOverhead( ) );
            overhead.set( test , 2L );
            assertEquals( "|Values did not match|" , 2L , test.getOverhead( ) );
        }
        catch ( NoSuchFieldException | IllegalAccessException e )
        {
            throw new RuntimeException( e );
        }
    }

    /**
     * Tests whether the running flag is correctly set to false when calling the stop function
     */
    public void testStop( )
    {
        try
        {
            LoopRoutine test = new TestLoopRoutine( );
            Field running = test.getClass( ).getSuperclass( ).getDeclaredField( "running" );
            running.setAccessible( true );
            running.set( test , true );
            Boolean isRunning = ( Boolean ) running.get( test );
            assertTrue( "|Value did not match|" , isRunning );
            test.exitLoop( );
            isRunning = ( Boolean ) running.get( test );
            assertFalse( "|Values did not match|" , isRunning );
        }
        catch ( NoSuchFieldException | IllegalAccessException e )
        {
            throw new RuntimeException( e );
        }
    }

    /**
     * Tests whether running flag is correctly set to true when the routine is initialized
     */
    public void testInternalInitialize( )
    {
        try
        {
            LoopRoutine test = new TestLoopRoutine( );
            Field running = test.getClass( ).getSuperclass( ).getDeclaredField( "running" );
            running.setAccessible( true );
            running.set( test , false );
            Boolean isRunning = ( Boolean ) running.get( test );
            assertFalse( "|Value did not match|" , isRunning );
            test.internalInitialize( );
            isRunning = ( Boolean ) running.get( test );
            assertTrue( "|Value did not match|" , isRunning );
        }
        catch ( Exception e )
        {
            throw new RuntimeException( e );
        }
    }

    /**
     * Tests whether the routine is looped when executed and the overhead is updated
     */
    public void testInternalPerform( )
    {
        try
        {
            TestLoopRoutine test = new TestLoopRoutine( );
            Field running = test.getClass( ).getSuperclass( ).getDeclaredField( "running" );
            running.setAccessible( true );
            running.set( test , true );
            assertEquals( "|Values did not match|" , 0 , test.getCount( ) );
            assertEquals( "|Values did not match|" , 0L , test.getOverhead( ) );
            test.internalPerform( );
            assertEquals( "|Values did not match|" , 6 , test.getCount( ) );
            assertTrue( "|Condition did not match|" , test.getOverhead( ) > 0L );
        }
        catch ( IllegalAccessException | NoSuchFieldException e )
        {
            throw new RuntimeException( e );
        }
    }

    /**
     * Dummy test routine
     */
    private static class TestLoopRoutine extends LoopRoutine
    {

        private int count = 0;

        protected TestLoopRoutine( )
        {
            super( );
        }

        @Override
        protected void update( )
        {
            setCount( getCount( ) + 1 );
            if ( count > 5 )
            {
                exitLoop( );
            }
        }

        @Override
        protected void internalTerminate( )
        {

        }

        @Override
        protected void onInitialize( )
        {

        }

        public int getCount( )
        {
            return count;
        }

        public void setCount( int count )
        {
            this.count = count;
        }

    }

}