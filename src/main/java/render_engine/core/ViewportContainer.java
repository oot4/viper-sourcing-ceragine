package render_engine.core;

import render_engine.api.ViewportPositionValidator;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

/**
 * A class representing a container for the viewport
 */
public final class ViewportContainer implements ViewportPositionValidator
{

    /**
     * Maximum width of the viewport
     */
    private final double width;

    /**
     * Maximum height of the viewport
     */
    private final double height;

    /**
     * Location where the viewport is centered
     */
    private final Point2D centering;

    /**
     * Shape of the viewport
     */
    private final Rectangle2D viewport;

    /**
     * Constructor for creating a new instance of a {@code ViewportContainer}
     *
     * @param width  the specified width of the viewport area
     * @param height the specified height of the viewport area
     */
    private ViewportContainer( double width , double height )
    {
        this.width = width;
        this.height = height;
        this.centering = new Point2D.Double( );
        viewport = new Rectangle2D.Double( );
    }

    /**
     * Creates a new instance of a {@code ViewportContainer}
     *
     * @param resolution the specified resolution of the active host and screen
     * @param scale      the specified configured value for scaling rendered output
     *
     * @return an instance of a container for the viewport
     */
    public static ViewportContainer from( Dimension resolution , float scale )
    {
        double width = resolution.getWidth( ) / scale;
        double height = resolution.getHeight( ) / scale;
        return new ViewportContainer( width , height );
    }

    /**
     * Gets the current shape of the viewport and its location
     *
     * @return a copy of the viewport object in the container
     */
    public Rectangle2D viewport( )
    {
        return new Rectangle2D.Double( viewport.getX( ) , viewport.getY( ) , viewport.getWidth( ) , viewport.getHeight( ) );
    }

    /**
     * Validates the provided centering position
     *
     * @param centering the specified position to validate
     *
     * @return {@code True} if the specified centering position contains integers and is not an infinite value.{@code False} otherwise
     */
    private boolean validateCenteringY( Point2D centering )
    {
        double value = ( centering.getY( ) - Math.floor( centering.getY( ) ) ) * 4.0D;
        return value == Math.floor( value ) && ! Double.isInfinite( value );
    }

    /**
     * Adjusts the current centering of the viewport to match that of the specified value
     *
     * @param centering the specified value to center the viewport on
     */
    private void adjustCentering( Point2D centering )
    {
        if ( validateCenteringY( centering ) )
        {
            this.centering.setLocation( centering.getX( ) , centering.getY( ) + 0.01D );
        }
    }

    /**
     * Adjusts the current centering of the viewport to match that of the specified value
     *
     * @param x the specified value {@code x} to adjust the centering coordinates to
     * @param y the specified value {@code y} to adjust the centering coordinates to
     */
    public void adjustCentering( double x , double y )
    {
        adjustCentering( new Point2D.Double( x , y ) );
    }

    /**
     * Updates the viewport based on th current centering
     */
    public void updateViewport( )
    {
        double x = centering.getX( ) - ( width * 0.5D );
        double y = centering.getY( ) - ( height * 0.5D );
        viewport.setFrame( x , y , width , height );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Point2D getViewportPosition( Point offset , double x , double y , int size )
    {
        double adjustedY = y - size + offset.y;
        double adjustedX = x + offset.x;
        return new Point2D.Double( adjustedX , adjustedY );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean intersects( Point offset , double x , double y , int size )
    {
        Point2D point = getViewportPosition( offset , x , y , size );
        return viewport.intersects( point.getX( ) , point.getY( ) , size , size );
    }

}