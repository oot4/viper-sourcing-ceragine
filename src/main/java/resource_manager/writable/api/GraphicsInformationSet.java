/*
 * Copyright (c) 2022. The Ceragine project files, related assets and related source code
 * were created by, Ogesoka (Oge) Tasie and should not be copied or distributed without
 * consent of the creator (Ogesoka Tasie). All Rights Reserved.
 */

package resource_manager.writable.api;

import java.awt.*;

/**
 * An interface to be implemented by a container of graphics specific information
 */
public interface GraphicsInformationSet
{

    /**
     * Gets the current resolution width of the viewport
     *
     * @return the width value of the viewport
     */
    int getResolutionWidth( );

    /**
     * Sets the current width of the viewport
     *
     * @param width the specified value to set the viewport width to
     */
    void setResolutionWidth( int width );

    /**
     * Gets the current resolution height of the viewport
     *
     * @return the height value of the viewport
     */
    int getResolutionHeight( );

    /**
     * Sets the current height of the viewport
     *
     * @param height the specified value to set the viewport height to
     */
    void setResolutionHeight( int height );

    /**
     * Gets the current resolution of the viewport
     *
     * @return the resolution of the viewport as a {@code Dimension}
     */
    Dimension getResolution( );

    /**
     * Gets the base scale for rendering to the screen
     *
     * @return a float value indicating render scaling
     */
    float getRenderScale( );

    /**
     * Sets the base scale for rendering to the screen
     *
     * @param renderScale the specified float value to set as render scaling
     */
    void setRenderScale( float renderScale );

    /**
     * Is the current display mode in fullscreen?
     *
     * @return {@code True} if the current display mode is fullscreen. {@code False} otherwise
     */
    boolean fullscreen( );

    /**
     * Sets the fullscreen flag to {@code True} or {@code False}
     *
     * @param fullscreen is the current display mode in fullscreen?
     */
    void setFullscreen( boolean fullscreen );

    /**
     * Is resolution scaling active?
     *
     * @return {@code True} if resolution scaling is active. {@code False} otherwise
     */
    boolean resolutionScaling( );

    /**
     * Sets the resolution scaling to enabled or disabled
     *
     * @param resolutionScaling is resolution scaling active?
     */
    void enableResolutionScaling( boolean resolutionScaling );

    /**
     * Is anti-aliasing active?
     *
     * @return {@code True} if anti aliasing is active. {@code False} otherwise
     */
    boolean antiAliasing( );

    /**
     * Sets the anti-aliasing to enabled or disabled
     *
     * @param antiAliasing is anti aliasing active?
     */
    void enableAntiAliasing( boolean antiAliasing );

    /**
     * Is color interpolation active?
     *
     * @return {@code True} if color interpolation is active. {@code False} otherwise
     */
    boolean colorInterpolation( );

    /**
     * Sets the color interpolation to enabled or disabled
     *
     * @param colorInterpolation is color interpolation active?
     */
    void enableColorInterpolation( boolean colorInterpolation );

}