/*
 * Copyright (c) 2022. The Ceragine project files, related assets and related source code
 * were created by, Ogesoka (Oge) Tasie and should not be copied or distributed without
 * consent of the creator (Ogesoka Tasie). All Rights Reserved.
 */

package routine_handler.components.core;

import junit.framework.TestCase;

import java.lang.reflect.Field;

/**
 * A test class for the routine clock that provides processing time monitoring
 */
public class RoutineClockTest extends TestCase
{

    /**
     * Tests whether the modifier is returned correctly
     */
    public void testGetModifier( )
    {
        RoutineClock clock = RoutineClock.from( 1.0D );
        assertEquals( "|Values did not match|" , 1.0D , clock.getFpsModifier( ) );
    }

    /**
     * Tests whether the scale is returned correctly
     */
    public void testGetScale( )
    {
        RoutineClock clock = RoutineClock.from( 1.0D );
        assertEquals( "|Values did not match|" , 1.0F , clock.getScale( ) );
    }

    /**
     * Tests whether the delta in nanoseconds is returned correctly
     */
    public void testGetDelta9( )
    {
        RoutineClock clock = RoutineClock.from( 60.0D );
        assertEquals( "|Values did not match|" , ( long ) ( 1.0E9D * Math.pow( 60.0D , - 1.0D ) ) , clock.getFpsDelta9( ) );
    }

    /**
     * Tests whether the delta in milliseconds is returned correctly
     */
    public void testGetDelta3( )
    {
        RoutineClock clock = RoutineClock.from( 60.0D );
        assertEquals( "|Values did not match|" , ( long ) ( 1.0E9D * Math.pow( 60.0D , - 1 ) * Math.pow( 1.0E6D , - 1 ) ) , clock.getDelta3( ) );
    }

    /**
     * Tests whether the rate is set correctly, the modifier is never lower than 1 and never higher than 120.0D
     */
    public void testSetRate( )
    {
        try
        {
            RoutineClock clock = RoutineClock.from( 60.0D );
            clock.setRate( 30.0D );

            Field modifierField = RoutineClock.class.getDeclaredField( "fpsModifier" );
            Field delta9Field = RoutineClock.class.getDeclaredField( "fpsDelta9" );
            Field delta3Field = RoutineClock.class.getDeclaredField( "fpsDelta3" );

            modifierField.setAccessible( true );
            delta9Field.setAccessible( true );
            delta3Field.setAccessible( true );

            //Test setting rate works correctly
            assertEquals( "|Values did not match|" , 30.0D , modifierField.get( clock ) );
            assertEquals( "|Values did not match|" , ( long ) ( 1.0E9D * Math.pow( 30.0D , - 1.0D ) ) , delta9Field.get( clock ) );
            assertEquals( "|Values did not match|" , ( long ) ( 1.0E9D * Math.pow( 30.0D , - 1 ) * Math.pow( 1.0E6D , - 1 ) ) , delta3Field.get( clock ) );

            //Test minimum rate is not exceeded
            clock.setRate( - 1.0D );
            assertEquals( "|Values did not match|" , 1.0D , modifierField.get( clock ) );
            assertEquals( "|Values did not match|" , ( long ) 1.0E9D , delta9Field.get( clock ) );
            assertEquals( "|Values did not match|" , ( long ) ( 1.0E9D * Math.pow( 1.0E6D , - 1 ) ) , delta3Field.get( clock ) );

            //Test maximum rate is not exceeded
            clock.setRate( 250.0D );
            assertEquals( "|Values did not match|" , 200.0D , modifierField.get( clock ) );
            assertEquals( "|Values did not match|" , ( long ) ( 1.0E9D * Math.pow( 200.0D , - 1 ) ) , delta9Field.get( clock ) );
            assertEquals( "|Values did not match|" , ( long ) ( 1.0E9D * Math.pow( 200.0D , - 1 ) * Math.pow( 1.0E6D , - 1 ) ) , delta3Field.get( clock ) );

        }
        catch ( NoSuchFieldException | SecurityException | IllegalAccessException exception )
        {
            throw new RuntimeException( exception );
        }
    }

    /**
     * Tests whether the scale is set correctly, delta is always greater than 1 if set to a scale zero or below and, delta is never lower than {@code 1.0E9D / (modifier*2.5D)}
     */
    public void testSetScale( )
    {
        try
        {
            RoutineClock clock = RoutineClock.from( 60.0D );
            clock.setScale( 1.5F );

            Field scaleField = RoutineClock.class.getDeclaredField( "scale" );
            Field delta9Field = RoutineClock.class.getDeclaredField( "delta9" );
            Field delta3Field = RoutineClock.class.getDeclaredField( "delta3" );

            scaleField.setAccessible( true );
            delta9Field.setAccessible( true );
            delta3Field.setAccessible( true );

            //Test setting scale works correctly
            assertEquals( "|Values did not match|" , 1.5F , scaleField.get( clock ) );
            assertEquals( "|Values did not match|" , ( long ) ( 1.0E9D * Math.pow( 59.727500569606D * 1.5D , - 1.0D ) ) , delta9Field.get( clock ) );
            assertEquals( "|Values did not match|" , ( long ) ( 1.0E9D * Math.pow( 59.727500569606D * 1.5D , - 1 ) * Math.pow( 1.0E6D , - 1 ) ) , delta3Field.get( clock ) );

            //Test setting scale to -1 works correctly by clamping the value to 0 but does not scale delta to 1.0E9D/-1.0D
            //If this passes value of 0 will pass
            clock.setScale( - 1.0F );
            assertEquals( "|Values did not match|" , 0.0F , scaleField.get( clock ) );
            assertEquals( "|Values did not match|" , ( long ) ( 1.0E9D * Math.pow( 59.727500569606D , - 1 ) ) , delta9Field.get( clock ) );
            assertEquals( "|Values did not match|" , ( long ) ( 1.0E9D * Math.pow( 59.727500569606D , - 1 ) * Math.pow( 1.0E6D , - 1 ) ) , delta3Field.get( clock ) );

            //Test setting scale above 2.5 works correctly by clamping the value to 2.5 but does not scale delta to 1.09ED/(60.0D*3.0D)
            //If this passes value of 2.5 will pass
            clock.setScale( 3.0F );
            assertEquals( "|Values did not match|" , 2.5F , scaleField.get( clock ) );
            assertEquals( "|Values did not match|" , ( long ) ( 1.0E9D * Math.pow( 59.727500569606D * 2.5D , - 1 ) ) , delta9Field.get( clock ) );
            assertEquals( "|Values did not match|" , ( long ) ( 1.0E9D * Math.pow( 59.727500569606D * 2.5D , - 1 ) * Math.pow( 1.0E6D , - 1 ) ) , delta3Field.get( clock ) );

        }
        catch ( NoSuchFieldException | SecurityException | IllegalAccessException exception )
        {
            throw new RuntimeException( exception );
        }
    }

    /**
     * Tests whether the minimum modifier is set resulting in the minimum delta possible
     */
    public void testSetMinRate( )
    {
        try
        {
            RoutineClock clock = RoutineClock.from( 20.0D );
            clock.setMinRate( );

            Field modifierField = RoutineClock.class.getDeclaredField( "fpsModifier" );
            Field scaleField = RoutineClock.class.getDeclaredField( "scale" );
            Field delta9Field = RoutineClock.class.getDeclaredField( "fpsDelta9" );
            Field delta3Field = RoutineClock.class.getDeclaredField( "fpsDelta3" );

            modifierField.setAccessible( true );
            scaleField.setAccessible( true );
            delta9Field.setAccessible( true );
            delta3Field.setAccessible( true );

            assertEquals( "|Values did not match|" , 1.0D , modifierField.get( clock ) );
            assertEquals( "|Values did not match|" , 1.0F , scaleField.get( clock ) );
            assertEquals( "|Values did not match|" , ( long ) ( 1.0E9D ) , delta9Field.get( clock ) );
            assertEquals( "|Values did not match|" , ( long ) ( 1.0E9D * Math.pow( 1.0E6D , - 1 ) ) , delta3Field.get( clock ) );
        }
        catch ( IllegalAccessException | IllegalArgumentException | NoSuchFieldException | SecurityException exception )
        {
            throw new RuntimeException( exception );
        }
    }

    /**
     * Tests whether the standard modifier is set resulting in a delta of approximately {@code 16742706.2988 nanoseconds}
     */
    public void testSetBaseRate( )
    {
        try
        {
            RoutineClock clock = RoutineClock.from( 1.0D );
            clock.setBaseRate( );

            Field modifierField = RoutineClock.class.getDeclaredField( "fpsModifier" );
            Field scaleField = RoutineClock.class.getDeclaredField( "scale" );
            Field delta9Field = RoutineClock.class.getDeclaredField( "fpsDelta9" );
            Field delta3Field = RoutineClock.class.getDeclaredField( "fpsDelta3" );

            modifierField.setAccessible( true );
            scaleField.setAccessible( true );
            delta9Field.setAccessible( true );
            delta3Field.setAccessible( true );

            assertEquals( "|Values did not match|" , 59.727500569606D , modifierField.get( clock ) );
            assertEquals( "|Values did not match|" , 1.0F , scaleField.get( clock ) );
            assertEquals( "|Values did not match|" , ( long ) ( 1.0E9D * Math.pow( 59.727500569606D , - 1 ) ) , delta9Field.get( clock ) );
            assertEquals( "|Values did not match|" , ( long ) ( 1.0E9D * Math.pow( 59.727500569606D , - 1 ) * Math.pow( 1.0E6D , - 1 ) ) , delta3Field.get( clock ) );
        }
        catch ( IllegalAccessException | IllegalArgumentException | NoSuchFieldException | SecurityException exception )
        {
            throw new RuntimeException( exception );
        }
    }

    /**
     * Tests whether the maximum modifier is set resulting in the max delta possible
     */
    public void testSetMaxRate( )
    {
        try
        {
            RoutineClock clock = RoutineClock.from( 1.0D );
            clock.setMaxRate( );

            Field modifierField = RoutineClock.class.getDeclaredField( "fpsModifier" );
            Field scaleField = RoutineClock.class.getDeclaredField( "scale" );
            Field delta9Field = RoutineClock.class.getDeclaredField( "fpsDelta9" );
            Field delta3Field = RoutineClock.class.getDeclaredField( "fpsDelta3" );

            modifierField.setAccessible( true );
            scaleField.setAccessible( true );
            delta9Field.setAccessible( true );
            delta3Field.setAccessible( true );

            assertEquals( "|Values did not match|" , 200.0D , modifierField.get( clock ) );
            assertEquals( "|Values did not match|" , 1.0F , scaleField.get( clock ) );
            assertEquals( "|Values did not match|" , ( long ) ( 1.0E9D * Math.pow( 200.0D , - 1 ) ) , delta9Field.get( clock ) );
            assertEquals( "|Values did not match|" , ( long ) ( 1.0E9D * Math.pow( 200.0D , - 1 ) * Math.pow( 1.0E6D , - 1 ) ) , delta3Field.get( clock ) );
        }
        catch ( IllegalAccessException | IllegalArgumentException | NoSuchFieldException | SecurityException exception )
        {
            throw new RuntimeException( exception );
        }
    }

}