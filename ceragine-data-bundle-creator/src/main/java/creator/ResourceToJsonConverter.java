/*
 * Copyright (c) 2022. The Ceragine project files, related assets and related source code
 * were created by, Ogesoka (Oge) Tasie and should not be copied or distributed without
 * consent of the creator (Ogesoka Tasie). All Rights Reserved.
 */

package creator;

import game_debugger.Alert;
import game_debugger.GameDebugger;
import resource_manager.loader.ResourceIO;
import resource_manager.writable.api.WritableResource;
import resource_manager.writable.core.Spritesheet;
import resource_manager.writable.standard.DataBundle;

import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Converts resources from the specified directory in to a data bundle to be used with a ceragine framework project
 */
public class ResourceToJsonConverter
{

    /**
     * Entry point for executing the data bundle creator
     *
     * @param args command line arguments
     */
    public static void main( String[] args )
    {
        GameDebugger.setDebugMode( true );
        MyDataBundle dataBundle = new MyDataBundle( );
        Map< String, BufferedImage > imageMap = ResourceIO.loadAll( System.getProperty( "user.dir" ) + "/resources" );
        List< Spritesheet > spritesheets = new CopyOnWriteArrayList<>( );
        imageMap.forEach( ( key , image ) -> spritesheets.add( Spritesheet.from( key , image ) ) );
        dataBundle.initialize( spritesheets );
    }

    /**
     * A class representing a template data bundle
     */
    @WritableResource( filename = "mydatabundle" )
    private static class MyDataBundle extends DataBundle
    {

        private void initialize( List< Spritesheet > spritesheets )
        {
            if ( spritesheets.isEmpty( ) )
            {
                GameDebugger.log( Alert.DIRE , "There are no files in the resources folder to read. Please copy your image files to the resources folder" );
                return;
            }
            setSpritesheets( spritesheets );
            writeToFile( );
        }

    }

}