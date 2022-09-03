package caching.core;

import caching.api.DataNode;

/**
 * A class representing a node in a caching environment
 *
 * @param <T> the specified type of the item the node can contain
 */
public final class CacheNode< T > implements DataNode< T >
{

    /**
     * The item stored in the node
     */
    private T item;

    /**
     * Pointer to the next linked node
     */
    private DataNode< T > next;

    /**
     * Pointer to the previous linked node
     */
    private DataNode< T > previous;

    /**
     * Constructor for creating a new instance of a {@code CacheDataNode}
     *
     * @param item     the specified item to contain in the node
     * @param next     the specified 'next' linked node
     * @param previous the specified 'previous' linked node
     */
    private CacheNode( T item , CacheNode< T > next , CacheNode< T > previous )
    {
        this.item = item;
        this.next = next;
        this.previous = previous;
    }

    /**
     * Creates an empty instance of a {@code CacheDataNode}
     *
     * @param <T> the type of item that can be stored in the node
     *
     * @return a new empty {@code CacheDataNode} instance
     */
    public static < T > CacheNode< T > empty( )
    {
        return from( null , null , null );
    }

    /**
     * Creates a new instance of a {@code CacheDataNode}
     *
     * @param item     the specified item to store in the node
     * @param next     the specified 'next' linked node
     * @param previous the specified 'previous' linked node
     * @param <T>      the type of item that can be stored in the node
     *
     * @return a new {@code NodeLRU} instance from the specified arguments
     */
    public static < T > CacheNode< T > from( T item , CacheNode< T > next , CacheNode< T > previous )
    {
        return new CacheNode<>( item , next , previous );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public T item( )
    {
        return item;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setItem( T item )
    {
        this.item = item;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DataNode< T > next( )
    {
        return next;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setNext( DataNode< T > node )
    {
        if ( this.getClass( ).isInstance( node ) )
        {
            next = node;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DataNode< T > previous( )
    {
        return previous;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setPrevious( DataNode< T > node )
    {
        if ( this.getClass( ).isInstance( node ) )
        {
            previous = node;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void detach( )
    {
        if ( previous != null )
        {
            previous.setNext( next );
        }
        if ( next != null )
        {
            next.setPrevious( previous );
        }
        next = null;
        previous = null;
    }

}