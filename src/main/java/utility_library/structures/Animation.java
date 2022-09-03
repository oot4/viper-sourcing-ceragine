package utility_library.structures;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * A class representing a list of animation sprites
 */
public final class Animation
{

    /**
     * Sequence of sprites
     */
    private final List< Sprite > sprites = new CopyOnWriteArrayList<>( );

    /**
     * Constructor for creating a new {@code Animation}
     */
    private Animation( )
    {
        //Created through a static method
    }

    /**
     * Creates a new instance of an {@code Animation}
     *
     * @return a new {@code Animation} object
     */
    public static Animation newInstance( )
    {
        return new Animation( );
    }

    /**
     * Is the provided index an illegal or unusable value?
     *
     * @param index the specified index to validate
     *
     * @return {@code True} if the index is illegal or unusable. {@code False} otherwise
     */
    private boolean isIllegal( int index )
    {
        return sprites.isEmpty( ) || index <= - 1 || index >= sprites.size( );
    }

    /**
     * Adds sprites to the animation list
     *
     * @param sprites the specified sprite(s) to add to the animation list
     */
    public void add( Sprite... sprites )
    {
        this.sprites.addAll( Arrays.asList( sprites ) );
    }

    /**
     * <p>
     * Inserts the specified sprite at the specified index position in the list by shifting the sprite at that position and any subsequent sprites to the right
     * </p>
     *
     * @param index  the specified index position to insert the sprite at
     * @param sprite the specified sprite to insert
     */
    public void insert( int index , Sprite sprite )
    {
        if ( isIllegal( index ) )
        {
            return;
        }
        sprites.add( index , sprite );
    }

    /**
     * Replaces the specified sprite at the specified index position with the newly provided sprite
     *
     * @param index  the index of the element to replace
     * @param sprite the specified sprite to replace with
     */
    public void replaceSprite( int index , Sprite sprite )
    {
        if ( isIllegal( index ) )
        {
            return;
        }
        sprites.set( index , sprite );
    }

    /**
     * Replaces the entire list of sprites
     *
     * @param sprites the specified list of sprites to replace with
     */
    public void replaceAll( Sprite... sprites )
    {
        this.sprites.clear( );
        add( sprites );
    }

    /**
     * Removes the sprite at the specified index position
     *
     * @param index the specified index of the element to remove
     */
    private void remove( int index )
    {
        if ( isIllegal( index ) )
        {
            return;
        }
        sprites.remove( index );
    }

    /**
     * Removes the sprites at the specified indices
     *
     * @param indices the specified index positions of the sprites to remove
     */
    public void remove( int... indices )
    {
        for ( int index : indices )
        {
            remove( index );
        }
    }

    /**
     * Gets the sprite at the specified index
     *
     * @param index the specified index of the sprite to retrieve
     *
     * @return the {@link Sprite} object at the given index
     */
    public Sprite sprite( int index )
    {
        if ( isIllegal( index ) )
        {
            return null;
        }
        return sprites.get( index );
    }

    /**
     * Gets the current size of the animation list
     *
     * @return the number of elements in the animation list
     */
    public int size( )
    {
        return sprites.size( );
    }

}