/*
 * Copyright (c) 2022. The Ceragine project files, related assets and related source code
 * were created by, Ogesoka (Oge) Tasie and should not be copied or distributed without
 * consent of the creator (Ogesoka Tasie). All Rights Reserved.
 */

package utility_library.wrappers.standard;

import utility_library.wrappers.api.GameResource;
import utility_library.wrappers.api.GameWrapper;

import java.util.Map;

/**
 * An abstract class to be extended by a game resource wrapper
 *
 * @param <T> the type of game resource that can be contained
 */
public abstract class StandardGameWrapper< T extends GameResource > implements GameWrapper< T >
{

    /**
     * Map of game resources
     */
    private final Map< String, T > elements;

    /**
     * Default constructor for creating a new instance of a {@code GameWrapper}
     *
     * @param elements the specified map of elements to contain
     */
    protected StandardGameWrapper( Map< String, T > elements )
    {
        this.elements = elements;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final int size( )
    {
        return elements.size( );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final boolean isEmpty( )
    {
        return elements.isEmpty( );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final boolean contains( String key )
    {
        return elements.containsKey( key );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final T get( String key )
    {
        return elements.get( key );
    }

}