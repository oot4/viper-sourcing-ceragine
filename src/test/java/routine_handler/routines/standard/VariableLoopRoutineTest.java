/*
 * Copyright (c) 2022. The Ceragine project files, related assets and related source code
 * were created by, Ogesoka (Oge) Tasie and should not be copied or distributed without
 * consent of the creator (Ogesoka Tasie). All Rights Reserved.
 */

package routine_handler.routines.standard;

import junit.framework.TestCase;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Test class for a routine with a variable loop
 */
public class VariableLoopRoutineTest extends TestCase
{

    /**
     * Tests whether the update method is successfully executed
     */
    public void testUpdate( )
    {
        try
        {
            TestRoutine test = new TestRoutine( );

            //Set the clock speed and overhead to an amount that will allow one routine call
            test.getClock( ).setMinRate( );
            Field overhead = LoopRoutine.class.getDeclaredField( "overhead" );
            overhead.setAccessible( true );
            overhead.set( test , 1000000000L );

            //Retrieve the update method to invoke
            Method method = VariableLoopRoutine.class.getDeclaredMethod( "update" );

            //Assert methods were not called
            assertFalse( "|Condition was not false|" , test.beginMethodWasCalled.getStatus( ) );
            assertFalse( "|Condition was not false|" , test.executeMethodWasCalled.getStatus( ) );
            assertFalse( "|Condition was not false|" , test.finishMethodWasCalled.getStatus( ) );

            method.invoke( test );

            //Assert methods were called
            assertTrue( "|Condition was not true|" , test.beginMethodWasCalled.getStatus( ) );
            assertTrue( "|Condition was not true|" , test.executeMethodWasCalled.getStatus( ) );
            assertTrue( "|Condition was not true|" , test.finishMethodWasCalled.getStatus( ) );

        }
        catch ( NoSuchMethodException | InvocationTargetException | IllegalAccessException | NoSuchFieldException e )
        {
            e.printStackTrace( );
        }
    }

    /**
     * Dummy test routine
     */
    private static final class TestRoutine extends VariableLoopRoutine
    {

        final Status beginMethodWasCalled;

        final Status executeMethodWasCalled;

        final Status finishMethodWasCalled;

        TestRoutine( )
        {
            beginMethodWasCalled = new Status( false );
            executeMethodWasCalled = new Status( false );
            finishMethodWasCalled = new Status( false );
        }

        @Override
        protected void onInitialize( )
        {
        }

        @Override
        protected void internalTerminate( )
        {

        }

        @Override
        protected void begin( )
        {
            beginMethodWasCalled.setStatus( true );
        }

        @Override
        protected void execute( )
        {
            executeMethodWasCalled.setStatus( true );
        }

        @Override
        protected void finish( long interpolation , boolean updated )
        {
            finishMethodWasCalled.setStatus( true );
        }

    }

    /**
     * Status used to indicate whether something is true or not
     */
    private static final class Status
    {

        private boolean status;

        public Status( boolean status )
        {
            this.status = status;
        }

        public void setStatus( boolean status )
        {
            this.status = status;
        }

        public boolean getStatus( )
        {
            return status;
        }

    }

}