package utility_library.gameplay.rendering.api;

import utility_library.structures.Sprite;


/**
 * A functional interface to extended by a container of a sprite grid
 */
@FunctionalInterface
public interface SpriteContainer
{

    /**
     * Gets the sprite grid from the container
     *
     * @return a sprite grid object
     */
    Sprite sprite( );

}