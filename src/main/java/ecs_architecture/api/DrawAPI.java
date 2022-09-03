package ecs_architecture.api;

/**
 * A functional interface to be implemented when using a render layer to draw graphics
 */
@FunctionalInterface
public interface DrawAPI
{

    /**
     * Draws a sprite which is an {@code 8x8} pixel image
     *
     * @param spritesheet the specified spritesheet to get the image from
     * @param x           the specified {@code x} position to render the image
     * @param y           the specified {@code y} position to render the image
     * @param tile        the specified tile image on the spritesheet to render
     * @param mirror      the specified direction to mirror the image
     * @param scale       the specified scale to resize the image to
     * @param alpha       the specified alpha value to apply to the image
     */
    void draw( String spritesheet , double x , double y , int tile , int mirror , int scale , int alpha );

    /**
     * Draws a sprite which is an {@code 8x8} pixel image
     *
     * @param spritesheet the specified spritesheet to get the image from
     * @param x           the specified {@code x} position to render the image
     * @param y           the specified {@code y} position to render the image
     * @param tile        the specified tile image on the spritesheet to render
     * @param mirror      the specified direction to mirror the image
     * @param scale       the specified scale to resize the image to
     */
    default void draw( String spritesheet , double x , double y , int tile , int mirror , int scale )
    {
        draw( spritesheet , x , y , tile , mirror , scale , 255 );
    }

    /**
     * Draws a sprite which is an {@code 8x8} pixel image
     *
     * @param spritesheet the specified spritesheet to get the image from
     * @param x           the specified {@code x} position to render the image
     * @param y           the specified {@code y} position to render the image
     * @param tile        the specified tile image on the spritesheet to render
     * @param mirror      the specified direction to mirror the image
     */
    default void draw( String spritesheet , double x , double y , int tile , int mirror )
    {
        draw( spritesheet , x , y , tile , mirror , 1 );
    }

    /**
     * Draws a sprite which is an {@code 8x8} pixel image
     *
     * @param spritesheet the specified spritesheet to get the image from
     * @param x           the specified {@code x} position to render the image
     * @param y           the specified {@code y} position to render the image
     * @param tile        the specified tile image on the spritesheet to render
     */
    default void draw( String spritesheet , double x , double y , int tile )
    {
        draw( spritesheet , x , y , tile , 0 );
    }

}