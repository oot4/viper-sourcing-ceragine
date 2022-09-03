/*
 * Copyright (c) 2022. The Ceragine project files, related assets and related source code
 * were created by, Ogesoka (Oge) Tasie and should not be copied or distributed without
 * consent of the creator (Ogesoka Tasie). All Rights Reserved.
 */

package utility_library.algorithms.api;

/**
 * A functional interface to be implemented when a unit of work on some parameter needs to be computed
 *
 * @param <P> the parameter object type
 */
@FunctionalInterface
public interface ParameterizedComputable< P >
{

    /**
     * Executes the computation function. A parameter is provided to the computation
     *
     * @param parameter the specified {@link P parameter} to provide to the function
     */
    void compute( P parameter );

}