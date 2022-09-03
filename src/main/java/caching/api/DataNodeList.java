package caching.api;

import java.util.Collection;

/**
 * An interface to be implemented by a list containing a chain link of data nodes
 *
 * @param <T> the specified type of item that can be stored in the list
 */
public interface DataNodeList< T >
{

    /**
     * Admits a new item to the list
     *
     * @param item the specified item to admit
     *
     * @return the node containing the newly admitted item
     */
    DataNode< T > admit( T item );

    /**
     * Admits a collection of new items to the list
     *
     * @param items the specified collection of items to admit
     *
     * @return {@code True} if all items were successfully admitted. {@code False} otherwise
     */
    default boolean admitAll( Collection< ? extends T > items )
    {
        for ( T item : items )
        {
            if ( admit( item ).isEmpty( ) )
            {
                return false;
            }
        }
        return true;
    }

    /**
     * Evicts an item from the list
     *
     * @return the node containing the least used item
     */
    DataNode< T > evict( );

    /**
     * Evicts all items from the list
     */
    void evictAll( );

    /**
     * Ranks the specified node as containing the item with the most recent uses
     *
     * @param node the specified node containing the most used item
     */
    void prioritize( DataNode< T > node );

    /**
     * Updates the item of the specified node
     *
     * @param node the specified node whose item should be updated
     * @param item the specified item to update the node with
     */
    void update( DataNode< T > node , T item );

    /**
     * Gets the size of the list
     *
     * @return the number of elements currently available in the list
     */
    int size( );

    /**
     * Is the list empty?
     *
     * @return {@code True} if the data node list is empty. {@code False} otherwise
     */
    default boolean isEmpty( )
    {
        return size( ) == 0;
    }

}