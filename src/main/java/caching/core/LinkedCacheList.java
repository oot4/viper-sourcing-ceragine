package caching.core;

import caching.api.DataNode;
import caching.api.DataNodeList;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * A class representing a linked list with LRU functionality for caching items
 *
 * @param <T>
 */
public final class LinkedCacheList< T > implements DataNodeList< T >
{

    /**
     * Size of the list
     */
    private AtomicInteger size;

    /**
     * Most used item
     */
    private DataNode< T > mostUsed;

    /**
     * Least used item
     */
    private DataNode< T > leastUsed;

    /**
     * Constructor for creating a new instance of a {@code CacheLinkedList}
     */
    private LinkedCacheList( )
    {
    }

    /**
     * Creates a new instance of a {@code CacheLinkedList}
     *
     * @param <T> the specified type of item that can be stored in the nodes of the list
     *
     * @return a new caching list with {@code Least Recently Used} functionality
     */
    public static < T > LinkedCacheList< T > newInstance( )
    {
        LinkedCacheList< T > linkedList = new LinkedCacheList<>( );
        linkedList.evictAll( );
        return linkedList;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DataNode< T > admit( T item )
    {
        DataNode< T > node = CacheNode.from( item , null , null );
        if ( leastUsed == null || mostUsed == null )
        {
            mostUsed = node;
            leastUsed = mostUsed;
        }
        prioritize( node );
        if ( mostUsed == node )
        {
            size.incrementAndGet( );
        }
        return node;
    }

    /**
     * Is the current capacity of the cache linked list limited to 1?
     *
     * @return {@code True} if the cache linked list has a current capacity of 1. {@code False} otherwise
     */
    private boolean onlyOneElement( )
    {
        return mostUsed == leastUsed;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DataNode< T > evict( )
    {
        DataNode< T > oldLeastUsed = leastUsed;
        if ( onlyOneElement( ) )
        {
            mostUsed = null;
            leastUsed = null;
        }
        else
        {
            leastUsed = leastUsed.previous( );
        }
        size.decrementAndGet( );
        return oldLeastUsed;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void evictAll( )
    {
        mostUsed = null;
        leastUsed = null;
        size = new AtomicInteger( 0 );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void prioritize( DataNode< T > node )
    {
        if ( node != mostUsed )
        {
            if ( node == leastUsed )
            {
                leastUsed = node.previous( );
            }
            node.detach( );
            mostUsed.setPrevious( node );
            node.setNext( mostUsed );
            mostUsed = node;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void update( DataNode< T > node , T item )
    {
        node.setItem( item );
        prioritize( node );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int size( )
    {
        return size.get( );
    }

}