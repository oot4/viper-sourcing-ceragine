/*
 * Copyright (c) 2022. The Ceragine project files, related assets and related source code
 * were created by, Ogesoka (Oge) Tasie and should not be copied or distributed without
 * consent of the creator (Ogesoka Tasie). All Rights Reserved.
 */

package resource_manager.loader;

import game_debugger.Alert;
import game_debugger.GameDebugger;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import static java.util.stream.Collectors.toList;

/**
 * A class representing a resource loader.
 * <p>
 * Resources are expected to be placed directly into the dedicated resource folder at which point the system can detect files successfully.
 * Files can be placed in a custom folder with custom sub-folders filled with resources however, the top level custom folder must be a direct sub-folder to the project module or folder.
 * </P>
 */
public final class ResourceIO
{

    /**
     * File separator on Windows systems
     */
    private static final String WINDOWS_FILE_SEPARATOR = "\\";

    /**
     * File separator on Unix systems
     */
    private static final String UNIX_FILE_SEPARATOR = "/";

    /**
     * Character indicating that the next expected substring is a file extension
     */
    private static final String FILE_EXTENSION_SEPARATOR = ".";

    /**
     * Handler for generating URLs
     */
    private static final URLHandler URL_HANDLER = new URLHandler( );

    /**
     * The configuration of the graphics environment
     */
    private static GraphicsConfiguration graphicsConfiguration;

    /**
     * Creates a new instance of a {@code ResourceLoader}
     *
     * @throws UnsupportedOperationException directly initializing this class throws an exception
     */
    private ResourceIO( )
    {
        throw new UnsupportedOperationException( "This class cannot be instantiated" );
    }

    /**
     * Returns a list of all files with the specified extension from a resource folder or file system
     *
     * @param folder    the specified folder to search for files that have a matching extension
     * @param extension the specified file extension to match for
     *
     * @return a list of the available files with extensions that match
     */
    private static List< String > getUnknownFilenames( String folder , String extension )
    {
        List< String > unknownFileNames = new CopyOnWriteArrayList<>( );
        try ( BufferedReader bufferedReader = new BufferedReader( new InputStreamReader( read( folder ) , StandardCharsets.UTF_8 ) ) )
        {
            List< String > filenames = bufferedReader.lines( ).collect( toList( ) );
            filenames.forEach( filename -> {
                //Retrieve and validate the filename extension if one is present
                String actualExtension = "";
                int indexOfLastFES = filename.lastIndexOf( FILE_EXTENSION_SEPARATOR );
                int indexOfLastWFS = filename.lastIndexOf( WINDOWS_FILE_SEPARATOR );
                int indexOfLastUFS = filename.lastIndexOf( UNIX_FILE_SEPARATOR );
                int indexOfLastSeparator = Math.max( indexOfLastWFS , indexOfLastUFS );
                if ( indexOfLastFES > indexOfLastSeparator )
                {
                    actualExtension = filename.substring( indexOfLastFES + 1 );
                }

                //Add filename to list if it matches the extension
                if ( actualExtension.equalsIgnoreCase( extension ) )
                {
                    unknownFileNames.add( folder + "/" + filename );
                    return;
                }
                //If extension is blank then filename is a folder, therefore search the folder
                if ( actualExtension.isBlank( ) )
                {
                    unknownFileNames.addAll( getUnknownFilenames( folder + "/" + filename , extension ) );
                }
            } );
            return unknownFileNames;
        }
        catch ( Exception e )
        {
            GameDebugger.log( Alert.WARN , "No files were found at the specified path.." + folder , e );
            return Collections.emptyList( );
        }
    }

    /**
     * Gets the specified file content as a string, either from a resource folder or file system
     *
     * @param url     the URL path to the file
     * @param charset the specified charset used to read the string from the file
     *
     * @return the contents of the specified file as a string
     */
    private static String getString( URL url , Charset charset )
    {
        try ( Scanner scanner = new Scanner( URL_HANDLER.openURLStream( url ) , charset ) )
        {
            scanner.useDelimiter( "\\A" );
            return scanner.hasNext( ) ? scanner.next( ) : null;
        }
        catch ( IOException e )
        {
            GameDebugger.log( Alert.DIRE , "Could not read file.." , e );
            return null;
        }
    }

    /**
     * Gets the specified file as an input stream, either from a resource folder or file system
     *
     * @param url the URL path to the file
     *
     * @return the contents of the specified file
     */
    private static InputStream getStream( URL url )
    {
        try
        {
            InputStream stream = URL_HANDLER.openURLStream( url );
            assert stream != null;
            return stream.markSupported( ) ? stream : new BufferedInputStream( stream );
        }
        catch ( IOException e )
        {
            GameDebugger.log( Alert.DIRE , "File at " + url + " could not be opened" , e );
            return null;
        }
    }

    /**
     * Gets the specified file as a buffered image, either from a resource folder or file system
     *
     * @param url the URL path to the file
     *
     * @return the contents of the specified file
     */
    private static BufferedImage getImage( URL url )
    {
        try
        {
            BufferedImage image = ImageIO.read( url );
            if ( image == null || image.getWidth( ) == 0 || image.getHeight( ) == 0 || image.getWidth( ) % 8 != 0 || image.getHeight( ) % 8 != 0 )
            {
                GameDebugger.log( Alert.WARN , "Image dimensions are unacceptable; image should be 'image.getWidth() % 8 == 0, image.getHeight() % 8 == 0'" );
                return null;
            }

            if ( graphicsConfiguration == null )
            {
                GraphicsEnvironment graphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment( );
                GraphicsDevice graphicsDevice = graphicsEnvironment.getDefaultScreenDevice( );
                graphicsConfiguration = graphicsDevice.getDefaultConfiguration( );
            }

            BufferedImage compatibleImage = graphicsConfiguration.createCompatibleImage( image.getWidth( ) , image.getHeight( ) , Transparency.TRANSLUCENT );
            if ( compatibleImage == null )
            {
                GameDebugger.log( Alert.WARN , "Could not create compatible image" );
                return null;
            }
            compatibleImage.createGraphics( ).drawImage( image , 0 , 0 , null );
            return compatibleImage;
        }
        catch ( IOException e )
        {
            GameDebugger.log( Alert.DIRE , "File at " + url + " could not be opened" , e );
            return null;
        }
    }

    /**
     * Gets the URL of the file using the filename
     *
     * @param filename the specified name of the file, including its extension
     *
     * @return a URL to the file either from a resource folder or the file system
     */
    public static URL getURL( String filename )
    {
        URL classURL = URL_HANDLER.getURLFromClass( filename );
        if ( classURL != null )
        {
            return classURL;
        }
        try
        {
            return URL_HANDLER.getNewURL( filename );
        }
        catch ( MalformedURLException e )
        {
            try
            {
                return URL_HANDLER.getNewFileURL( filename );
            }
            catch ( MalformedURLException ex )
            {
                GameDebugger.log( Alert.DIRE , "URL could not be found.." + filename , ex );
                return null;
            }
        }
    }

    /**
     * Gets the specified file contents as a string, either from a resource folder or file system
     *
     * @param filename the specified name of the file, including its extension
     * @param charset  the specified charset used to read the string from the file
     *
     * @return the contents of the file as a string
     */
    public static String read( String filename , Charset charset )
    {
        return getString( getURL( filename ) , charset );
    }

    /**
     * Gets the specified file as an input stream, either from a resource folder or file system
     *
     * @param filename the specified name of the file, including its extension
     *
     * @return the contents of the specified file as an input stream
     */
    public static InputStream read( String filename )
    {
        return getStream( getURL( filename ) );
    }

    /**
     * Gets the specified file as a buffered image, either from a resource folder or file system
     *
     * @param filename the specified name of the file, including its extension
     *
     * @return the contents of the specified file as a buffered image
     */
    public static BufferedImage load( String filename )
    {
        return getImage( getURL( filename ) );
    }

    /**
     * Loads all images from the resource folder or filesystem. images must be formatted as png files
     * Duplicate files are ignored and only unique files are stored in the map
     *
     * @param folder the specified folder to load images from
     *
     * @return a map of buffered images loaded from the specified folder
     */
    public static Map< String, BufferedImage > loadAll( String folder )
    {
        Map< String, BufferedImage > images = new ConcurrentHashMap<>( );
        getUnknownFilenames( folder , "png" ).forEach( filename -> {
            //Format string to remove ".", "/" and any folder names
            String[] split = filename.split( "\\." )[ 0 ].split( "/" );
            String key = split[ split.length - 1 ];
            BufferedImage image = load( filename );
            images.put( key , image );
        } );
        return images;
    }

}