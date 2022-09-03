/*
 * Copyright (c) 2022. The Ceragine project files, related assets and related source code
 * were created by, Ogesoka (Oge) Tasie and should not be copied or distributed without
 * consent of the creator (Ogesoka Tasie). All Rights Reserved.
 */

package resource_manager.writable.core;

import junit.framework.TestCase;

import java.awt.image.BufferedImage;

/**
 * A class for testing the spritesheet data object
 */
public class SpritesheetTest extends TestCase
{

    /**
     * Test spritesheet
     */
    private static final Spritesheet SPRITESHEET = Spritesheet.from( "test" , new BufferedImage( 100 , 120 , BufferedImage.TYPE_INT_ARGB ) );

    /**
     * Tests whether the key name of the spritesheet is correctly returned
     */
    public void testGetKey( )
    {
        String key = SPRITESHEET.getKey( );
        assertEquals( "|Values did not match|" , "test" , key );
    }

    /**
     * Tests whether the width of the image is correctly returned
     */
    public void testGetWidth( )
    {
        int width = SPRITESHEET.getWidth( );
        assertEquals( "|Values did not match|" , 100 , width );
    }

    /**
     * Tests whether the height of the image is correctly returned
     */
    public void testGetHeight( )
    {
        int height = SPRITESHEET.getHeight( );
        assertEquals( "|Values did not match|" , 120 , height );
    }

    /**
     * Tests whether the pixel array is correctly returned
     */
    public void testGetPixels( )
    {
        int[] pixels = SPRITESHEET.getPixels( );
        assertEquals( "|Values did not match|" , 100 * 120 , pixels.length );
    }

}