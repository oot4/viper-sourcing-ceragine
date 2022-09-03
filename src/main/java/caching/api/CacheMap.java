package caching.api;

import java.util.Map;
import java.util.Optional;

/**
 * An interface to be implemented by a caching map with {@code Least Recently Used} (LRU) eviction algorithmic approach
 *
 * @param <K> the specified type of the key
 * @param <V> the specified type of the value
 */
public interface CacheMap< K, V >
{

    /**
     * Gets the current size of the map
     *
     * @return the specified number of elements contained in the map
     */
    int size( );

    /**
     * Is the map empty and without elements?
     *
     * @return {@code True} if there are no elements present in the map.{@code False otherwise}
     */
    default boolean isEmpty( )
    {
        return size( ) == 0;
    }

    /**
     * Puts the specified key-value pair in to the map
     *
     * @param key   the specified key to the value
     * @param value the specified value to contain
     *
     * @return {@code True} if the key and value were successfully put in the map. {@code False} otherwise
     */
    boolean put( K key , V value );

    /**
     * Puts all the specified key-value pairs in to the map
     *
     * @param map the specified entry set to put in to the map
     *
     * @return {@code True} if the keys and values were successfully put in the map. {@code False} otherwise
     */
    default boolean putAll( Map< ? extends K, ? extends V > map )
    {
        for ( Map.Entry< ? extends K, ? extends V > entry : map.entrySet( ) )
        {
            K key = entry.getKey( );
            V value = entry.getValue( );
            if ( ! put( key , value ) )
            {
                return false;
            }
        }
        return true;
    }

    /**
     * Gets the value whose key matches the one specified, from the map
     *
     * @param key the specified key to the element that must be retrieved
     *
     * @return an {@code Optional} with the value contained within it
     */
    Optional< V > get( K key );

    /**
     * Clears the cache map of all its elements
     */
    void clear( );

}