/*
 * Copyright (c) 2022. The Ceragine project files, related assets and related source code
 * were created by, Ogesoka (Oge) Tasie and should not be copied or distributed without
 * consent of the creator (Ogesoka Tasie). All Rights Reserved.
 */

package resource_manager.writable.api;

/**
 * An interface to be implemented by a container of debug specific information
 */
public interface DebugInformationSet
{

    /**
     * Is debugging mode enabled?
     *
     * @return {@code True} if debugging mode is enabled. {@code False} otherwise
     */
    boolean debugMode( );

    /**
     * Sets the debugging mode to {@code True} or {@code False}
     *
     * @param debugMode is debugging mode enabled?
     */
    void enableDebugMode( boolean debugMode );

    /**
     * Gets the rate at which debug information is displayed to the console
     *
     * @return the expected rate at which to show debug information
     */
    int debugRate( );

    /**
     * Sets the rate at which debug information is displayed to the console
     *
     * @param debugRate the specified rate ate which to set debug information
     */
    void setDebugRate( int debugRate );

}