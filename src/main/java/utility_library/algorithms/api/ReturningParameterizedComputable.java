/*
 * Copyright (c) 2022. The Ceragine project files, related assets and related source code
 * were created by, Ogesoka (Oge) Tasie and should not be copied or distributed without
 * consent of the creator (Ogesoka Tasie). All Rights Reserved.
 */

package utility_library.algorithms.api;

import game_debugger.Alert;
import game_debugger.GameDebugger;

/**
 * A functional interface to be implemented when a unit of work on some parameter that produces a return value needs to be computed
 *
 * @param <P> the parameter object type
 * @param <R> the computations return type
 */
@FunctionalInterface
public interface ReturningParameterizedComputable< P, R > extends ParameterizedComputable< P >
{

    /**
     * Executes the computation function. A parameter is provided to the computation and a value is returned
     *
     * @param parameter the specified {@link P parameter} to provide to the function
     *
     * @return the {@link R result} of the function
     */
    R computeAndGet( P parameter );

    /**
     * {@inheritDoc}
     */
    @Override
    default void compute( P parameter )
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