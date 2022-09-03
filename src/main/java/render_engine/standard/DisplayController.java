package render_engine.standard;

import game_debugger.Alert;
import game_debugger.GameDebugger;
import render_engine.api.ViewportPositionValidator;
import render_engine.core.HostContainer;
import render_engine.core.ScreenContainer;
import render_engine.core.ViewportContainer;
import resource_manager.writable.api.ClientInformationSet;
import resource_manager.writable.api.GameInformationSet;
import resource_manager.writable.api.GraphicsInformationSet;
import resource_manager.writable.core.ConfigurationBundle;
import routine_handler.components.core.RoutineClock;
import utility_library.algorithms.api.Computable;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferStrategy;

/**
 * A class representing a display controller
 */
public abstract class DisplayController
{

    /**
     * Host container for the game window
     */
    private HostContainer hostContainer;

    /**
     * Screen container for the game canvas
     */
    private ScreenContainer screenContainer;

    /**
     * Viewport container for the viewing area on screen
     */
    private ViewportContainer viewportContainer;

    /**
     * Default constructor for creating a new display controller
     */
    protected DisplayController( )
    {
    }

    /**
     * Sets up the child of this display controller
     *
     * @param container the specified container of the screen
     */
    protected abstract void setup( ScreenContainer container );

    /**
     * Sets up the display controller and prepares it for rendering
     *
     * @param configuration      the specified configuration data required to set up the display controller
     * @param clock              the game fps clock
     * @param stopFunction       the specified function used to terminate the game on invoking the window to close
     * @param clearInputFunction the specified function used to clear input when the window is no longer in focus
     */
    public final void setup( ConfigurationBundle configuration , RoutineClock clock , Computable stopFunction , Computable clearInputFunction )
    {
        String title = generateTitle( configuration.gameInformation( ) );
        HostContainer graphicsHost = HostContainer.from( title );
        ScreenContainer graphicsScreen = ScreenContainer.from( graphicsHost , configuration.graphicsInformation( ).getResolution( ) , 2 );
        setupListeners( graphicsHost.host( ) , clock , configuration.clientInformation( ) , stopFunction , clearInputFunction );
        setupDisplay( graphicsHost , configuration.graphicsInformation( ) );
        graphicsHost.setRenderScale( configuration.graphicsInformation( ).getRenderScale( ) );
        ViewportContainer graphicsViewport = ViewportContainer.from( graphicsHost.host( ).getSize( ) , graphicsHost.getScaling( ) );
        setup( graphicsScreen );
        hostContainer = graphicsHost;
        screenContainer = graphicsScreen;
        viewportContainer = graphicsViewport;
    }

    /**
     * Generates a title from the game info to apply to the hosting frame
     *
     * @param gameInfo the specified game info containing the necessary title, subtitle and version strings
     *
     * @return a generated string containing at least the name and version of the game
     */
    private static String generateTitle( GameInformationSet gameInfo )
    {
        String name = gameInfo.name( );
        String subtitle = gameInfo.subtitle( );
        String version = gameInfo.version( );
        return subtitle != null && ! subtitle.isBlank( ) ? name + " " + version + " - " + subtitle : name + " " + version;
    }

    /**
     * Sets up the window event listeners for the hosting frame
     *
     * @param window             the specified hosting window to initialize listeners for
     * @param clock              the specified game fps clock
     * @param clientInfo         the specified client information containing the fps configuration information
     * @param stopFunction       the specified stop function to invoke when the game is terminating
     * @param clearInputFunction the specified function used to clear input when the window is no longer in focus
     */
    private static void setupListeners( Window window , RoutineClock clock , ClientInformationSet clientInfo , Computable stopFunction , Computable clearInputFunction )
    {
        window.addWindowStateListener( e -> {
            if ( e.getNewState( ) == Frame.ICONIFIED )
            {
                clock.setMinRate( );
            }
            else
            {
                clock.setRate( clientInfo.fpsModifier( ) );
            }
        } );

        window.addWindowFocusListener( new WindowFocusListener( )
        {
            @Override
            public void windowGainedFocus( WindowEvent e )
            {
                clock.setRate( clientInfo.fpsModifier( ) );
            }

            @Override
            public void windowLostFocus( WindowEvent e )
            {
                clock.setRate( 10 );
                clearInputFunction.compute( );
            }
        } );

        window.addWindowListener( new WindowAdapter( )
        {
            @Override
            public void windowClosing( WindowEvent e )
            {
                stopFunction.compute( );
            }
        } );
    }

    /**
     * Sets up the display with the specified graphics information
     *
     * @param hostContainer the specified host frame to set up
     * @param graphicsInfo  the specified graphics information used to set up the game frame
     */
    private static void setupDisplay( HostContainer hostContainer , GraphicsInformationSet graphicsInfo )
    {
        Dimension screenSize = graphicsInfo.getResolution( );
        if ( graphicsInfo.fullscreen( ) )
        {
            GraphicsDevice graphicsDevice = GraphicsEnvironment.getLocalGraphicsEnvironment( ).getDefaultScreenDevice( );
            if ( ! graphicsDevice.isFullScreenSupported( ) )
            {
                GameDebugger.log( Alert.WARN , "Display does not support fullscreen mode" );
                graphicsInfo.setFullscreen( false );
                setupDisplay( hostContainer , graphicsInfo );
                return;
            }
            graphicsDevice.setFullScreenWindow( hostContainer.host( ) );
            screenSize = new Dimension( hostContainer.host( ).getSize( ).width , hostContainer.host( ).getSize( ).height );
        }
        else
        {
            hostContainer.host( ).setResizable( false );
        }
        Dimension dimensionInset = new Dimension( screenSize.width + hostContainer.host( ).getInsets( ).left + hostContainer.host( ).getInsets( ).right , screenSize.height + hostContainer.host( ).getInsets( ).top + hostContainer.host( ).getInsets( ).bottom );
        hostContainer.host( ).setSize( dimensionInset );
        hostContainer.setResolutionScale( screenSize , graphicsInfo );
        hostContainer.host( ).pack( );
        hostContainer.host( ).setLocationRelativeTo( null );
        hostContainer.host( ).setVisible( true );
        hostContainer.host( ).requestFocus( );
    }

    /**
     * Sets the icon of the game to the one specified
     *
     * @param image the specified image the hosting frame will use as an icon
     */
    public final void setIcon( Image image )
    {
        hostContainer.host( ).setIconImage( image );
    }

    /**
     * Gets the current buffering strategy of the screen container
     *
     * @return the buffering strategy of the canvas in the screen container
     */
    protected final BufferStrategy strategy( )
    {
        return screenContainer.strategy( );
    }

    /**
     * Gets the screen object contained in this screen container
     *
     * @return a canvas for rendering images to
     */
    protected final Canvas screen( )
    {
        return screenContainer.screen( );
    }

    /**
     * Gets the calculated total scaling when rendering
     *
     * @return the product of the render and resolution scale
     */
    protected final float scaling( )
    {
        return hostContainer.getScaling( );
    }

    /**
     * Gets the current shape of the viewport and its location
     *
     * @return a copy of the viewport object in the container
     */
    protected final Rectangle2D viewport( )
    {
        return viewportContainer.viewport( );
    }

    /**
     * Gets a viewport position validator for validating the position of sprites in the viewport area
     *
     * @return an instance of a viewport container which provides viewport position validation functionality
     */
    public final ViewportPositionValidator viewportPositionValidator( )
    {
        return viewportContainer;
    }
    
    /**
     * Adjusts the current centering of the viewport to match that of the specified value
     *
     * @param x the specified value {@code x} to adjust the centering coordinates to
     * @param y the specified value {@code y} to adjust the centering coordinates to
     */
    public final void adjustCentering( double x , double y )
    {
        viewportContainer.adjustCentering( x , y );
    }

    /**
     * Updates the viewport based on th current centering
     */
    public final void updateViewport( )
    {
        viewportContainer.updateViewport( );
    }

}