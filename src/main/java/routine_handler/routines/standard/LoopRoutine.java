/*
 * Copyright (c) 2022. The Ceragine project files, related assets and related source code were created by, Ogesoka (Oge) Tasie and should not be copied or distributed without consent of the creator (Ogesoka Tasie). All Rights Reserved.
 */

package routine_handler.routines.standard;

import routine_handler.components.core.RoutineClock;
import routine_handler.routines.api.Tracker;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * An abstract class to be extended by a routine that runs in a loop
 */
public abstract class LoopRoutine extends ThreadRoutine
{

    /**
     * Flag indicating whether the routine is actively running
     */
    private boolean running = false;

    /**
     * Current cycle number of the routine
     */
    private long cycle = 0L;

    /**
     * Used to time the execution time of the loop in one second
     */
    private long timer = 0L;

    /**
     * Processing time for a single loop
     */
    private long overhead = 0L;

    /**
     * Shared resource access control lock
     */
    private final Lock lock = new ReentrantLock( );

    /**
     * Default constructor for creating a new {@code LoopedRoutine}
     */
    protected LoopRoutine( )
    {
        this( "unnamed_l_routine" );
    }

    /**
     * Default constructor for creating a new {@code LoopedRoutine}
     *
     * @param name the name of the thread handling the routine
     */
    protected LoopRoutine( String name )
    {
        super( name );
    }

    /**
     * Gets the current number of cycles that have started executing, increments when a cycle is started
     *
     * @return the current cycle number
     */
    public final long getCycle( )
    {
        return cycle;
    }

    /**
     * Gets the processing time taken to complete one loop routine
     *
     * @return the total time in nanoseconds to complete the last loop of the routine
     */
    public final long getOverhead( ) { return overhead; }

    /**
     * Gets the lock that controls access to shared resources between multiple threads
     *
     * @return the lock owned by the looped routine
     */
    public final Lock getLock( )
    {
        return lock;
    }

    /**
     * Gets the current value of the high-resolution time source, in nanoseconds, owned by the currently running Java Virtual Machine
     *
     * @return the value of the high-resolution time source
     */
    private static long nanos( )
    {
        return System.nanoTime( );
    }

    /**
     * Informs the current routine to not attempt another loop of operations
     */
    public final void exitLoop( )
    {
        running = false;
    }

    /**
     * Runs extra initialization as part of its internal initialization process execution
     */
    protected abstract void onInitialize( );

    /**
     * {@inheritDoc}
     */
    @Override
    protected final void internalInitialize( )
    {
        onInitialize( );
        running = true;
    }

    /**
     * Runs an update process as part of the internal process execution
     */
    protected void update( )
    {
        super.internalPerform( );
    }

    /**
     * Prints the total number of updates and frames per second
     *
     * @param updates the specified number of updates to print
     * @param frames  the specified number of frames to print
     * @param tracker the specified update and frame tracker
     *
     * @return {@code True} if the number of updates and frames were printed after one second. {@code False} otherwise
     */
    protected final boolean track( long updates , long frames , Tracker tracker )
    {
        if ( timer >= RoutineClock.ONE_SECOND )
        {
            tracker.track( updates , frames );
            timer = 0;
            return true;
        }
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected final void internalPerform( )
    {
        while ( running )
        {
            final long start = nanos( );
            ++ cycle;
            Lock internalLock = lock;
            internalLock.lock( );
            try
            {
                update( );
            }
            finally
            {
                internalLock.unlock( );
            }
            overhead = nanos( ) - start;
            timer += overhead;
        }
    }

}