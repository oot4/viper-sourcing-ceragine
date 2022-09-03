/*
 * Copyright (c) 2022. The Ceragine project files, related assets and related source code
 * were created by, Ogesoka (Oge) Tasie and should not be copied or distributed without
 * consent of the creator (Ogesoka Tasie). All Rights Reserved.
 */

package resource_manager.loader;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * A class representing a URL handler
 */
final class URLHandler
{

    /**
     * Creates a new instance of a {@code URLHandler}
     */
    URLHandler( ) { }

    /**
     * Gets a URL from the provided string using a classloader
     *
     * @param filename the specified string used to generate a URL
     *
     * @return a URL object generated from the class loader
     */
    URL getURLFromClass( String filename )
    {
        return ClassLoader.getSystemResource( filename );
    }

    /**
     * Gets a new URL from the provided string
     *
     * @param filename the specified string used to generate a URL
     *
     * @return a newly generated URL object
     *
     * @throws MalformedURLException thrown if creating a new URL was unsuccessful
     */
    URL getNewURL( String filename ) throws MalformedURLException
    {
        return new URL( filename );
    }

    /**
     * Gets a URL from the provided string from a new file object
     *
     * @param filename the specified string used to generate a URL
     *
     * @return a URL object generated from a newly created file object
     *
     * @throws MalformedURLException thrown if creating a new URL from the generated file  was unsuccessful
     */
    URL getNewFileURL( String filename ) throws MalformedURLException
    {
        File file = new File( filename );
        return file.toURI( ).toURL( );
    }

    /**
     * Opens a URL stream from the provided URL
     *
     * @param url the specified URL to open a stream for
     *
     * @return an input stream object generated from the specified URL
     *
     * @throws IOException thrown if opening the URL stream was unsuccessful
     */
    InputStream openURLStream( URL url ) throws IOException
    {
        return url.openStream( );
    }

}