/*
 * Copyright (c) 2022. The Ceragine project files, related assets and related source code
 * were created by, Ogesoka (Oge) Tasie and should not be copied or distributed without
 * consent of the creator (Ogesoka Tasie). All Rights Reserved.
 */

package utility_library.structures;

/**
 * A class representing a status or condition
 */
public final class Condition
{

    /**
     * Is the condition active?
     */
    private boolean active = false;

    /**
     * Creates a new instance of a {@code Condition}
     */
    private Condition( )
    {
    }

    /**
     * Creates a new instance of a {@code Condition}
     *
     * @return a new instance as a {@link Condition condition}
     */
    public static Condition newInstance( )
    {
        return new Condition( );
    }

    /**
     * Gets the state of the condition
     *
     * @return {@code True} if the condition is active. {@code False} otherwise
     */
    public boolean status( )
    {
        return active;
    }

    /**
     * Sets the state of the condition
     *
     * @param active the specified state of the status to set
     */
    public void set( boolean active )
    {
        this.active = active;
    }

    /**
     * Changes the current state of the condition to the opposite
     */
    public void change( )
    {
        set( ! active );
    }

}