package render_engine.api;

import java.awt.*;
import java.awt.geom.Point2D;

/**
 * An interface to be implemented when an intersection between a sprite shape and viewport needs to be tested or, when a provided coordinate and image size needs to be converted to a position in the viewport
 */
public interface ViewportPositionValidator
{

    /**
     * Gets the x and y coordinates adjusted to the viewport
     *
     * @param offset the specified offset value used to adjust the x and y values
     * @param x      the specified x value to adjust
     * @param y      the specified y value to adjust
     * @param size   the specified size of the image to fit within the viewport
     *
     * @return a new point adjusted to the viewport area (Orthogonal points)
     */
    Point2D getViewportPosition( Point offset , double x , double y , int size );

    /**
     * Determines whether the specifies whether the specified sprite shape intersects the viewport area
     *
     * @param offset the specified offset value used to adjust the x and y values
     * @param x      the specified x value to test
     * @param y      the specified y value to test
     * @param size   the specified size of the image expected to intersect the viewport area
     *
     * @return {@code True} if the sprite shape intersects the viewport area. {@code False} otherwise
     */
    boolean intersects( Point offset , double x , double y , int size );

}