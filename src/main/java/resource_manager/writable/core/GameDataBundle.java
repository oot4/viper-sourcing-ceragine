/*
 * Copyright (c) 2022. The Ceragine project files, related assets and related source code
 * were created by, Ogesoka (Oge) Tasie and should not be copied or distributed without
 * consent of the creator (Ogesoka Tasie). All Rights Reserved.
 */

package resource_manager.writable.core;

import ceragine.Game;
import game_debugger.Alert;
import game_debugger.GameDebugger;
import resource_manager.writable.api.WritableResource;
import resource_manager.writable.standard.DataBundle;

/**
 * A class representing a data bundle with game resources
 */
@WritableResource( filename = "mydatabundle" )
public class GameDataBundle extends DataBundle
{

    /**
     * Is the data bundle initialized?
     */
    private boolean initialized = false;

    /**
     * Constructor for creating a new {@code GameDataBundle}
     */
    protected GameDataBundle( ) { }

    /**
     * Initializes the game data bundle from file
     *
     * @param game the specified game to this game data bundle is being initialized for
     *
     * @throws UnsupportedOperationException throws an exception if the game provided is null or if the data bundle is already initialized
     */
    public final void initializeFromFile( Game game )
    {
        if ( game == null || initialized )
        {
            throw new UnsupportedOperationException( "This method was called illegally" );
        }
        boolean unsuccessful = this.read( );
        if ( unsuccessful )
        {
            String filename = this.getClass( ).getAnnotation( WritableResource.class ).filename( );
            String extension = this.getClass( ).getAnnotation( WritableResource.class ).extension( );
            GameDebugger.log( Alert.DIRE , "File was not found, '" + filename + "." + extension + "' does not exist and no game resources could be loaded" );
        }
        initialized = ! unsuccessful;
    }

    /**
     * Is the data bundle initialized?
     *
     * @return {@code True} if the game data bundle has been initialized. {@code False} otherwise
     */
    public final boolean isInitialized( )
    {
        return initialized;
    }

}