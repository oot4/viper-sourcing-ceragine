/*
 * Copyright (c) 2022. The Ceragine project files, related assets and related source code
 * were created by, Ogesoka (Oge) Tasie and should not be copied or distributed without
 * consent of the creator (Ogesoka Tasie). All Rights Reserved.
 */

package routine_handler.routines.api;

/**
 * An interface to be implemented when a routine should be performed
 */
public interface Routine extends Launchable
{

    /**
     * Attaches a subroutine as part of the routine
     *
     * @param subroutine the specified {@link Subroutine subroutine} to attach
     */
    void attach( Subroutine subroutine );

    /**
     * Detaches a subroutine from the routine
     *
     * @param subroutine the specified {@link Subroutine subroutine} to detach
     */
    void detach( Subroutine subroutine );

    /**
     * Gets the total number of attached subroutines
     *
     * @return the tallied number of subroutines performed in this routine
     */
    int size( );

    /**
     * {@inheritDoc}
     */
    @Override
    default void initialize( )
    {
        //Do nothing
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default void terminate( )
    {
        //Do nothing
    }

}