/*
 * Copyright (c) 2022. The Ceragine project files, related assets and related source code
 * were created by, Ogesoka (Oge) Tasie and should not be copied or distributed without
 * consent of the creator (Ogesoka Tasie). All Rights Reserved.
 */

package resource_manager.writable.core;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;
import game_debugger.Alert;
import game_debugger.GameDebugger;
import resource_manager.writable.api.WritableResource;
import resource_manager.writable.standard.WritableBundle;

/**
 * An abstract class to be extended by bundle of configuration data
 */
@WritableResource( filename = "config" )
public final class ConfigurationBundle extends WritableBundle
{

    /**
     * Client information bundle
     */
    private ClientInfoBundle clientInformation;

    /**
     * Debug information bundle
     */
    private DebugInfoBundle debugInformation;

    /**
     * Graphics information bundle
     */
    private GraphicsInfoBundle graphicsInformation;

    /**
     * Game information bundle
     */
    private GameInfoBundle gameInformation;

    /**
     * Constructor for creating a new {@code ConfigurationBundle}
     */
    private ConfigurationBundle( )
    {
        super( );
    }

    /**
     * Initializes and creates a new instance of a {@code ConfigurationBundle}
     *
     * @return a new instance of a {@link ConfigurationBundle configuration bundle}
     */
    public static ConfigurationBundle initializeFromFile( )
    {
        ConfigurationBundle configurationBundle = new ConfigurationBundle( );
        boolean unsuccessful = configurationBundle.read( );
        if ( unsuccessful )
        {
            configurationBundle.setClientInformation( ClientInfoBundle.standard( ) );
            configurationBundle.setDebugInformation( DebugInfoBundle.standard( ) );
            configurationBundle.setGraphicsInformation( GraphicsInfoBundle.standard( ) );
            configurationBundle.setGameInformation( GameInfoBundle.standard( ) );
            configurationBundle.writeToFile( );
            GameDebugger.log( Alert.INFO , "File was not found, default configuration file created instead" );
        }
        return configurationBundle;
    }

    /**
     * Gets the client information bundle
     *
     * @return a client information container
     */
    @JsonGetter( "client information" )
    public ClientInfoBundle clientInformation( )
    {
        return clientInformation;
    }

    /**
     * Sets the client information for the configuration bundle
     *
     * @param clientInformation the specified {@link ClientInfoBundle client information bundle} to set
     */
    @JsonSetter( "client information" )
    private void setClientInformation( ClientInfoBundle clientInformation )
    {
        this.clientInformation = clientInformation;
    }

    /**
     * Gets the debug information bundle
     *
     * @return a debug information container
     */
    @JsonGetter( "debug information" )
    public DebugInfoBundle debugInformation( )
    {
        return debugInformation;
    }

    /**
     * Sets the debug information for the configuration bundle
     *
     * @param debugInformation the specified {@link DebugInfoBundle debug information bundle} to set
     */
    @JsonSetter( "debug information" )
    private void setDebugInformation( DebugInfoBundle debugInformation )
    {
        this.debugInformation = debugInformation;
    }

    /**
     * Gets the graphics information bundle
     *
     * @return a graphics information container
     */
    @JsonGetter( "graphics information" )
    public GraphicsInfoBundle graphicsInformation( )
    {
        return graphicsInformation;
    }

    /**
     * Sets the graphics information for the configuration bundle
     *
     * @param graphicsInformation the specified {@link GraphicsInfoBundle graphics information bundle} to set
     */
    @JsonSetter( "graphics information" )
    private void setGraphicsInformation( GraphicsInfoBundle graphicsInformation )
    {
        this.graphicsInformation = graphicsInformation;
    }

    /**
     * Gets the game information bundle
     *
     * @return a game information container
     */
    @JsonGetter( "game information" )
    public GameInfoBundle gameInformation( )
    {
        return gameInformation;
    }

    /**
     * Sets the game information for the configuration bundle
     *
     * @param gameInformation the specified {@link GameInfoBundle game information bundle} to set
     */
    @JsonSetter( "game information" )
    private void setGameInformation( GameInfoBundle gameInformation )
    {
        this.gameInformation = gameInformation;
    }

}