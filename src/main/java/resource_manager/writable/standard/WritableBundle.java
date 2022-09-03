/*
 * Copyright (c) 2022. The Ceragine project files, related assets and related source code were created by, Ogesoka (Oge) Tasie and should not be copied or distributed without consent of the creator (Ogesoka Tasie). All Rights Reserved.
 */

package resource_manager.writable.standard;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;
import game_debugger.Alert;
import game_debugger.GameDebugger;
import resource_manager.loader.ResourceIO;
import resource_manager.writable.api.WritableResource;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * An abstract class to be extended by a contained or constant data that can be written to or read from, a file or archive.
 */
@WritableResource
public abstract class WritableBundle
{

    /**
     * JSON file extension
     */
    @JsonIgnore
    private static final String JSON_EXTENSION = "json";

    /**
     * Ginda file extension
     */
    @JsonIgnore
    private static final String GINDA_EXTENSION = "ginda";

    /**
     * Configuration properties file extension
     */
    @JsonIgnore
    private static final String PROPERTIES_EXTENSION = "properties";

    /**
     * Archive file extension
     */
    @JsonIgnore
    private static final String GINDA_ARCHIVE_EXTENSION = "gar";

    /**
     * Pattern for default filename
     */
    @JsonIgnore
    private static final Pattern DEFAULT_FILENAME_PATTERN = Pattern.compile( "^##default$" );

    /**
     * Pattern for filename
     */
    @JsonIgnore
    private static final Pattern FILENAME_PATTERN = Pattern.compile( "^((##default)|([a-zA-Z\\d_-])+)$" );

    /**
     * Pattern for extension string
     */
    @JsonIgnore
    private static final Pattern EXTENSION_PATTERN = Pattern.compile( "^(" + JSON_EXTENSION + "|" + GINDA_EXTENSION + "|" + PROPERTIES_EXTENSION + "|" + GINDA_ARCHIVE_EXTENSION + ")$" );

    /**
     * Pattern for version string
     */
    @JsonIgnore
    private static final Pattern VERSION_PATTERN = Pattern.compile( "^(\\d*)\\.(\\d*)\\.(\\d*)$" );

    /**
     * Writable bundle version
     */
    private String version;

    /**
     * Default constructor for creating a new instance of a {@code WritableBundle}
     */
    protected WritableBundle( )
    {
    }

    /**
     * Gets the current version of the writable bundle
     *
     * @return the version of the writable bundle as a string
     */
    public String getVersion( )
    {
        return version;
    }

    /**
     * Sets the current version of the writable bundle
     *
     * @param version the specified version string to give the writable bundle
     */
    public void setVersion( String version )
    {
        this.version = version;
    }

    /**
     * Validates the values provided in the {@link WritableResource writable resource} annotation against some regex conditions
     *
     * @param writableResource the specified writable resource to validate
     *
     * @return an object containing a generated filename with extension and version string as a {@link WritableInfo writable information} object
     */
    private WritableInfo validateWritableResource( WritableResource writableResource )
    {
        //Validate filename
        String filename = ( ! DEFAULT_FILENAME_PATTERN.matcher( writableResource.filename( ) ).matches( ) && ! writableResource.filename( ).isBlank( ) ) ? writableResource.filename( ) : this.getClass( ).getSimpleName( );
        if ( ! FILENAME_PATTERN.matcher( filename ).matches( ) )
        {
            throw new IllegalArgumentException( "Filename in writable resource does not follow the regular expression: '^(##default|[a-zA-Z\\\\d_-])$'" );
        }
        //Validate extension
        String extension = writableResource.extension( ).isBlank( ) ? JSON_EXTENSION : writableResource.extension( );
        if ( ! EXTENSION_PATTERN.matcher( extension ).matches( ) )
        {
            throw new IllegalArgumentException( "Extension in writable resource does not follow the regular expression: '^(json | ginda | properties | gar)$'" );
        }
        String currentVersion = writableResource.version( ).isBlank( ) ? "1.0.0" : writableResource.version( );
        if ( ! VERSION_PATTERN.matcher( currentVersion ).matches( ) )
        {
            throw new IllegalArgumentException( "Versioning does not follow the regular expression: '^(\\\\d*)\\\\.(\\\\d*)\\\\.(\\\\d*)$'" );
        }
        return new WritableInfo( filename + "." + extension , currentVersion );
    }

    /**
     * Gets a list of field members available in the class type recursively
     *
     * @param type the class type to search in for fields
     *
     * @return a list of fields found in the class and its super class(es)
     */
    private static List< Field > findFields( Class< ? > type )
    {
        if ( type == WritableBundle.class || type == null )
        {
            return Collections.emptyList( );
        }
        List< Field > fields = new CopyOnWriteArrayList<>( );
        fields.addAll( Arrays.asList( type.getDeclaredFields( ) ) );
        fields.addAll( findFields( type.getSuperclass( ) ) );
        return fields;
    }

    /**
     * Writes out the bundle to a file or archive
     *
     * @param fileOutputStream the specified file output stream of data to write
     * @param version          the specified version of the file or archive
     * @param writableBundle   the specified data object to write
     * @param archive          should the output be archived?
     *
     * @throws IOException throws an exception if writing the bundle to a file or archive failed
     */
    private static void write( FileOutputStream fileOutputStream , String version , WritableBundle writableBundle , boolean archive ) throws IOException
    {
        ObjectMapper mapper = new ObjectMapper( );
        writableBundle.version = version;
        String jsonString = mapper.writerWithDefaultPrettyPrinter( ).writeValueAsString( writableBundle );
        byte[] data = jsonString.getBytes( StandardCharsets.UTF_8 );
        if ( archive )
        {
            try ( GZIPOutputStream gzipOutputStream = new GZIPOutputStream( fileOutputStream ) )
            {
                gzipOutputStream.write( data );
                gzipOutputStream.flush( );
            }
            return;
        }
        try ( ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream( ) )
        {
            byteArrayOutputStream.write( data );
            byteArrayOutputStream.flush( );
            byteArrayOutputStream.writeTo( fileOutputStream );
        }
    }

    /**
     * Writes out the bundle to a file or archive
     *
     * @param url            the specified URL location to write to
     * @param version        the specified version of the file or archive
     * @param writableBundle the specified data object to write
     * @param archive        should the output be archived?
     *
     * @throws URISyntaxException   throws an exception if the syntax of the generated URLs URI is incorrect
     * @throws IOException          throws an exception if writing the bundle to a file or archive failed
     * @throws NullPointerException throws an exception if the generated URL is null
     */
    private static void write( URL url , String version , WritableBundle writableBundle , boolean archive ) throws URISyntaxException, IOException, NullPointerException
    {
        if ( url == null )
        {
            throw new NullPointerException( "Provided URL is null" );
        }
        File file = new File( url.toURI( ) );
        if ( file.exists( ) )
        {
            Files.delete( file.toPath( ).toAbsolutePath( ) );
        }
        try ( FileOutputStream fileOutputStream = new FileOutputStream( file ) )
        {
            write( fileOutputStream , version , writableBundle , archive );
            fileOutputStream.flush( );
        }
    }

    /**
     * Writes out the bundle to a file or archive
     *
     * @param writableInfo   the specified {@link WritableInfo writable information} object containing the filename and version to use
     * @param writableBundle the specified data object to write
     * @param archive        should the output be archived?
     *
     * @throws URISyntaxException   throws an exception if the syntax of the generated URLs URI is incorrect
     * @throws IOException          throws an exception if writing the bundle to a file or archive failed
     * @throws NullPointerException throws an exception if the generated URL is null
     */
    private static void write( WritableInfo writableInfo , WritableBundle writableBundle , boolean archive ) throws URISyntaxException, IOException, NullPointerException
    {
        String filename;
        if ( archive )
        {
            filename = writableInfo.getFilenameWithExtension( ).endsWith( "." + GINDA_ARCHIVE_EXTENSION ) ? writableInfo.getFilenameWithExtension( ) : writableInfo.getFilenameWithExtension( ).split( "\\." )[ 0 ] + "." + GINDA_ARCHIVE_EXTENSION;
        }
        else
        {
            filename = writableInfo.getFilenameWithExtension( ).endsWith( "." + GINDA_ARCHIVE_EXTENSION ) ? writableInfo.getFilenameWithExtension( ).split( "\\." )[ 0 ] + "." + GINDA_EXTENSION : writableInfo.getFilenameWithExtension( );
        }
        URL url = ResourceIO.getURL( filename );
        write( url , writableInfo.getVersion( ) , writableBundle , archive );
    }

    /**
     * Writes out the bundle to a file or archive specified in the {@code WritableResource} annotation of the class
     *
     * @param archive should the output be archived?
     */
    private void write( boolean archive )
    {
        WritableResource writableResource = this.getClass( ).getAnnotation( WritableResource.class );
        WritableInfo writableInfo = validateWritableResource( writableResource );
        try
        {
            write( writableInfo , this , archive );
        }
        catch ( URISyntaxException | IOException e )
        {
            GameDebugger.log( Alert.WARN , "Could not complete the operation to write the data to a file" , e );
        }
    }

    /**
     * Writes out the bundle to a file specified in {@code WritableResource} annotation of the object
     */
    protected final void writeToFile( )
    {
        write( false );
    }

    /**
     * Writes out the bundle to an archive specified in the {@code WritableResource} annotation of the object
     */
    protected final void writeToArchive( )
    {
        write( true );
    }

    /**
     * Reads in the bundle from an archive or file
     *
     * @param inputStream the specified input stream of data
     * @param type        the class type of the object to produce
     * @param <T>         the generic type this method operates on
     *
     * @return an object mapped to the data read from the file or archive
     *
     * @throws IOException throws an exception if reading the data from the archive or file failed
     */
    private static < T extends WritableBundle > T read( InputStream inputStream , Class< T > type ) throws IOException
    {
        ObjectMapper mapper = new ObjectMapper( );
        return mapper.readValue( inputStream , type );
    }

    /**
     * Reads in the bundle from an archive or file
     *
     * @param writableInfo the specified {@link WritableInfo writable information} object containing the filename and version to use
     * @param type         the class type of the object to produce
     * @param <T>          the generic type this method operates on
     *
     * @return an object mapped to the data read from the file or archive
     *
     * @throws IOException throws an exception if reading the data from the archive or file failed
     */
    private static < T extends WritableBundle > T read( WritableInfo writableInfo , Class< T > type ) throws IOException
    {
        String filename = writableInfo.getFilenameWithExtension( );
        T writableBundle;
        try ( InputStream inputStream = ResourceIO.read( filename ) )
        {
            if ( filename.endsWith( "." + GINDA_ARCHIVE_EXTENSION ) )
            {
                GZIPInputStream gzipInputStream = new GZIPInputStream( inputStream );
                writableBundle = read( gzipInputStream , type );
            }
            else
            {
                writableBundle = read( inputStream , type );
            }
        }
        return writableBundle;
    }

    /**
     * Reads in the bundle from an archive or file specified in the {@code WritableResource} annotation of the object
     *
     * @param type the class type of the object to produce
     * @param <T>  the generic type this method operates on
     *
     * @return {@code True} if the file was successfully read. {@code False} otherwise
     */
    private < T extends WritableBundle > boolean read( Class< T > type )
    {
        WritableResource writableResource = this.getClass( ).getAnnotation( WritableResource.class );
        WritableInfo writableInfo = validateWritableResource( writableResource );
        try
        {
            List< Field > fields = findFields( type );
            T writableBundle = read( writableInfo , type );
            if ( writableBundle == null )
            {
                return false;
            }
            for ( Field field : fields )
            {
                if ( Modifier.isStatic( field.getModifiers( ) ) || Modifier.isFinal( field.getModifiers( ) ) )
                {
                    //Static or final fields are not considered
                    continue;
                }

                field.setAccessible( true );
                field.set( this , field.get( writableBundle ) );
            }
            this.version = writableBundle.getVersion( );
            return true;
        }
        catch ( IllegalArgumentException | IOException | IllegalAccessException e )
        {
            GameDebugger.log( Alert.WARN , "Could not complete the operation to read the data from file" , e );
            return false;
        }

    }

    /**
     * Reads in the bundle from an archive or file specified in the {@code WritableResource} annotation of the object
     *
     * @return {@code True} if the file was successfully read. {@code False} otherwise
     */
    protected final boolean read( )
    {
        return ! read( this.getClass( ) );
    }

    /**
     * A class representing a set of string data for a writable resource
     */
    private static final class WritableInfo
    {

        /**
         * The filename and its extension
         */
        private final String filenameWithExtension;

        /**
         * Version string of the writable resource
         */
        private final String version;

        /**
         * Creates a new instance of a {@code WritableInfo} set
         *
         * @param filenameWithExtension the specified filename and extension
         * @param version               the specified version of the writable resource
         */
        public WritableInfo( String filenameWithExtension , String version )
        {
            this.filenameWithExtension = filenameWithExtension;
            this.version = version;
        }

        /**
         * Gets the filename with an extension
         *
         * @return the string value of the filename with extension
         */
        public String getFilenameWithExtension( )
        {
            return filenameWithExtension;
        }

        /**
         * Gets the version number
         *
         * @return the string value of the version
         */
        public String getVersion( )
        {
            return version;
        }

    }

}