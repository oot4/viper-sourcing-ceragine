package routine_handler.routines.core;

import ceragine.Game;
import render_engine.api.Deliverable;
import render_engine.core.ScreenContainer;
import render_engine.standard.DisplayController;
import routine_handler.components.api.DisplayListener;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

/**
 * A class representing a display routine for show graphical output
 */
public final class DisplayRoutine extends DisplayController implements DisplayListener
{

    private Graphics2D context;

    private BufferedImage buffer;

    /**
     * Delivers rendered graphics to the screen
     */
    private final Deliverable deliverable;

    /**
     * Constructor for creating a new {@code DisplayRoutine}
     *
     * @param deliverable the specified deliverable strategy
     */
    private DisplayRoutine( Deliverable deliverable )
    {
        this.deliverable = deliverable;
    }

    /**
     * Creates a new instance of a {@code DisplayRoutine}
     *
     * @param deliverable the specified delivery strategy for displaying graphics
     *
     * @return a new instance of a {@code DisplayRoutine} for controlling the flow of graphics output
     */
    public static DisplayRoutine from( Deliverable deliverable )
    {
        return new DisplayRoutine( deliverable );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void setup( ScreenContainer container )
    {
        buffer = new BufferedImage( container.screen( ).getWidth( ) , container.screen( ).getHeight( ) , BufferedImage.TYPE_INT_ARGB );
        context = buffer.createGraphics( );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void display( )
    {
        Graphics2D gfx = null;
        do
        {
            try
            {
                gfx = ( Graphics2D ) strategy( ).getDrawGraphics( );
                context.setColor( screen( ).getBackground( ) );
                Rectangle2D boundary = new Rectangle( 0 , 0 , screen( ).getWidth( ) , screen( ).getHeight( ) );
                context.setClip( boundary );
                context.fill( boundary );
                context.setRenderingHint( RenderingHints.KEY_ANTIALIASING , Game.configuration( ).graphicsInformation( ).antiAliasing( ) ? RenderingHints.VALUE_ANTIALIAS_ON : RenderingHints.VALUE_ANTIALIAS_OFF );
                context.setRenderingHint( RenderingHints.KEY_INTERPOLATION , Game.configuration( ).graphicsInformation( ).colorInterpolation( ) ? RenderingHints.VALUE_INTERPOLATION_BILINEAR : RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR );
                updateViewport( );
                if ( deliverable != null )
                {
                    deliverable.deliver( context , scaling( ) , viewport( ) );
                }
                gfx.drawImage( buffer , 0 , 0 , screen( ).getWidth( ) , screen( ).getHeight( ) , null );
            }
            finally
            {
                if ( gfx != null )
                {
                    gfx.dispose( );
                }
            }
            strategy( ).show( );
        }
        while ( strategy( ).contentsLost( ) );
        Toolkit.getDefaultToolkit( ).sync( );
    }

}