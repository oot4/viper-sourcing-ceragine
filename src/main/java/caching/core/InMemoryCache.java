package caching.core;

import caching.api.CacheMap;
import caching.api.Cacheable;
import caching.api.DataNode;
import caching.api.DataNodeList;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * A class representing an in memory cache with LRU operations
 *
 * @param <K> the type of key used to identify nodes and cacheable data
 * @param <V> the type of value that can be cached
 */
public final class InMemoryCache< K, V > implements CacheMap< K, V >
{

    /**
     * Maximum Capacity
     */
    private final int capacity;

    /**
     * DataNode reference map
     */
    private final Map< K, DataNode< Cacheable< K, V > > > references;

    /**
     * Data node list
     */
    private final DataNodeList< Cacheable< K, V > > nodes;

    /**
     * Constructor for creating a new instance of an {@code InMemoryCache}
     *
     * @param capacity   the specified maximum capacity to give the cache
     * @param references the specified reference map used to query nodes
     * @param nodes      the specified data node list used to store cacheable items
     */
    private InMemoryCache( int capacity , Map< K, DataNode< Cacheable< K, V > > > references , DataNodeList< Cacheable< K, V > > nodes )
    {
        this.capacity = capacity;
        this.references = references;
        this.nodes = nodes;
    }

    /**
     * Creates a new instance of an {@code InMemoryCache} with a default size
     *
     * @param <K> the type of key used to identify nodes and cacheable data
     * @param <V> the type of value that can be cached
     *
     * @return a newly created instance of an {@code InMemoryCache} with a capacity of {@code 1024}
     */
    public static < K, V > InMemoryCache< K, V > standard( )
    {
        return of( 1024 );
    }

    /**
     * Creates a new instance of an {@code InMemoryCache}
     *
     * @param capacity the specified maximum capacity to give the cache
     * @param <K>      the type of key used to identify nodes and cacheable data
     * @param <V>      the type of value that can be cached
     *
     * @return a newly created instance of an {@code InMemoryCache} with the specified capacity
     *
     * @throws IllegalArgumentException throws an exception if the capacity is less than one
     */
    public static < K, V > InMemoryCache< K, V > of( int capacity )
    {
        if ( capacity < 0 )
        {
            throw new IllegalArgumentException( "Capacity cannot be less than 1" );
        }
        Map< K, DataNode< Cacheable< K, V > > > references = new ConcurrentHashMap<>( capacity );
        DataNodeList< Cacheable< K, V > > nodes = LinkedCacheList.newInstance( );
        return new InMemoryCache<>( capacity , references , nodes );
    }

    /**
     * Does the cache contain the specified key?
     *
     * @param key the specified key whose presence is to be tested
     *
     * @return {@code True} if the specified key is present in the cache. {@code False} otherwise
     */
    public boolean contains( K key )
    {
        return references.containsKey( key );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int size( )
    {
        return nodes.size( );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean put( K key , V value )
    {
        if ( key == null || value == null )
        {
            //Do not permit null values, storing nothing is a waste
            return false;
        }
        Cacheable< K, V > cacheData = CacheData.from( key , value );
        DataNode< Cacheable< K, V > > pointer;
        if ( references.containsKey( key ) )
        {
            pointer = references.get( key );
            nodes.update( pointer , cacheData );
            return true;
        }
        if ( nodes.size( ) == capacity )
        {
            pointer = nodes.evict( );
            references.remove( pointer.item( ).key( ) );
        }
        pointer = nodes.admit( cacheData );
        references.put( key , pointer );
        return references.containsKey( key ) && references.containsValue( pointer );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional< V > get( K key )
    {
        if ( key != null )
        {
            DataNode< Cacheable< K, V > > node = references.get( key );
            if ( node != null && node.hasItem( ) )
            {
                nodes.prioritize( node );
                return Optional.of( node.item( ).value( ) );
            }
        }
        return Optional.empty( );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void clear( )
    {
        references.clear( );
        nodes.evictAll( );
    }

}