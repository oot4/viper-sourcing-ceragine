package render_engine.core;

import utility_library.algorithms.core.Clamp;

import java.awt.*;
import java.awt.image.BufferStrategy;

/**
 * A class representing a Screen for drawing images to
 */
public final class ScreenContainer
{

    /**
     * Graphics buffer size
     */
    private final int buffers;

    /**
     * Drawing canvas
     */
    private final Canvas screen;

    /**
     * Canvas buffer strategy
     */
    private BufferStrategy strategy;

    /**
     * Constructor for creating a new {@code Screen} instance
     *
     * @param buffers the specified buffer size to apply to the canvas buffer strategy
     * @param screen  the specified canvas used to draw images
     */
    private ScreenContainer( int buffers , Canvas screen )
    {
        this.buffers = buffers;
        this.screen = screen;
    }

    /**
     * Creates a new screen with the specified parameter values
     *
     * @param hostContainer the specified {@code JFrame} to host the screen
     * @param size          the dimensions to give the screen
     * @param buffers       the specified number of buffers to apply to the buffer strategy
     *
     * @return a new instance of a screen with a canvas to draw on
     */
    public static ScreenContainer from( HostContainer hostContainer , Dimension size , int buffers )
    {
        Canvas graphicsScreen = new Canvas( );
        graphicsScreen.setBackground( Color.BLACK );
        graphicsScreen.setFont( new Font( Font.MONOSPACED , Font.BOLD , 12 ) );
        graphicsScreen.setSize( size );
        graphicsScreen.setPreferredSize( size );
        hostContainer.host( ).add( graphicsScreen );
        int bufferSize = Clamp.toInt( buffers , 1 , 4 );
        return new ScreenContainer( bufferSize , graphicsScreen );
    }

    /**
     * Gets the current buffer strategy. The strategy is initialized with the specified number of buffers on the first call to this method
     *
     * @return the current buffering strategy for rendering graphics
     */
    public BufferStrategy strategy( )
    {
        if ( strategy == null )
        {
            screen.createBufferStrategy( buffers );
            strategy = screen.getBufferStrategy( );
        }
        return strategy;
    }

    /**
     * Gets the screen object contained in this screen container
     *
     * @return a canvas for rendering images to
     */
    public Canvas screen( )
    {
        return screen;
    }

}