/*
 * Copyright (c) 2022. The Ceragine project files, related assets and related source code
 * were created by, Ogesoka (Oge) Tasie and should not be copied or distributed without
 * consent of the creator (Ogesoka Tasie). All Rights Reserved.
 */

package ecs_architecture.standard;

import ecs_architecture.api.GameplayAPI;
import game_debugger.Alert;
import game_debugger.GameDebugger;
import utility_library.algorithms.core.RandomString;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * An abstract class to be extended by a container of entities
 */
abstract class EntityContainer implements GameplayAPI
{

    /**
     * Maximum entity capacity
     */
    private long max = 1024;

    /**
     * Set of entities
     */
    private final Set< String > entities = ConcurrentHashMap.newKeySet( );

    /**
     * Default constructor for creating a new {@code EntityContainer}
     */
    protected EntityContainer( )
    {
    }

    /**
     * Checks whether the specified entity id exists in the current set of entities
     *
     * @param id the specified entity id whose presence is to be tested
     *
     * @return {@code True} if the element is present. {@code False} otherwise
     */
    protected final boolean contains( String id )
    {
        return entities.contains( id );
    }

    /**
     * Gets a randomly generated ID
     *
     * @return a randomly generated string acting as an entity ID
     */
    private String newID( )
    {
        String id = RandomString.randStr8( ) + RandomString.randInt8( );
        if ( entities.contains( id ) )
        {
            return newID( );
        }
        return id;
    }

    /**
     * Adds a new entity
     *
     * @return a randomly generated string identifying the new entity
     *
     * @throws IndexOutOfBoundsException throws an exception is the maximum allowed entities is reached
     */
    protected final String internalAdd( )
    {
        if ( entities.size( ) == max )
        {
            if ( max == 2048 )
            {
                GameDebugger.log( Alert.DIRE , "Max number of entities reached, clear some unused entities" );
                throw new IndexOutOfBoundsException( "Max number of entities reached, clear some unused entities" );
            }
            max += 512;
        }
        String id = newID( );
        entities.add( id );
        return id;
    }

    /**
     * Removes an entity
     *
     * @param id the specified ID of the entity to remove
     *
     * @return {@code True} if the entity was successfully removed. {@code False} otherwise
     */
    protected boolean internalRemove( String id )
    {
        return entities.remove( id );
    }

}