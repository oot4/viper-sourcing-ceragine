/*
 * Copyright (c) 2022. The Ceragine project files, related assets and related source code
 * were created by, Ogesoka (Oge) Tasie and should not be copied or distributed without
 * consent of the creator (Ogesoka Tasie). All Rights Reserved.
 */

package routine_handler.routines.api;

/**
 * An interface to be implemented when a process needs to be launched
 */
public interface Launchable extends Subroutine
{

    /**
     * Initializes the routine. Should be used post-launch
     */
    void initialize( );

    /**
     * Terminates the routine. This should be called post-initialization
     */
    void terminate( );

    /**
     * {@inheritDoc}
     */
    @Override
    default void perform( )
    {
        //Do nothing
    }

}