package render_engine.standard;

import ceragine.Game;
import ecs_architecture.api.DrawAPI;
import render_engine.api.Deliverable;
import render_engine.api.ViewportPositionValidator;
import render_engine.core.RenderLayer;
import routine_handler.routines.api.Tracker;
import utility_library.algorithms.api.Computable;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import static render_engine.core.RenderLayer.TILE_SIZE;

/**
 * A class representing render layer system that controls the render order of the available graphics layers
 */
public abstract class RenderLayerController implements DrawAPI, Computable, Deliverable
{

    /**
     * Bold font
     */
    private static final Font BOLD_FONT = new Font( Font.MONOSPACED , Font.BOLD , 12 );

    /**
     * Plain font
     */
    private static final Font PLAIN_FONT = new Font( Font.MONOSPACED , Font.PLAIN , 11 );

    /**
     * Offset along x-axis for text
     */
    private static final int TEXT_OFFSET_X = 4;

    /**
     * Offset along y-axis for text
     */
    private static final int TEXT_OFFSET_Y = 12;

    /**
     * Background color of the viewport
     */
    private static final int VIEWPORT_BACKGROUND_COLOR = 0;

    /**
     * Color of the metrics text
     */
    private static final Color METRICS_TEXT_COLOR = new Color( 3 , 160 , 98 );

    /**
     * Current offset y of text
     */
    private int textOffsetY;

    /**
     * The screen offset
     */
    private final Point offset;

    /**
     * Number of updates
     */
    private long updates = 0L;

    /**
     * Number of frames
     */
    private long frames = 0L;

    /**
     * Time to compute a single render cycle
     */
    private long computeTime;

    /**
     * Used memory
     */
    private float memoryUsage;

    /**
     * The current running version of java
     */
    private final String javaVersion;

    /**
     * Active runtime
     */
    private final Runtime runtime;

    /**
     * Available layer for rendering
     */
    private final RenderLayer layer = RenderLayer.newInstance( );

    /**
     * Tracker for monitoring updates and frames
     */
    private Tracker tracker;

    /**
     * Validates the position of sprite images on the viewport
     */
    private ViewportPositionValidator viewportPositionValidator;

    /**
     * Queue of outline render tasks
     */
    private final Set< Computable > outlineRenderQueue = ConcurrentHashMap.newKeySet( );

    /**
     * Shape outlines to render
     */
    private final Set< Shape > outlines = ConcurrentHashMap.newKeySet( );

    /**
     * Default constructor for creating a new {@code RenderLayerController}
     *
     * @param offset the specified offset used by the layers in the controller
     */
    protected RenderLayerController( Point offset )
    {
        this.offset = offset;
        runtime = Runtime.getRuntime( );
        javaVersion = System.getProperty( "java.version" ) + " (VM: " + System.getProperty( "java.vm.name" ) + ", VENDOR: " + System.getProperty( "java.vendor" ) + ")";
    }

    /**
     * Updates the current memory usage estimate
     */
    private void updateMemoryUsage( )
    {
        memoryUsage = Math.round( ( runtime.totalMemory( ) - runtime.freeMemory( ) ) / ( 1024.0F * 1024.0F ) * 10.0F ) * 0.1F;
    }

    /**
     * Draws the specified string to the graphics object
     *
     * @param gfx  the specified graphics object to draw to
     * @param text the specified text to draw
     */
    private void drawText( Graphics2D gfx , String text )
    {
        gfx.setFont( PLAIN_FONT );
        gfx.drawString( text , TEXT_OFFSET_X , textOffsetY );
        textOffsetY += TEXT_OFFSET_Y;
    }

    /**
     * Draws the debug information to the graphics object
     *
     * @param gfx        the specified graphics object to draw to
     * @param viewport   the viewport object
     * @param timeToDraw the time taken to draw a frame
     */
    private void drawInfo( Graphics2D gfx , Rectangle2D viewport , long timeToDraw )
    {
        if ( Game.configuration( ).debugInformation( ).debugMode( ) )
        {
            updateMemoryUsage( );
            textOffsetY = 0;
            gfx.setColor( METRICS_TEXT_COLOR );
            textOffsetY += TEXT_OFFSET_Y;
            gfx.setFont( BOLD_FONT );
            gfx.drawString( "[JVM]" , TEXT_OFFSET_X , textOffsetY );
            textOffsetY += TEXT_OFFSET_Y;
            drawText( gfx , "java      : " + javaVersion );
            drawText( gfx , "memory    : " + String.format( "%-5.5s" , memoryUsage ) + " mb" );
            drawText( gfx , "threads   : " + Thread.activeCount( ) );
            drawText( gfx , "[ENGINE]    " );
            drawText( gfx , "draw_tt   : " + ( System.nanoTime( ) - timeToDraw ) / 1.0E6D );
            drawText( gfx , "prep_tt   : " + computeTime / 1.0E6D );
            drawText( gfx , "viewport  : " + viewport.getX( ) + " " + viewport.getY( ) );
            drawText( gfx , "updates   : " + updates + "/s" );
            drawText( gfx , "frames    : " + frames + "/s" );
        }
    }

    /**
     * Draws a single outline to the graphics object
     *
     * @param gfx    the specified graphics object to draw to
     * @param shape  the specified shape of the outline to draw
     * @param stroke the specified stroke settings used when drawing
     */
    private void drawOutline( Graphics2D gfx , Shape shape , Stroke stroke )
    {
        if ( shape == null )
        {
            return;
        }
        Stroke current = gfx.getStroke( );
        gfx.setStroke( stroke );
        gfx.draw( shape );
        gfx.setStroke( current );
    }

    /**
     * Draws a single outline to the graphics object
     *
     * @param gfx       the specified graphics object to draw to
     * @param shape     the specified shape of the outline to draw
     * @param transform the specified transformation to apply to the shape
     * @param stroke    the specified stroke settings used when drawing
     */
    private void drawOutline( Graphics2D gfx , Shape shape , AffineTransform transform , Stroke stroke )
    {
        if ( transform == null )
        {
            drawOutline( gfx , shape , stroke );
            return;
        }
        drawOutline( gfx , transform.createTransformedShape( shape ) , stroke );
    }

    /**
     * Draws the stored outlines to the viewport area
     *
     * @param gfx      the specified graphics object to draw to
     * @param viewport the specified bounds of the viewport in to the game
     */
    private void drawOutlines( Graphics2D gfx , Rectangle2D viewport )
    {
        gfx.setColor( Color.GREEN );
        Stroke stroke = new BasicStroke( 1 / viewportPositionValidator.getRenderScale( ) );
        outlines.forEach( shape -> {
            if ( shape != null )
            {
                Object hint = gfx.getRenderingHint( RenderingHints.KEY_ANTIALIASING );
                gfx.setRenderingHint( RenderingHints.KEY_ANTIALIASING , RenderingHints.VALUE_ANTIALIAS_OFF );
                double anchorX = shape.getBounds( ).getX( ) + shape.getBounds( ).getWidth( ) * 0.5D;
                double anchorY = shape.getBounds( ).getY( ) + shape.getBounds( ).getHeight( ) * 0.5D;
                AffineTransform transform = new AffineTransform( );
                transform.translate( - viewport.getX( ) , - viewport.getY( ) );
                transform.rotate( Math.toRadians( 0 ) , anchorX , anchorY );
                drawOutline( gfx , shape , transform , stroke );
                gfx.setRenderingHint( RenderingHints.KEY_ANTIALIASING , hint );
            }
        } );
    }

    /**
     * Adds a shape to be drawn to the viewport area
     *
     * @param shape the specified shape to draw
     */
    public final void drawOutline( Shape shape )
    {
        outlineRenderQueue.add( ( ) -> outlines.add( shape ) );
    }

    /**
     * Sets the offset of images when they are rendered
     *
     * @param x the specified value {@code x} to offset tile images by
     * @param y the specified value {@code y} to offset tile images by
     */
    public final void setRenderOffset( int x , int y )
    {
        offset.move( x , y );
    }

    /**
     * Clears the render packets and computable functions. This method is used to ensure that the system does not oversize the render queue
     */
    public final void clear( )
    {
        layer.clear( );
        outlines.clear( );
        outlineRenderQueue.clear( );
    }

    /**
     * Delivers the current supply of render packets to the graphics object.
     * This method calls each layer in the order {@code FLOOR}, {@code ENVIRONMENT}, {@code CHARACTER}, {@code USER_INTERFACE}, {@code OVERLAY}
     *
     * @param gfx      the specified graphics object to deliver render packets to
     * @param scale    the specified scale to apply to the graphics object
     * @param viewport the specified bounds of the viewport in to the game
     */
    @Override
    public final void deliver( Graphics2D gfx , float scale , Rectangle2D viewport )
    {
        long start = 0;
        AffineTransform transform = null;
        try
        {
            transform = gfx.getTransform( );
            gfx.scale( scale , scale );
            gfx.setColor( new Color( VIEWPORT_BACKGROUND_COLOR , 0 , VIEWPORT_BACKGROUND_COLOR ) );
            gfx.fill( new Rectangle2D.Double( 0.0D , 0.0D , viewport.getWidth( ) , viewport.getHeight( ) ) );
            start = System.nanoTime( );
            layer.deliver( gfx , viewport , offset , viewportPositionValidator );
            drawOutlines( gfx , viewport );
        }
        finally
        {
            gfx.setTransform( transform );
            drawInfo( gfx , viewport , start );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void draw( String spritesheet , double x , double y , int tile , int mirror , int scale , int alpha )
    {
        //To prevent unnecessary computations, we modify the x and y coordinates here and determine whether to draw or not
        if ( viewportPositionValidator == null )
        {
            throw new IllegalStateException( "viewport position validator, must be initialized first" );
        }
        if ( viewportPositionValidator.intersects( offset , x , y , TILE_SIZE * scale ) )
        {
            //The render packet delivery process will ensure the drawn image is relative to the viewport area
            layer.draw( spritesheet , x , y , tile , mirror , scale , alpha );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void compute( )
    {
        long start = System.nanoTime( );
        layer.compute( );
        outlineRenderQueue.forEach( Computable::compute );
        computeTime = System.nanoTime( ) - start;
    }

    /**
     * Gets the tracker function of the render layer controller
     *
     * @return the updates-per-second and frames-per-second tracker object
     */
    public final Tracker tracker( )
    {
        if ( tracker == null )
        {
            tracker = ( trackedUpdates , trackedFrames ) -> {
                updates = trackedUpdates;
                frames = trackedFrames;
            };
        }
        return tracker;
    }

    /**
     * Sets the viewport position validator. Can only be done once
     *
     * @param viewportPositionValidator the specified viewport position validator to assign
     */
    public final void setViewportPositionValidator( ViewportPositionValidator viewportPositionValidator )
    {
        if ( this.viewportPositionValidator == null )
        {
            this.viewportPositionValidator = viewportPositionValidator;
        }
    }

}