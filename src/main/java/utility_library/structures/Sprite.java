package utility_library.structures;

import utility_library.algorithms.core.Clamp;

import java.awt.*;
import java.util.Arrays;

/**
 * A class representing a grid of sprites
 */
public final class Sprite
{

    /**
     * Grid scale
     */
    private int scale;

    /**
     * Grid alpha
     */
    private int alpha;

    /**
     * Grid width
     */
    private final int width;

    /**
     * Grid height
     */
    private final int height;

    /**
     * Sprite tiles
     */
    private int[] tiles;

    /**
     * Mirrored values
     */
    private int[] mirrored;

    /**
     * Offset values
     */
    private final Point offset;

    /**
     * Spritesheet name
     */
    private final String spritesheet;

    /**
     * Constructor for creating a new {@code Sprite}
     *
     * @param spritesheet the specified filename of the spritesheet this grid will use
     * @param width       the specified width of the sprite grid
     * @param height      the specified height of the sprite grid
     */
    private Sprite( String spritesheet , int width , int height )
    {
        this.width = width;
        this.height = height;
        this.spritesheet = spritesheet;
        offset = new Point( );
    }

    /**
     * Creates a new instance of a {@code Sprite}
     *
     * @param spritesheet the specified filename of the spritesheet this grid will use
     * @param width       the specified width of the sprite grid
     * @param height      the specified height of the sprite grid
     *
     * @return a new {@code SpriteGrid} instance of the specified dimensions and spritesheet tile set
     */
    public static Sprite from( String spritesheet , int width , int height )
    {
        int size = width * height;
        Sprite grid = new Sprite( spritesheet , width , height );
        grid.setScale( 1 );
        grid.setAlpha( 255 );
        grid.tiles = new int[ size ];
        grid.mirrored = new int[ size ];
        return grid;
    }

    /**
     * Validates the position index is within bounds
     *
     * @param position the specified position to validate
     * @param target   the target array to test the provided index of
     */
    private static void validatePosition( int position , int[] target )
    {
        if ( position < 0 || position >= target.length )
        {
            throw new IllegalArgumentException( "illegal argument, position is not a valid index" );
        }
    }

    /**
     * Copies the value array to the target array if they match length
     *
     * @param target the specified target array to copy to
     * @param values the specified values to copy
     */
    private static void copy( int[] target , int[] values )
    {
        if ( values.length != target.length )
        {
            throw new IllegalArgumentException( "illegal argument, value array is not the same length as target" );
        }
        System.arraycopy( values , 0 , target , 0 , values.length );
    }

    /**
     * Gets the scaling value applied to the sprite grid
     *
     * @return the value to scale the sprite grid by
     */
    public int scale( )
    {
        return scale;
    }

    /**
     * sets the scaling of the sprite grid
     *
     * @param scale the specified scaling value to apply to the sprite grid
     */
    public void setScale( int scale )
    {
        this.scale = Clamp.toInt( scale , 0 , 4 );
    }

    /**
     * Gets the alpha value applied to the sprite grid
     *
     * @return the value to adjust the opacity of the sprite grid
     */
    public int alpha( )
    {
        return alpha;
    }

    /**
     * Sets the opacity of the sprite grid by applying an alpha value
     *
     * @param alpha the specified alpha value to apply to the sprite grid
     */
    public void setAlpha( int alpha )
    {
        this.alpha = Clamp.toInt( alpha , 0 , 255 );
    }

    /**
     * Gets the width of the sprite grid
     *
     * @return the number of tiles horizontally from left to right
     */
    public int width( )
    {
        return width;
    }

    /**
     * Gets the height of the sprite grid
     *
     * @return the number of tiles vertically from top to bottom
     */
    public int height( )
    {
        return height;
    }

    /**
     * Gets the single dimensional array of tile values indexed :
     * <p>{@code |0 -> ( (width*height) - 1 )|}</p>
     *
     * @return the array of tile values representing the sprite grid
     */
    public int[] tiles( )
    {
        return Arrays.copyOf( tiles , tiles.length );
    }

    /**
     * Sets the tile value at the given array index to the one specified
     *
     * @param position the specified position of the tile value to replace
     * @param tile     the specified tile value to assign
     */
    public void replaceTile( int position , int tile )
    {
        validatePosition( position , tiles );
        tiles[ position ] = tile;
    }

    /**
     * Replaces all the current tile values with the values specified
     *
     * @param tiles the specified tile value array to replace with
     */
    public void replaceTiles( int[] tiles )
    {
        copy( this.tiles , tiles );
    }

    /**
     * Gets the single dimensional array of mirror values indexed :
     * <p>{@code |0 -> ( (width*height) - 1 )|}</p>
     *
     * @return the array of mirror values representing mirrored tiles in the sprite grid
     */
    public int[] mirrored( )
    {
        return mirrored;
    }

    /**
     * Sets the mirror value at the given array index to the one specified
     *
     * @param mirror   the specified value to assign
     * @param position the specified position of the mirror value to replace
     */
    public void replaceMirror( int mirror , int position )
    {
        validatePosition( position , mirrored );
        mirrored[ position ] = mirror;
    }

    /**
     * Replaces all the current mirror values with the values specified
     *
     * @param mirrored the specified mirror value array to replace with
     */
    public void replaceMirrored( int[] mirrored )
    {
        copy( this.mirrored , mirrored );
    }

    /**
     * Gets the offset value to apply to the position of tiles when rendering
     *
     * @return the offset amount along {@code x-axis} and {@code y-axis}
     */
    public Point offset( )
    {
        return offset;
    }

    /**
     * The name of the spritesheet containing the image data for the tiles in the sprite grid
     *
     * @return the string name of the spritesheet
     */
    public String spritesheet( )
    {
        return spritesheet;
    }

}