package render_engine.core;

import resource_manager.writable.api.GraphicsInformationSet;
import utility_library.algorithms.core.Clamp;

import javax.swing.*;
import java.awt.*;

/**
 * A class representing a screen host
 */
public final class HostContainer
{

    /**
     * Current resolution scale
     */
    private float resolutionScale = 1.0F;

    /**
     * Base render scale
     */
    private float renderScale = 1.0F;

    /**
     * Window frame
     */
    private final JFrame host;

    /**
     * Constructor for creating a new {@code Host}
     *
     * @param host the specified frame to the host will house
     */
    private HostContainer( JFrame host )
    {
        this.host = host;
    }

    /**
     * Creates a new screen host with the specified title
     *
     * @param title the specified title to give the host
     *
     * @return a new instance of a {@code Host} object
     */
    public static HostContainer from( String title )
    {
        JFrame graphicsHost = new JFrame( );
        graphicsHost.setBackground( Color.BLACK );
        graphicsHost.setTitle( title );
        graphicsHost.setDefaultCloseOperation( WindowConstants.DO_NOTHING_ON_CLOSE );
        return new HostContainer( graphicsHost );
    }

    /**
     * Gets the hosting frame
     *
     * @return a component host as a {@code JFrame}
     */
    public JFrame host( )
    {
        return host;
    }

    /**
     * Gets the current resolution scale
     *
     * @return a float value indicating resolution scale
     */
    public float getResolutionScale( )
    {
        return resolutionScale;
    }

    /**
     * Sets the current resolution scale
     *
     * @param resolution   the specified resolution used to determine the new resolution scale
     * @param graphicsInfo the specified graphics information containing resolution scaling configuration
     */
    public void setResolutionScale( Dimension resolution , GraphicsInformationSet graphicsInfo )
    {
        float scale = 1.0F;
        if ( graphicsInfo.resolutionScaling( ) )
        {
            scale = ( float ) ( resolution.getWidth( ) / 1920.0F );
        }
        resolutionScale = scale;
    }

    /**
     * Gets the base render scale applied to all display routines
     *
     * @return a float value indicating base scale for rendering
     */
    public float getRenderScale( )
    {
        return renderScale;
    }

    /**
     * Sets the base render scale applied to all display routines
     *
     * @param renderScale te specified float value to set the base render scale to
     */
    public void setRenderScale( float renderScale )
    {
        this.renderScale = Clamp.toFloat( renderScale , 1.0F , 4.0F );
    }

    /**
     * Gets the calculated total scaling when rendering
     *
     * @return the product of the render and resolution scale
     */
    public float getScaling( )
    {
        return getRenderScale( ) * getResolutionScale( );
    }

}