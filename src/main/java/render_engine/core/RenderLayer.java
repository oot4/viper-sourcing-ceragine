package render_engine.core;

import caching.core.InMemoryCache;
import ceragine.Game;
import ecs_architecture.api.DrawAPI;
import game_debugger.Alert;
import game_debugger.GameDebugger;
import render_engine.api.ViewportPositionValidator;
import utility_library.algorithms.api.Computable;
import utility_library.algorithms.core.Clamp;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Optional;
import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * A class representing a rendering layer
 */
public final class RenderLayer implements DrawAPI, Computable
{

    /**
     * Range of allowed indexes along any axis on a tile
     */
    private static final int INDEX_RANGE = 7;

    /**
     * Size of a single tile
     */
    public static final int TILE_SIZE = 8;

    /**
     * Green colour bitwise component
     */
    private static final int GREEN_BITWISE = 8;

    /**
     * Red colour bitwise component
     */
    private static final int RED_BITWISE = 16;

    /**
     * Alpha bitwise component
     */
    private static final int ALPHA_BITWISE = 24;

    /**
     * RGB colour bitwise component
     */
    private static final int RGB_MASK = 255;

    /**
     * Factor for applying mirroring to a tile
     */
    private static final Point MIRROR_FACTOR = new Point( 1 , 2 );

    /**
     * Queue of render tasks
     */
    private final Deque< Computable > renderQueue = new ConcurrentLinkedDeque<>( );

    /**
     * Render packet queue
     */
    private final Deque< RenderPacket > packets = new ConcurrentLinkedDeque<>( );

    /**
     * Cache for images, speeds up rendering
     */
    private final InMemoryCache< String, BufferedImage > cache = InMemoryCache.of( 2048 );

    /**
     * Constructor for creating a new instance of a {@code RenderLayer}
     */
    private RenderLayer( ) { }

    /**
     * Creates a new instance of a {@code RenderLayer}
     *
     * @return a new render layer object
     */
    public static RenderLayer newInstance( )
    {
        return new RenderLayer( );
    }

    /**
     * Gets the offset for the specified tile on a spritesheet
     *
     * @param spritesheet the specified spritesheet that owns the tile
     * @param tile        the specified tile on the spritesheet
     *
     * @return a number corresponding to an offset value for a tile
     */
    private static int tileOffset( String spritesheet , int tile )
    {
        Dimension size = Game.sprites( ).dimension( spritesheet );
        int tileWidth = size.width / TILE_SIZE;
        int tileHeight = size.height / TILE_SIZE;
        if ( tile < 0 || tile >= tileWidth * tileHeight )
        {
            GameDebugger.log( Alert.INFO , "tile is illegal" );
            throw new IllegalArgumentException( );
        }
        int x = tile % tileWidth;
        int y = tile / tileWidth;
        return ( x << 3 ) + ( y << 3 ) * size.width;
    }

    /**
     * Gets the {@code (x, y)} location of a pixel on a tile
     *
     * @param index the specified index to generate a location for
     *
     * @return a point {@code (x, y)} representing the location of the pixel
     */
    private static Point location( int index )
    {
        return location( index , TILE_SIZE );
    }

    /**
     * Gets the {@code (x, y)} location of a pixel on a tile
     *
     * @param index the specified index to generate a location for
     * @param width the width of a 2D plane
     *
     * @return a point {@code (x, y)} representing the location of the pixel
     */
    private static Point location( int index , int width )
    {
        return new Point( index % width , index / width );
    }

    /**
     * Gets the location a tile is destined for
     *
     * @param position the specified destination coordinates
     * @param index    the specified index of the pixel
     * @param scaling  the specified scaling to apply
     *
     * @return a new point with the calculated destination coordinates
     */
    private static Point destination( Point position , int index , int scaling )
    {
        Point location = location( index );
        int x = destination( location.x , position.x , scaling );
        int y = destination( location.y , position.y , scaling );
        return new Point( x , y );
    }

    /**
     * Gets the location a tile is destined for
     *
     * @param location the specified index of the pixel
     * @param position the exact {@code x} or {@code y} value the tile is destined for
     * @param scaling  the specified scaling to apply to the pixel
     *
     * @return a value corresponding to the new destination of the pixel
     */
    private static int destination( int location , int position , int scaling )
    {
        return location + position + ( location * scaling ) - ( ( scaling << 0x3 ) / 0x2 );
    }

    /**
     * Applies mirroring to the pixel at the specified index
     *
     * @param index  the specified index value to apply mirroring to
     * @param mirror the specified mirroring effect to apply
     *
     * @return a new point with applied mirroring
     */
    private static Point mirror( int index , int mirror )
    {
        Point location = location( index );
        if ( ( mirror & MIRROR_FACTOR.x ) > 0 )
        {
            location.x = INDEX_RANGE - location.x;
        }
        if ( ( mirror & MIRROR_FACTOR.y ) > 0 )
        {
            location.y = INDEX_RANGE - location.y;
        }
        return location;
    }

    /**
     * Gets the colour of the pixel located on a tile from a spritesheet
     *
     * @param spritesheet the spritesheet with ownership of the tile
     * @param pixel       the specified pixel location to find the colour of
     * @param tileOffset  the offset value applied to the tile
     *
     * @return an integer corresponding to an ARGB or RGB value
     */
    private static int colour( String spritesheet , Point pixel , int tileOffset )
    {
        int width = Game.sprites( ).dimension( spritesheet ).width;
        int index = tileOffset + pixel.x + pixel.y * width;
        if ( index < 0 || index >= Game.sprites( ).pixels( spritesheet ).length )
        {
            GameDebugger.log( Alert.DIRE , "The index is out of bounds, could not retrieve pixel color" );
            throw new IndexOutOfBoundsException( "The index is out of bounds, could not retrieve pixel color" );
        }
        return Game.sprites( ).pixels( spritesheet )[ index ];
    }


    /**
     * Gets the argb value calculated from the color value of a pixel
     *
     * @param colour       the specified colour value to calculate an argb value for
     * @param transparency the specified alpha value to apply to the colour
     *
     * @return the calculated argb value of the pixel as an integer
     */
    private static int argb( int colour , int transparency )
    {
        int a = ( colour >> ALPHA_BITWISE ) & RGB_MASK;
        if ( a == 0 )
        {
            return colour;
        }
        int r = ( colour >> RED_BITWISE ) & RGB_MASK;
        int g = ( colour >> GREEN_BITWISE ) & RGB_MASK;
        int b = colour & RGB_MASK;
        return ( transparency << ALPHA_BITWISE ) + ( r << RED_BITWISE ) + ( g << GREEN_BITWISE ) + b;
    }

    /**
     * Draws an image to the specified graphics object at user location
     *
     * @param gfx   the specified graphics object to draw to
     * @param image the image to draw
     * @param x     the specified x location to draw the image
     * @param y     the specified y location to draw the image
     */
    private static void draw( Graphics2D gfx , Image image , double x , double y )
    {
        if ( image == null )
        {
            return;
        }
        final AffineTransform transform = AffineTransform.getTranslateInstance( x , y );
        gfx.drawImage( image , transform , null );
    }

    /**
     * Gets a tile drawn from the specified values. Also caches the result
     *
     * @param spritesheet the specified spritesheet to get the image from
     * @param tile        the specified tile image on the spritesheet to render
     * @param mirror      the specified direction to mirror the image
     * @param scale       the specified scale to resize the image to
     * @param alpha       the specified alpha value to apply to the image
     *
     * @return the image of the tile
     */
    private BufferedImage getTileImage( String spritesheet , int tile , int mirror , int scale , int alpha )
    {
        int imageSize = TILE_SIZE * scale;
        int tileOffset = tileOffset( spritesheet , tile );
        int scaling = scale - 1;
        int transparency = Clamp.toInt( alpha , 0 , 255 );
        BufferedImage image = new BufferedImage( imageSize , imageSize , BufferedImage.TYPE_INT_ARGB );
        int[] data = ( ( DataBufferInt ) image.getRaster( ).getDataBuffer( ) ).getData( );
        for ( int index = 0; index < TILE_SIZE * TILE_SIZE; index++ )
        {
            Point pixel = mirror( index , mirror );
            Point destination = destination( new Point( scaling * ( TILE_SIZE / 2 ) , scaling * ( TILE_SIZE / 2 ) ) , index , scaling );
            int colour = colour( spritesheet , pixel , tileOffset );
            int argb = argb( colour , transparency );
            for ( int s = 0; s < scale * scale; s++ )
            {
                Point scaledPixel = location( s , scale );
                boolean widthOutOfBounds = destination.x + scaledPixel.x < 0 || destination.x + scaledPixel.x >= image.getWidth( );
                boolean heightOutOfBounds = destination.y + scaledPixel.y < 0 || destination.y + scaledPixel.y >= image.getHeight( );
                if ( widthOutOfBounds || heightOutOfBounds )
                {
                    continue;
                }
                int destinationIndex = ( destination.x + scaledPixel.x ) + ( destination.y + scaledPixel.y ) * image.getWidth( );
                if ( destinationIndex < 0 || destinationIndex >= data.length )
                {
                    GameDebugger.log( Alert.DIRE , "The index is out of bounds" );
                    throw new IndexOutOfBoundsException( "The index is out of bounds" );
                }
                data[ destinationIndex ] = argb;
            }
        }
        return image;
    }

    /**
     * Draws a sprite which is an {@code 8x8} pixel image
     *
     * @param spritesheet the specified spritesheet to get the image from
     * @param x           the specified {@code x} position to render the image
     * @param y           the specified {@code y} position to render the image
     * @param tile        the specified tile image on the spritesheet to render
     * @param mirror      the specified direction to mirror the image
     * @param scale       the specified scale to resize the image to
     * @param alpha       the specified alpha value to apply to the image
     */
    private void internalDraw( String spritesheet , double x , double y , int tile , int mirror , int scale , int alpha )
    {
        String key = spritesheet + tile + mirror + scale + alpha;
        if ( cache.contains( key ) )
        {
            Optional< BufferedImage > optional = cache.get( key );
            optional.ifPresent( image -> packets.offer( RenderPacket.from( x , y , image ) ) );
        }
        else
        {
            BufferedImage image = getTileImage( spritesheet , tile , mirror , scale , alpha );
            cache.put( key , image );
            packets.offer( RenderPacket.from( x , y , image ) );
        }
    }

    /**
     * Delivers the current supply of render packets to the graphics object
     *
     * @param gfx                       the specified graphics object to deliver render packets to
     * @param viewport                  the specified bounds of the window in to the game
     * @param offset                    the specified offset to apply to the position of sprites
     * @param viewportPositionValidator the specified viewport position validator used to validate the position of sprite images
     */
    public void deliver( Graphics2D gfx , Rectangle2D viewport , Point offset , ViewportPositionValidator viewportPositionValidator )
    {
        RenderPacket packet;
        gfx.setComposite( AlphaComposite.getInstance( AlphaComposite.SRC_OVER , 1.0F ) );
        ArrayDeque< RenderPacket > packetQueue = new ArrayDeque<>( packets );
        while ( ( packet = packetQueue.poll( ) ) != null )
        {
            if ( viewportPositionValidator.intersects( offset , packet.destination( ).getX( ) , packet.destination( ).getY( ) , packet.image( ).getWidth( ) ) )
            {
                Point2D destination = viewportPositionValidator.getViewportPosition( offset , packet.destination( ).getX( ) , packet.destination( ).getY( ) , packet.image( ).getWidth( ) );
                draw( gfx , packet.image( ) , destination.getX( ) - viewport.getX( ) , destination.getY( ) - viewport.getY( ) );
            }
        }
    }

    /**
     * Clears the render packets and computable functions. This method is used to ensure that the system does not oversize the render queue
     */
    public void clear( )
    {
        renderQueue.clear( );
        packets.clear( );
        cache.clear( );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void compute( )
    {
        Computable pointer;
        while ( ( pointer = renderQueue.poll( ) ) != null )
        {
            pointer.compute( );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void draw( String spritesheet , double x , double y , int tile , int mirror , int scale , int alpha )
    {
        renderQueue.offer( ( ) -> internalDraw( spritesheet , x , y , tile , mirror , scale , alpha ) );
    }

    /**
     * A class representing a packet of rendering data
     */
    private static final class RenderPacket
    {

        /**
         * The destination location of the image
         */
        private final Point2D destination;

        /**
         * The image to render
         */
        private final BufferedImage image;

        /**
         * Constructor for creating a new instance of a {@code RenderPacket}
         *
         * @param destination the specified destination of the image to render
         * @param image       the specified image to render
         */
        private RenderPacket( Point2D destination , BufferedImage image )
        {
            this.destination = destination;
            this.image = image;
        }

        /**
         * Creates a new instance of a {@code RenderPacket}
         *
         * @param x     the specified position {@code x}
         * @param y     the specified position {@code y}
         * @param image the specified image to store
         *
         * @return a new render packet object
         */
        public static RenderPacket from( double x , double y , BufferedImage image )
        {
            Point2D destination = new Point2D.Double( x , y );
            return new RenderPacket( destination , image );
        }

        /**
         * Gets the destination of the image in this render packet
         *
         * @return the point to render the image in the render packet
         */
        public Point2D destination( )
        {
            return destination;
        }

        /**
         * Gets the image to be rendered
         *
         * @return the tile image stored in the render packet
         */
        public BufferedImage image( )
        {
            return image;
        }

    }

}