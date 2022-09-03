package caching.api;

/**
 * An interface to be implemented by a cacheable item
 *
 * @param <K> the type of the key used to identify the cacheable item
 * @param <V> the type of the data stored in the cacheable item
 */
public interface Cacheable< K, V >
{

    /**
     * Gets the key used to identify the cacheable item
     *
     * @return the key part of the key-value pair
     */
    K key( );

    /**
     * Sets the key used to identify the cacheable item
     *
     * @param key the specified key to set
     */
    void setKey( K key );

    /**
     * Gets the value mapped to the key in the cacheable item
     *
     * @return the value part of the key-value pair
     */
    V value( );

    /**
     * Sets the value mapped to the key of this cacheable item
     *
     * @param value the specified value to map
     */
    void setValue( V value );

}