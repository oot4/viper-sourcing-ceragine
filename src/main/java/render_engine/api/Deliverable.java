package render_engine.api;

import java.awt.*;
import java.awt.geom.Rectangle2D;

/**
 * A functional interface to be implemented when rendering tasks need to be executed and the output delivered to some graphics object
 */
@FunctionalInterface
public interface Deliverable
{

    /**
     * Delivers the current supply of render packets to the graphics object
     *
     * @param gfx      the specified graphics object to deliver render packets to
     * @param scale    the specified scale to apply to the graphics object
     * @param viewport the specified bounds of the viewport in to the game
     */
    void deliver( Graphics2D gfx , float scale , Rectangle2D viewport );

}