/*
 * Copyright (c) 2022. The Ceragine project files, related assets and related source code
 * were created by, Ogesoka (Oge) Tasie and should not be copied or distributed without
 * consent of the creator (Ogesoka Tasie). All Rights Reserved.
 */

package utility_library.algorithms.api;

import game_debugger.Alert;
import game_debugger.GameDebugger;

/**
 * A functional interface to be implemented when a unit of work that produces a return value needs to be computed
 *
 * @param <R> the computations return type
 */
@FunctionalInterface
public interface ReturningComputable< R > extends Computable
{

    /**
     * Executes the computation function. A value is returned
     *
     * @return the {@link R result} of the computation
     */
    R computeAndGet( );


    /**
     * {@inheritDoc}
     */
    @Override
    default void compute( )
    {
        try
        {
            throw new UnsupportedOperationException( );
        }
        catch ( UnsupportedOperationException exception )
        {
            GameDebugger.log( Alert.WARN , "Compute function has no implementation" );
        }
    }

}