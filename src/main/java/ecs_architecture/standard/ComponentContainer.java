/*
 * Copyright (c) 2022. The Ceragine project files, related assets and related source code
 * were created by, Ogesoka (Oge) Tasie and should not be copied or distributed without
 * consent of the creator (Ogesoka Tasie). All Rights Reserved.
 */

package ecs_architecture.standard;

import game_debugger.Alert;
import game_debugger.GameDebugger;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * An abstract class to be extended by a container of components
 */
abstract class ComponentContainer extends EntityContainer
{

    /**
     * Map of game component types and entities that use them
     */
    private final Map< Class< ? extends GameComponent >, Map< String, GameComponent > > components = new ConcurrentHashMap<>( );

    /**
     * Default constructor for creating a new {@code ComponentContainer}
     */
    protected ComponentContainer( )
    {
    }

    /**
     * Validates the specified ID against some conditions
     *
     * @param id the specified ID of the entity to validate
     *
     * @throws NullPointerException     throws an exception if the provided ID is null or blank
     * @throws IllegalArgumentException throws an exception if the provided ID does not exist in the entity container
     */
    private void validateID( String id )
    {
        if ( id == null || id.isBlank( ) )
        {
            GameDebugger.log( Alert.WARN , "the id cannot be null or blank" );
            throw new NullPointerException( "the id cannot be null or blank" );
        }
        if ( ! contains( id ) )
        {
            GameDebugger.log( Alert.WARN , "the entity id does not exist" );
            throw new IllegalArgumentException( "the entity id does not exist" );
        }
    }

    /**
     * Adds components to an {@code existing} entity with the specified ID
     *
     * @param id             the specified ID of the entity to add components to
     * @param gameComponents the specified components to add to the entity
     */
    protected final void internalAdd( String id , GameComponent... gameComponents )
    {
        validateID( id );
        Arrays.stream( gameComponents ).filter( Objects::nonNull ).forEach( gameComponent -> components.computeIfAbsent( gameComponent.getClass( ) , type -> new ConcurrentHashMap<>( ) ).put( id , gameComponent ) );
    }

    /**
     * Adds components to a {@code new} entity. A randomly generated ID is assigned to the entity and mapped to its components
     * <p>
     * This ID can be retrieved by using {@code get(Class)} {@link ComponentContainer#get(Class) method} to acquire a group of components of the same type mapped to entity IDs, and calling {@code Map.keySet()}.
     * The ID can also be acquired by using methods like {@code Map.forEach} which have a consumer parameter that accepts a {@code String} id and {@code GameComponent}
     * </p>
     * <p>
     * To protect the IDs from being tampered with and to enforce an Entity-Component-System design, component IDs should only ever be exposed in similar ways to the ones described above
     * </p>
     *
     * @param gameComponents the specified components to add to the entity
     */
    protected final void internalAdd( GameComponent... gameComponents )
    {
        String id = super.internalAdd( );
        internalAdd( id , gameComponents );
    }

    /**
     * Removes the specified component from an {@code existing} entity
     *
     * @param id   the specified ID of the entity to remove a component from
     * @param type the specified {@link GameComponent component} type to remove
     *
     * @return {@code True} if the map contained the element and was successfully removed the component. {@code False} otherwise
     */
    protected final boolean internalRemove( String id , Class< ? extends GameComponent > type )
    {
        GameComponent component = components.getOrDefault( type , Collections.emptyMap( ) ).remove( id );
        return component != null;
    }

    /**
     * {@inheritDoc}
     *
     * @throws NoSuchElementException throws an exception if the specified entity ID can be found mapped to a component type but, the mapped value is illegal. This is perceived as the entity not having that component type
     */
    @Override
    protected final boolean internalRemove( String id )
    {
        try
        {
            validateID( id );
        }
        catch ( Exception e )
        {
            return false;
        }
        GameDebugger.log( Alert.INFO , "removing components for entity=" + id );
        StringBuilder stringBuilder = new StringBuilder( );
        components.forEach( ( type , group ) -> {
            if ( group.containsKey( id ) )
            {
                boolean removed = internalRemove( id , type );
                stringBuilder.append( "componentType=" ).append( type ).append( "::removed=" ).append( removed ).append( "<br>" );
            }
        } );
        GameDebugger.log( Alert.INFO , stringBuilder.toString( ) );
        return super.internalRemove( id );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final Map< String, GameComponent > get( Class< ? extends GameComponent > type )
    {
        return components.computeIfAbsent( type , componentType -> new ConcurrentHashMap<>( ) );
    }

}