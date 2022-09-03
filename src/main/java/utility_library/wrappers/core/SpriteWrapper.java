/*
 * Copyright (c) 2022. The Ceragine project files, related assets and related source code
 * were created by, Ogesoka (Oge) Tasie and should not be copied or distributed without
 * consent of the creator (Ogesoka Tasie). All Rights Reserved.
 */

package utility_library.wrappers.core;

import game_debugger.Alert;
import game_debugger.GameDebugger;
import resource_manager.writable.core.GameDataBundle;
import resource_manager.writable.core.Spritesheet;
import utility_library.wrappers.standard.StandardGameWrapper;

import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * A class representing a resource wrapper for sprites
 */
public final class SpriteWrapper extends StandardGameWrapper< Spritesheet >
{

    /**
     * Spritesheet wrapper instance
     */
    private static SpriteWrapper spriteWrapper;

    /**
     * Constructor for creating a new instance of a {@code SpriteWrapper}
     *
     * @param elements the specified map of elements to contain
     */
    private SpriteWrapper( Map< String, Spritesheet > elements )
    {
        super( elements );
        if ( spriteWrapper != null )
        {
            throw new UnsupportedOperationException( "|instance already exists, use any of the static methods instead|" );
        }
    }

    /**
     * The instance of a {@code SpriteWrapper}
     *
     * @return the static instance of {@link SpriteWrapper sprite wrapper}
     *
     * @throws NullPointerException throws an exception if the static instance of the sprite wrapper class could not be initialized
     */
    public static SpriteWrapper getInstance( GameDataBundle gameDataBundle )
    {
        if ( spriteWrapper == null )
        {
            synchronized ( SpriteWrapper.class )
            {
                boolean unsuccessful = ! initialize( gameDataBundle );
                if ( spriteWrapper == null || unsuccessful )
                {
                    throw new NullPointerException( "|sprite wrapper could not be initialized|" );
                }
            }
        }
        return spriteWrapper;
    }

    /**
     * Is the key a valid key?
     *
     * @param key the specified key to validate
     *
     * @return {@code True} if the key is a valid one. {@code False} otherwise
     */
    private boolean valid( String key )
    {
        if ( ! spriteWrapper.contains( key ) )
        {
            GameDebugger.log( Alert.WARN , "Key cannot be found" );
            return false;
        }
        return true;
    }

    /**
     * Initializes the static instance of {@code SpriteWrapper}
     *
     * @return {@code True} if the static instance of {@code SpriteWrapper} was initialized. {@code False} otherwise
     *
     * @throws IllegalArgumentException throws an exception if the game data bundle provided has not been initialized
     */
    private static boolean initialize( GameDataBundle gameDataBundle )
    {
        if ( ! gameDataBundle.isInitialized( ) )
        {
            throw new IllegalArgumentException( "The data bundle has not been initialized" );
        }
        List< Spritesheet > spritesheetList = gameDataBundle.getSpritesheets( );
        if ( spritesheetList == null )
        {
            return false;
        }
        Map< String, Spritesheet > spritesheetMap = new ConcurrentHashMap<>( );
        spritesheetList.forEach( spritesheet -> spritesheetMap.putIfAbsent( spritesheet.getKey( ) , spritesheet ) );
        spriteWrapper = new SpriteWrapper( spritesheetMap );
        return ! spriteWrapper.isEmpty( );
    }

    /**
     * Gets the dimension of spritesheet in units of pixels
     *
     * @param key the specified key to the spritesheet element with the required dimensions
     *
     * @return the spritesheet dimensions as a {@code Dimension} object
     */
    public Dimension dimension( String key )
    {
        if ( valid( key ) )
        {
            int width = spriteWrapper.get( key ).getWidth( );
            int height = spriteWrapper.get( key ).getHeight( );
            return new Dimension( width , height );
        }
        return new Dimension( );
    }

    /**
     * Gets the pixel data of the specified spritesheet resource
     *
     * @param key the specified key to the spritesheet element with the pixel data
     *
     * @return an array of pixel values
     */
    public int[] pixels( String key )
    {
        if ( valid( key ) )
        {
            return spriteWrapper.get( key ).getPixels( );
        }
        return new int[ 0 ];
    }

}