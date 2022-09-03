/*
 * Copyright (c) 2022. The Ceragine project files, related assets and related source code
 * were created by, Ogesoka (Oge) Tasie and should not be copied or distributed without
 * consent of the creator (Ogesoka Tasie). All Rights Reserved.
 */

package routine_handler.components.api;

import routine_handler.routines.api.Subroutine;

/**
 * A functional interface to be implemented when a rendering process needs to be performed
 */
@FunctionalInterface
public interface RenderListener extends Subroutine
{

    /**
     * Executes the instructions defined for a rendering process
     *
     * @param interpolation an interpolation value used for smoothing the rendering output
     */
    void render( double interpolation );

    /**
     * {@inheritDoc}
     */
    @Override
    default void perform( )
    {
        //Do nothing
    }

}