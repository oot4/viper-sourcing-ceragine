/*
 * Copyright (c) 2022. The Ceragine project files, related assets and related source code
 * were created by, Ogesoka (Oge) Tasie and should not be copied or distributed without
 * consent of the creator (Ogesoka Tasie). All Rights Reserved.
 */

package routine_handler.components.core;

import routine_handler.routines.standard.VariableLoopRoutine;
import utility_library.algorithms.core.Clamp;
import utility_library.algorithms.core.Quotient;

import java.text.MessageFormat;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import static game_debugger.GameDebugger.validateCondition;

/**
 * A class representing a clock for routines. This is used to adjust the speed at which a variable loop {@link VariableLoopRoutine routine} performs its updates
 */
public final class RoutineClock
{

    /**
     * Modifier to adjust the tick rate
     */
    private double modifier = 0.0D;

    /**
     * Modifier to adjust the frames per second
     */
    private double fpsModifier = 0.0D;

    /**
     * Scales the routine clock rate to allow the rate to speed u or down
     */
    private float scale = 0.0F;

    /**
     * Delta time in nanoseconds
     */
    private long delta9 = 0L;

    /**
     * Delta time in nanoseconds for the fps
     */
    private long fpsDelta9 = 0L;

    /**
     * Delta time in milliseconds
     */
    private long delta3 = 0L;

    /**
     * Delta time in nanoseconds for the fps
     */
    private long fpsDelta3 = 0L;

    /**
     * The amount of nanoseconds in one second
     */
    public static final long ONE_SECOND = 1000000000L;

    /**
     * A value used to convert between nanoseconds and milliseconds
     */
    public static final long MILLI_CONVERSION_FACTOR = 1000000L;

    /**
     * A value to convert milliseconds to microseconds
     */
    public static final long MICRO_CONVERSION_FACTOR = 1000L;

    /**
     * The minimum value a specified modifier can be
     */
    private static final double MINIMUM_MODIFIER = 1.0D;

    /**
     * The standard modifier value
     */
    private static final double BASE_MODIFIER = 59.727500569606D;

    /**
     * The maximum value a specified modifier can be
     */
    private static final double MAXIMUM_MODIFIER = 200.0D;

    /**
     * The minimum value a specified scale can be
     */
    private static final float MINIMUM_SCALE = 0.0F;

    /**
     * The standard scale value
     */
    private static final float BASE_SCALE = 1.0F;

    /**
     * The maximum value a specified scale can be
     */
    private static final float MAXIMUM_SCALE = 2.5F;

    /**
     * Set of game clocks for synchronization
     */
    private final Set< RoutineClock > clocks = ConcurrentHashMap.newKeySet( );

    /**
     * Creates a new instance of a {@code RoutineClock}
     */
    private RoutineClock( )
    {
    }

    /**
     * Creates and returns a new instance of a {@code RoutineClock} object using the {@link #BASE_MODIFIER base modifier}
     *
     * @return a new instance as a {@link RoutineClock routine clock}
     */
    public static RoutineClock standard( )
    {
        return from( BASE_MODIFIER );
    }

    /**
     * Creates and returns a new instance of a {@code RoutineClock} object
     *
     * @param modifier the specified modifier used to dictate the delta time
     *
     * @return a new instance as a {@link RoutineClock routine clock}
     */
    public static RoutineClock from( double modifier )
    {
        RoutineClock clock = new RoutineClock( );
        clock.setRate( modifier );
        clock.setScale( BASE_SCALE );
        return clock;
    }

    /**
     * Attaches a clock which will be synchronized with the parent
     *
     * @param clock the specified clock to synchronize
     */
    public void attach( RoutineClock clock )
    {
        validateCondition( ( ) -> clock == null , ( ) -> clocks.add( clock ) , MessageFormat.format( "cannot attach {0}" , clock ) );
    }

    /**
     * Detaches a clock which was synchronized with the parent
     *
     * @param clock the specified clock to detach
     */
    public void detach( RoutineClock clock )
    {
        validateCondition( ( ) -> clock == null , ( ) -> clocks.remove( clock ) , MessageFormat.format( "cannot detach {0}" , clock ) );
    }

    /**
     * Gets the modifier used to dictate the delta time
     *
     * @return the number of expected ticks in one second
     */
    public double getModifier( )
    {
        return modifier;
    }

    /**
     * Gets the modifier used to dictate the frames per second
     *
     * @return the number of expected frames per second
     */
    public double getFpsModifier( )
    {
        return fpsModifier;
    }

    /**
     * Gets the scale value applied to the routine clock tick rate
     *
     * @return the scale value used to modify the delta time
     */
    public float getScale( )
    {
        return scale;
    }

    /**
     * Gets the delta time in nanoseconds
     *
     * @return the change in time per tick in nanoseconds
     */
    public long getDelta9( )
    {
        return delta9;
    }

    /**
     * Gets the delta time in nanoseconds for the fps
     *
     * @return the change in time per frame in nanoseconds
     */
    public long getFpsDelta9( )
    {
        return fpsDelta9;
    }

    /**
     * Gets the delta time in milliseconds
     *
     * @return the change in time per tick in milliseconds
     */
    public long getDelta3( )
    {
        return delta3;
    }

    /**
     * Gets the delta time in milliseconds for the fps
     *
     * @return the change in time per frame in milliseconds
     */
    public long getFpsDelta3( )
    {
        return fpsDelta3;
    }

    /**
     * Sets the rate of the routine clock
     *
     * @param modifier the specified modifier to apply to the rate
     */
    public void setRate( double modifier )
    {
        this.modifier = Clamp.toDouble( BASE_MODIFIER , MINIMUM_MODIFIER , MAXIMUM_MODIFIER );
        fpsModifier = Clamp.toDouble( modifier , MINIMUM_MODIFIER , MAXIMUM_MODIFIER );
        adjustDelta( );
        for ( RoutineClock clock : clocks )
        {
            clock.setRate( modifier );
        }
    }

    /**
     * Sets the scale value applied to the routine clock tick rate
     *
     * @param scale the specified scale value used to modify the delta time
     */
    public void setScale( float scale )
    {
        this.scale = Clamp.toFloat( scale , MINIMUM_SCALE , MAXIMUM_SCALE );
        adjustDelta( );
        for ( RoutineClock clock : clocks )
        {
            clock.setScale( scale );
        }
    }

    /**
     * Adjust the delta based on the current modifier and scale of the routine clock
     */
    private void adjustDelta( )
    {
        final float scaleModifier = Clamp.toFloat( scale , BASE_SCALE , MAXIMUM_SCALE ) * ( float ) modifier;
        final float scaleFpsModifier = Clamp.toFloat( BASE_SCALE , BASE_SCALE , MAXIMUM_SCALE ) * ( float ) fpsModifier;
        delta9 = ( long ) Quotient.toDouble( ONE_SECOND , scaleModifier );
        fpsDelta9 = ( long ) Quotient.toDouble( ONE_SECOND , scaleFpsModifier );
        delta3 = Quotient.toLong( delta9 , MILLI_CONVERSION_FACTOR );
        fpsDelta3 = Quotient.toLong( fpsDelta9 , MILLI_CONVERSION_FACTOR );
    }

    /**
     * Sets the rate of the routine clock to the minimum
     */
    public void setMinRate( )
    {
        setRate( MINIMUM_MODIFIER );
        setScale( BASE_SCALE );
    }

    /**
     * Sets the rate of the routine clock to the standard
     */
    public void setBaseRate( )
    {
        setRate( BASE_MODIFIER );
        setScale( BASE_SCALE );
    }

    /**
     * Sets the rate of the routine clock to the maximum
     */
    public void setMaxRate( )
    {
        setRate( MAXIMUM_MODIFIER );
        setScale( BASE_SCALE );
    }

}