/*
 * Copyright (c) 2022. The Ceragine project files, related assets and related source code were created by, Ogesoka (Oge) Tasie and should not be copied or distributed without consent of the creator (Ogesoka Tasie). All Rights Reserved.
 */

package utility_library.structures;

import routine_handler.routines.api.Launchable;

/**
 * A class representing a counter of frames
 */
public final class Counter implements Launchable
{

    /**
     * Current frame
     */
    private long frame;

    /**
     * Maximum frame count before resetting {@link Counter#frame frames}
     */
    private long max = - 1;

    /**
     * Constructor for creating a new instance of a {@code Counter}
     */
    private Counter( )
    {
    }

    /**
     * Creates a new instance of a {@code Counter}
     *
     * @param capacity the specified capacity to give to the counter
     *
     * @return a new instance as a {@link Counter counter} with the specified capacity
     */
    public static Counter from( long capacity )
    {
        Counter counter = new Counter( );
        counter.max = capacity;
        return counter;
    }

    /**
     * Creates a new instance of a counter
     *
     * @return a new instance as a {@link Counter counter}
     */
    public static Counter from( )
    {
        return new Counter( );
    }

    /**
     * Should the counter start updating?
     *
     * @return {@code True} if the frame {@link Counter#max capacity} is greater than 0. {@code False} otherwise
     */
    private boolean canUpdate( )
    {
        return max > 0;
    }

    /**
     * Has the specified interval been reached?
     *
     * @param interval the specified interval to test against
     *
     * @return {@code True} if {@code frame % interval == 1}. {@code False} otherwise
     */
    public boolean reachedInterval( long interval )
    {
        setMaxIfGreater( interval );
        if ( interval == 0 || interval == 1 )
        {
            return true;
        }
        return frame % interval == 1;
    }

    /**
     * Gets the current frame
     *
     * @return a number corresponding to the number of frames incremented
     */
    public long currentFrame( )
    {
        return frame;
    }

    /**
     * The maximum number of frames this counter can reach before the current {@link Counter#frame frames} are reset
     *
     * @return the frame capacity of the counter
     */
    public long max( )
    {
        return max;
    }

    /**
     * Sets the maximum number of frames this counter can reach before the current {@link Counter#frame frames} are reset
     *
     * @param max the specified capacity to give the counter
     */
    public void setMaxExact( long max )
    {
        this.max = max;
    }

    /**
     * Sets the maximum number of frames this counter can reach before the current {@link Counter#frame frames} are reset. This differs to {@code Counter#setMaxExact setMaxExact(long)} in that the value is only set if it is greater than the current max
     *
     * @param max the specified capacity to give the counter
     */
    public void setMaxIfGreater( long max )
    {
        if ( max > this.max )
        {
            setMaxExact( max );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void initialize( )
    {
        frame = - 1;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void terminate( )
    {
        initialize( );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void perform( )
    {
        if ( canUpdate( ) )
        {
            frame++;
        }
        if ( frame > max )
        {
            frame = 0;
        }
    }

}