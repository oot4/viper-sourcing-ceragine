package ecs_architecture.api;

import ecs_architecture.standard.GameComponent;
import ecs_architecture.standard.GameSystem;

import java.util.Map;

/**
 * An interface to be implemented by a provider of gameplay functionality
 */
public interface GameplayAPI extends KeyboardAPI, DrawAPI
{

    /**
     * Gets the map of entities with the specified component type
     *
     * @param type The specified {@link GameComponent component} type the entities must have
     *
     * @return A map of entities to the matching type of game component
     */
    Map< String, GameComponent > get( Class< ? extends GameComponent > type );

    /**
     * Adds a new entity with the specified component(s)
     *
     * @param gameComponents the specified {@link GameComponent component(s)} to give the entity
     */
    void add( GameComponent... gameComponents );

    /**
     * Adds to an existing entity, the specified component(s)
     *
     * @param id             the specified entity to add components to
     * @param gameComponents the specified {@link GameComponent component(s)} to give the entity
     */
    void add( String id , GameComponent... gameComponents );

    /**
     * Adds the specified game system(s)
     *
     * @param gameSystems the specified game {@link GameSystem system(s)} to add
     */
    void add( GameSystem... gameSystems );

    /**
     * Removes an entity and all its components
     *
     * @param id the specified entity to remove
     */
    void remove( String id );

    /**
     * Removes a component from the specified entity
     *
     * @param id   the specified entity to remove a component from
     * @param type the specified {@link GameComponent component} type to remove
     */
    void remove( String id , Class< ? extends GameComponent > type );

    /**
     * Activates the specified game system
     *
     * @param type the specified class type of the game system
     */
    void activate( Class< ? extends GameSystem > type );

    /**
     * Deactivates the specified game system
     *
     * @param type the specified class type of the game system
     */
    void deactivate( Class< ? extends GameSystem > type );

}