package caching.api;

/**
 * An interface to be implemented by a node in a linked chain that also contains a data item
 *
 * @param <T> the specified type of item that can be stored in the node
 */
public interface DataNode< T >
{

    /**
     * Gets the item contained in the node
     *
     * @return the item contained in the node
     */
    T item( );

    /**
     * Sets the item contained in the node
     *
     * @param item the specified item to contain in the node
     */
    void setItem( T item );

    /**
     * Gets the next node in the chain link
     *
     * @return the next node from this node
     */
    DataNode< T > next( );

    /**
     * Sets the next node in the chain link
     *
     * @param node the specified node to set as the next from this node
     */
    void setNext( DataNode< T > node );

    /**
     * Gets the previous node in the chain link
     *
     * @return the previous node from this node
     */
    DataNode< T > previous( );

    /**
     * Sets the previous node in the chain link
     *
     * @param node the specified node to set as the previous from this node
     */
    void setPrevious( DataNode< T > node );

    /**
     * Detaches the node from its 'next' and 'previous' linked nodes
     */
    void detach( );

    /**
     * Does the data node hold an item?
     *
     * @return {@code True} if the data node contains an item. {@code False} otherwise
     */
    default boolean hasItem( )
    {
        return item( ) != null;
    }

    /**
     * Is the data node empty?
     *
     * @return {@code True} if the data node does not contain an item. {@code False} otherwise
     */
    default boolean isEmpty( )
    {
        return ! hasItem( );
    }

}