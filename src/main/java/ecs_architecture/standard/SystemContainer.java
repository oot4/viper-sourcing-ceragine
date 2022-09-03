/*
 * Copyright (c) 2022. The Ceragine project files, related assets and related source code
 * were created by, Ogesoka (Oge) Tasie and should not be copied or distributed without
 * consent of the creator (Ogesoka Tasie). All Rights Reserved.
 */

package ecs_architecture.standard;

import ecs_architecture.api.GameplayAPI;
import game_debugger.Alert;
import game_debugger.GameDebugger;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * An abstract class to be extended by a container of systems
 */
abstract class SystemContainer extends ComponentContainer
{

    /**
     * Map of game systems and their class type
     */
    private final Map< Class< ? extends GameSystem >, GameSystem > systems = new ConcurrentHashMap<>( );

    /**
     * Default constructor for creating a new {@code SystemContainer}
     */
    protected SystemContainer( ) { }

    /**
     * Validates the specified game system class type against some conditions
     *
     * @param type the specified game system class type to validate
     *
     * @throws NullPointerException     throws an exception if the provided game system is null
     * @throws IllegalArgumentException throws an exception if the provided game system does not exist in the system container
     */
    private void validateType( Class< ? extends GameSystem > type )
    {
        if ( type == null )
        {
            GameDebugger.log( Alert.WARN , "the game system cannot be null, cannot have a null class type" );
            throw new NullPointerException( "the game system cannot be null, cannot have a class type" );
        }
        if ( ! systems.containsKey( type ) )
        {
            GameDebugger.log( Alert.WARN , "the game system does not exist" );
            throw new IllegalArgumentException( "the game system does not exist" );
        }
    }

    /**
     * Gets the map of game systems
     *
     * @return a map of game system classes to game system objects
     */
    protected final Map< Class< ? extends GameSystem >, GameSystem > systems( )
    {
        return systems;
    }

    /**
     * Adds game systems to the system container. The game system is linked up to the provided gameplay API
     *
     * @param gameplayAPI the specified {@link GameplayAPI API} the game system will be linked to
     * @param gameSystems the specified {@link GameSystem system} to add
     */
    protected final void internalAdd( GameplayAPI gameplayAPI , GameSystem... gameSystems )
    {
        Arrays.stream( gameSystems ).filter( Objects::nonNull ).forEach( gameSystem -> {
            gameSystem.setGameplayAPI( gameplayAPI );
            systems.putIfAbsent( gameSystem.getClass( ) , gameSystem );
        } );
    }

    /**
     * {@inheritDoc}.
     * This occurs only if the system is disabled
     */
    @Override
    public final void activate( Class< ? extends GameSystem > type )
    {
        validateType( type );
        boolean activate = ! systems.get( type ).isEnabled( );
        if ( activate )
        {
            systems.get( type ).setEnabled( true );
            systems.get( type ).initialize( );
        }
    }

    /**
     * {@inheritDoc}.
     * This occurs only if the system is enabled
     */
    @Override
    public final void deactivate( Class< ? extends GameSystem > type )
    {
        validateType( type );
        boolean deactivate = systems.get( type ).isEnabled( );
        if ( deactivate )
        {
            systems.get( type ).terminate( );
            systems.get( type ).setEnabled( false );
        }
    }

}