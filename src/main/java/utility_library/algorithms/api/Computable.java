/*
 * Copyright (c) 2022. The Ceragine project files, related assets and related source code
 * were created by, Ogesoka (Oge) Tasie and should not be copied or distributed without
 * consent of the creator (Ogesoka Tasie). All Rights Reserved.
 */

package utility_library.algorithms.api;

/**
 * A functional interface to be implemented when a unit of work needs to be computed
 */
@FunctionalInterface
public interface Computable
{

    /**
     * Executes the computation function
     */
    void compute( );

}