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
        computeTime = System.nanoTime( ) - start;
    }

    /**
     * Gets the tracker function of the render layer controller
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