package caching.core;

import caching.api.Cacheable;

/**
 * A class representing a cache data container that stores a Key-Value pair
 *
 * @param <K> the type of key used to identify the cache data
 * @param <V> the type of value the cache data object can contain
 */
public final class CacheData< K, V > implements Cacheable< K, V >
{

    /**
     * Cache data key
     */
    private K key;

    /**
     * Cache data value
     */
    private V value;

    /**
     * Constructor for creating a new instance of a {@code CacheData} object
     *
     * @param key   the specified key used to identify the cache data object
     * @param value the specified value contained in the cache data object
     */
    private CacheData( K key , V value )
    {
        this.key = key;
        this.value = value;
    }

    /**
     * Creates a new instance of an empty {@code CacheData} object
     *
     * @param <K> the type of key used to identify the cache data
     * @param <V> the type of value the cache data object can contain
     *
     * @return an instance of a {@code CacheData} object with empty key and value
     */
    public static < K, V > CacheData< K, V > empty( )
    {
        return from( null , null );
    }

    /**
     * Creates a new instance of a {@code CacheData} object
     *
     * @param key   the specified key used to identify the cache data object
     * @param value the specified value contained in the cache data object
     * @param <K>   the type of key used to identify the cache data
     * @param <V>   the type of value the cache data object can contain
     *
     * @return an instance of a {@code CacheData} object
     */
    public static < K, V > CacheData< K, V > from( K key , V value )
    {
        return new CacheData<>( key , value );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public K key( )
    {
        return key;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setKey( K key )
    {
        this.key = key;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public V value( )
    {
        return value;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setValue( V value )
    {
        this.value = value;
    }

}