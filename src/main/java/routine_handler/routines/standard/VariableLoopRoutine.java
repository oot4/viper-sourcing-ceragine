/*
 * Copyright (c) 2022. The Ceragine project files, related assets and related source code were created by, Ogesoka (Oge) Tasie and should not be copied or distributed without consent of the creator (Ogesoka Tasie). All Rights Reserved.
 */

package routine_handler.routines.standard;

import game_debugger.Alert;
import game_debugger.GameDebugger;
import routine_handler.components.core.RoutineClock;
import routine_handler.routines.api.Tracker;
import utility_library.algorithms.core.Clamp;
import utility_library.algorithms.core.Quotient;
import utility_library.annotations.Note;

/**
 * An abstract class to be extended by a routine with a variable loop time
 */
public abstract class VariableLoopRoutine extends LoopRoutine
{

    /**
     * Number of updates
     */
    private long updates = 0L;

    /**
     * Number of frames
     */
    private long frames = 0L;

    /**
     * The amount of lag between updates
     */
    private long updateLag = 0L;

    /**
     * The amount of lag between frames
     */
    private long frameLag = 0L;

    /**
     * The marginal error between the long and double value of the duration of the routines rest period
     */
    private double marginalError = 0.0D;

    /**
     * How far between updates the routine is
     */
    private long interpolation = 0L;

    /**
     * Is the routine updated
     */
    private boolean updated = false;

    /**
     * Clock to maintain the execution time of the routine
     */
    private final RoutineClock clock;

    /**
     * Default constructor for creating a new instance of a {@code VariableLoopRoutine} with standard clock speed and default name
     */
    protected VariableLoopRoutine( )
    {
        super( "unnamed_vl_routine" );
        clock = RoutineClock.standard( );
    }

    /**
     * Default constructor for creating a new instance of a {@code VariableLoopRoutine} with standard clock speed and a specified name
     *
     * @param name the specified name of the thread
     */
    protected VariableLoopRoutine( String name )
    {
        super( name );
        clock = RoutineClock.standard( );
    }

    /**
     * Default constructor for creating a new instance of a {@code VariableLoopRoutine} with clock speed set to the target frames per second and the thread given a name
     *
     * @param name      the specified name of the thread
     * @param targetFPS the target frames per second
     */
    protected VariableLoopRoutine( String name , double targetFPS )
    {
        super( name );
        clock = RoutineClock.from( targetFPS );
    }

    /**
     * Gets the clock timing the current routine
     *
     * @return the attached {@link RoutineClock routine clock} dictating the speed of the routine
     */
    public final RoutineClock getClock( )
    {
        return clock;
    }

    /**
     * Begins the current update
     */
    protected abstract void begin( );

    /**
     * Executes the main body of the current update
     */
    protected abstract void execute( );

    /**
     * Finishes up the current update
     *
     * @param interpolation the specified value of interpolation
     * @param updated       is the routine updated?
     */
    protected abstract void finish( long interpolation , boolean updated );

    /**
     * Gets the tracker function provided by the loop routine
     *
     * @return a tracker function for monitoring updates and frames
     */
    protected Tracker tracker( )
    {
        return ( trackedUpdates , trackedFrames ) -> { };
    }

    /**
     * Puts the current thread to rest for a specified duration
     */
    @Note( "This method seems to be responsible for performance loss across the system" )
    private void rest( )
    {
        long duration = ( clock.getDelta9( ) + clock.getFpsDelta9( ) ) - ( 2 * getOverhead( ) );
        double durationD = Clamp.toLong( duration , 0 , duration );
        durationD = Quotient.toDouble( durationD , RoutineClock.MILLI_CONVERSION_FACTOR );
        long durationL = Math.round( durationD );
        //margin of error
        marginalError += durationD - durationL;
        //adjust error
        if ( Math.abs( marginalError ) > 1.0D )
        {
            long adjustment = ( long ) marginalError;
            durationL += adjustment;
            marginalError -= adjustment;
        }
        if ( durationD > 0.0D )
        {
            try
            {
                Thread.sleep( durationL );
            }
            catch ( InterruptedException e )
            {
                GameDebugger.log( Alert.DIRE , "Thread was interrupted while trying to sleep" , e );
                Thread.currentThread( ).interrupt( );
            }
        }
    }

    /**
     * Begins the current update
     */
    private void internalBegin( )
    {
        begin( );
    }

    /**
     * Executes the main body of the current update
     */
    private void internalExecute( )
    {
        super.update( );
        execute( );
    }

    /**
     * Finishes up the current update
     */
    private void internalFinish( )
    {
        finish( interpolation , updated );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected final void update( )
    {
        if ( clock.getScale( ) > 0 )
        {
            updateLag += getOverhead( );
            while ( updateLag >= clock.getDelta9( ) )
            {
                internalBegin( );
                internalExecute( );
                updateLag -= clock.getDelta9( );
                updates++;
            }
            interpolation = Quotient.toLong( updateLag , clock.getDelta9( ) );
            updated = true;
        }
        frameLag += getOverhead( );
        while ( frameLag >= clock.getFpsDelta9( ) )
        {
            internalFinish( );
            frameLag -= clock.getFpsDelta9( );
            updated = false;
            frames++;
        }
        if ( track( updates , frames , tracker( ) ) )
        {
            updates = 0;
            frames = 0;
        }
        rest( );
    }

}