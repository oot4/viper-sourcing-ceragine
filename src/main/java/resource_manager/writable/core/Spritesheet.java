/*
 * Copyright (c) 2022. The Ceragine project files, related assets and related source code
 * were created by, Ogesoka (Oge) Tasie and should not be copied or distributed without
 * consent of the creator (Ogesoka Tasie). All Rights Reserved.
 */

package resource_manager.writable.core;

import utility_library.wrappers.api.GameResource;

import java.awt.image.BufferedImage;

/**
 * A class representing a sheet of sprite images
 */
public final class Spritesheet implements GameResource
{

    /**
     * Key identifying the sprite image
     */
    private String key;

    /**
     * Width of the spritesheet
     */
    private int width;

    /**
     * Height of the spritesheet
     */
    private int height;

    /**
     * RGB pixel data
     */
    private int[] pixels;

    /**
     * Creates a new instance of a {@code Spritesheet}
     */
    private Spritesheet( ) { }

    /**
     * Creates a new instance of a {@code Spritesheet}
     *
     * @param key    the specified key used to identify the spritesheet
     * @param width  the specified width of the spritesheet
     * @param height the specified height of the spritesheet
     * @param pixels the array of RGB pixel data contained in the spritesheet
     */
    private Spritesheet( String key , int width , int height , int[] pixels )
    {
        this.key = key;
        this.width = width;
        this.height = height;
        this.pixels = pixels;
    }

    /**
     * Creates a new instance of a {@code Spritesheet}
     *
     * @param key   the specified key used to identify the spritesheet
     * @param image the specified image containing the pixel data of the spritesheet
     *
     * @return a new object instance as a {@link Spritesheet}
     *
     * @throws IllegalArgumentException throws an exception if the version string is not of the correct format
     */
    public static Spritesheet from( String key , BufferedImage image )
    {
        int width = image.getWidth( );
        int height = image.getHeight( );
        int[] pixels = image.getRGB( 0 , 0 , width , height , null , 0 , width );
        return new Spritesheet( key , width , height , pixels );
    }

    /**
     * Gets the key name of the spritesheet
     *
     * @return the string value to identify the spritesheet
     */
    public String getKey( )
    {
        return key;
    }

    /**
     * Gets the width of the spritesheet
     *
     * @return the number of pixels horizontally on the spritesheet
     */
    public int getWidth( )
    {
        return width;
    }

    /**
     * Gets the height of the spritesheet
     *
     * @return the number of pixels vertically on the spritesheet
     */
    public int getHeight( )
    {
        return height;
    }

    /**
     * Gets the array of pixel data for the spritesheet
     *
     * @return an array of RGB values representing the image on the spritesheet
     */
    public int[] getPixels( )
    {
        return pixels;
    }

}