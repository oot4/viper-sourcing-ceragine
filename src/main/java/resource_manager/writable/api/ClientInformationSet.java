/*
 * Copyright (c) 2022. The Ceragine project files, related assets and related source code
 * were created by, Ogesoka (Oge) Tasie and should not be copied or distributed without
 * consent of the creator (Ogesoka Tasie). All Rights Reserved.
 */

package resource_manager.writable.api;

/**
 * An interface to be implemented by a container of client specific information
 */
public interface ClientInformationSet
{

    /**
     * Gets the current modifier used to adjust delta time
     *
     * @return the number of expected ticks in one second
     */
    double fpsModifier( );

    /**
     * Sets the current modifier used to adjust delta time to the value specified
     *
     * @param fpsModifier the specified value to set the modifier to
     */
    void setFpsModifier( double fpsModifier );

}