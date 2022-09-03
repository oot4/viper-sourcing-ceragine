/*
 * Copyright (c) 2022. The Ceragine project files, related assets and related source code
 * were created by, Ogesoka (Oge) Tasie and should not be copied or distributed without
 * consent of the creator (Ogesoka Tasie). All Rights Reserved.
 */

package routine_handler.routines.api;

/**
 * A functional interface to be implemented when an update cycle for a subroutine needs to be performed
 */
@FunctionalInterface
public interface Subroutine
{

    /**
     * Executes the {@code Subroutine} operation
     */
    void perform( );

}