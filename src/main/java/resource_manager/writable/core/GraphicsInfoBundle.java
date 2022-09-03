/*
 * Copyright (c) 2022. The Ceragine project files, related assets and related source code
 * were created by, Ogesoka (Oge) Tasie and should not be copied or distributed without
 * consent of the creator (Ogesoka Tasie). All Rights Reserved.
 */

package resource_manager.writable.core;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSetter;
import resource_manager.writable.api.GraphicsInformationSet;

import java.awt.*;

/**
 * A class representing a container for graphics specific information
 */
public final class GraphicsInfoBundle implements GraphicsInformationSet
{

    /**
     * Pixel width on screen
     */
    private int width;

    /**
     * Pixel height on screen
     */
    private int height;

    /**
     * Base scaling applied to rendering
     */
    private float renderScaling;

    /**
     * Is the application running in fullscreen mode?
     */
    private boolean fullscreen;

    /**
     * Is resolution scaling enabled?
     */
    private boolean resolutionScaling;

    /**
     * Is anti-aliasing enabled?
     */
    private boolean antiAliasing;

    /**
     * Is color interpolation enabled?
     */
    private boolean colorInterpolation;

    /**
     * Creates a new instance of a {@code GraphicsInfoBundle}
     */
    private GraphicsInfoBundle( ) { }

    /**
     * Creates a new instance of a {@code GraphicsInfoBundle}
     *
     * @return a new instance of a {@link GraphicsInfoBundle graphics information bundle}
     */
    public static GraphicsInfoBundle standard( )
    {
        GraphicsInfoBundle standard = new GraphicsInfoBundle( );
        Dimension dimension = Toolkit.getDefaultToolkit( ).getScreenSize( );
        int width = dimension.width / 2;
        int height = ( dimension.height - 100 ) / 2;
        standard.setResolutionWidth( width );
        standard.setResolutionHeight( height );
        standard.setRenderScale( 2.0F );
        standard.setFullscreen( false );
        standard.enableResolutionScaling( false );
        standard.enableAntiAliasing( false );
        standard.enableColorInterpolation( false );
        return standard;
    }

    /**
     * {@inheritDoc}
     */
    @JsonGetter( "width" )
    @Override
    public int getResolutionWidth( )
    {
        return width;
    }

    /**
     * {@inheritDoc}
     */
    @JsonSetter( "width" )
    @Override
    public void setResolutionWidth( int width )
    {
        if ( width % 8 != 0 )
        {
            width -= width % 8;
        }
        this.width = width;
    }

    /**
     * {@inheritDoc}
     */
    @JsonGetter( "height" )
    @Override
    public int getResolutionHeight( )
    {
        return height;
    }

    /**
     * {@inheritDoc}
     */
    @JsonSetter( "height" )
    @Override
    public void setResolutionHeight( int height )
    {
        if ( height % 8 != 0 )
        {
            height -= height % 8;
        }
        this.height = height;
    }

    /**
     * {@inheritDoc}
     */
    @JsonIgnore
    @Override
    public Dimension getResolution( )
    {
        if ( this.fullscreen )
        {
            GraphicsDevice graphicsDevice = GraphicsEnvironment.getLocalGraphicsEnvironment( ).getDefaultScreenDevice( );
            int graphicsDeviceWidth = graphicsDevice.getDisplayMode( ).getWidth( );
            int graphicsDeviceHeight = graphicsDevice.getDisplayMode( ).getHeight( );
            return new Dimension( graphicsDeviceWidth , graphicsDeviceHeight );
        }
        return new Dimension( this.width , this.height );
    }

    /**
     * {@inheritDoc}
     */
    @JsonGetter( "baseRenderScale" )
    @Override
    public float getRenderScale( )
    {
        return renderScaling;
    }

    /**
     * {@inheritDoc}
     */
    @JsonSetter( "baseRenderScale" )
    @Override
    public void setRenderScale( float renderScale )
    {
        this.renderScaling = renderScale;
    }

    /**
     * {@inheritDoc}
     */
    @JsonGetter( "fullscreen" )
    @Override
    public boolean fullscreen( )
    {
        return fullscreen;
    }

    /**
     * {@inheritDoc}
     */
    @JsonSetter( "fullscreen" )
    @Override
    public void setFullscreen( boolean fullscreen )
    {
        this.fullscreen = fullscreen;
    }

    /**
     * {@inheritDoc}
     */
    @JsonGetter( "resolutionScaling" )
    @Override
    public boolean resolutionScaling( )
    {
        return resolutionScaling;
    }

    /**
     * {@inheritDoc}
     */
    @JsonSetter( "resolutionScaling" )
    @Override
    public void enableResolutionScaling( boolean resolutionScaling )
    {
        this.resolutionScaling = resolutionScaling;
    }

    /**
     * {@inheritDoc}
     */
    @JsonGetter( "antiAliasing" )
    @Override
    public boolean antiAliasing( )
    {
        return antiAliasing;
    }

    /**
     * {@inheritDoc}
     */
    @JsonSetter( "antiAliasing" )
    @Override
    public void enableAntiAliasing( boolean antiAliasing )
    {
        this.antiAliasing = antiAliasing;
    }

    /**
     * {@inheritDoc}
     */
    @JsonGetter( "colorInterpolation" )
    @Override
    public boolean colorInterpolation( )
    {
        return colorInterpolation;
    }

    /**
     * {@inheritDoc}
     */
    @JsonSetter( "colorInterpolation" )
    @Override
    public void enableColorInterpolation( boolean colorInterpolation )
    {
        this.colorInterpolation = colorInterpolation;
    }

}