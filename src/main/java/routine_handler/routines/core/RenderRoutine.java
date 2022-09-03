package routine_handler.routines.core;

import render_engine.standard.RenderLayerController;
import routine_handler.components.api.RenderListener;

import java.awt.*;

/**
 * A class representing a routine for preparing graphics before pushing them to the screen
 */
public final class RenderRoutine extends RenderLayerController implements RenderListener
{

    /**
     * Constructor for creating a new {@code RenderRoutine}
     *
     * @param offset the specified offset to apply to the render routine
     */
    private RenderRoutine( Point offset )
    {
        super( offset );
    }

    /**
     * Creates a new instance of a {@code RenderRoutine} with the specified configuration settings
     *
     * @return a new instance of a render routine listener
     */
    public static RenderRoutine from( )
    {
        return new RenderRoutine( new Point( 0 , 0 ) );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void render( double interpolation )
    {
        compute( );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void perform( )
    {
        clear( );
    }

}