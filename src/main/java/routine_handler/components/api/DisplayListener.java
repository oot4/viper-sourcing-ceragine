/*
 * Copyright (c) 2022. The Ceragine project files, related assets and related source code
 * were created by, Ogesoka (Oge) Tasie and should not be copied or distributed without
 * consent of the creator (Ogesoka Tasie). All Rights Reserved.
 */

package routine_handler.components.api;

/**
 * A functional interface to be implemented when an image needs to be displayed
 */
@FunctionalInterface
public interface DisplayListener
{

    /**
     * Executes the instructions defined for displaying an image
     */
    void display( );

}