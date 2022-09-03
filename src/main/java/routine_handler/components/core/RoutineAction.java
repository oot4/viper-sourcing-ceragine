/*
 * Copyright (c) 2022. The Ceragine project files, related assets and related source code
 * were created by, Ogesoka (Oge) Tasie and should not be copied or distributed without
 * consent of the creator (Ogesoka Tasie). All Rights Reserved.
 */

package routine_handler.components.core;

import utility_library.algorithms.api.Computable;

/**
 * A class representing an action that occurs based on some time to execute during a routine
 */
public final class RoutineAction
{

    /**
     * The time to execute the action
     */
    private final long executionTime;

    /**
     * The computable action to execute
     */
    private final Computable computable;

    /**
     * Creates a new instance of a {@code RoutineAction}
     *
     * @param executionTime the specified time the action should execute
     * @param computable    the specified action to perform once conditions are met
     */
    public RoutineAction( long executionTime , Computable computable )
    {
        this.executionTime = executionTime;
        this.computable = computable;
    }

    /**
     * Computes the operation for the action
     */
    public void compute( )
    {
        computable.compute( );
    }

    /**
     * Gets the time of execution for the action
     *
     * @return the time the action should execute
     */
    public long getExecutionTime( )
    {
        return executionTime;
    }

}