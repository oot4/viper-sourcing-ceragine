/*
 * Copyright (c) 2022. The Ceragine project files, related assets and related source code
 * were created by, Ogesoka (Oge) Tasie and should not be copied or distributed without
 * consent of the creator (Ogesoka Tasie). All Rights Reserved.
 */

package utility_library.wrappers.api;

/**
 * An interface to be implemented by a wrapper of game resources
 *
 * @param <T> the type of game resources that can be stored and removed from this wrapper
 */
public interface GameWrapper< T extends GameResource >
{

    /**
     * Gets the size of the wrapper
     *
     * @return the number of elements in the wrapper
     */
    int size( );

    /**
     * Is this wrapper empty?
     *
     * @return {@code True} if the specified wrapper has no elements. {@code False} otherwise
     */
    boolean isEmpty( );

    /**
     * Is the specified element in this wrapper?
     *
     * @param key the specified key to the element to test the presence of
     *
     * @return {@code True} if the wrapper has the specified element. {@code False} otherwise
     */
    boolean contains( String key );

    /**
     * Get the specified element with the matching key
     *
     * @param key the specified key to the element
     *
     * @return the element whose key matches that specified as a game resource {@link T  type}
     */
    T get( String key );

}