/*
 * Copyright (c) 2022. The Ceragine project files, related assets and related source code
 * were created by, Ogesoka (Oge) Tasie and should not be copied or distributed without
 * consent of the creator (Ogesoka Tasie). All Rights Reserved.
 */

package resource_manager.writable.standard;

import resource_manager.writable.api.WritableResource;
import resource_manager.writable.core.Spritesheet;

import java.util.ArrayList;
import java.util.List;

/**
 * An abstract class to be extended by a container of constant data
 */
@WritableResource
public abstract class DataBundle extends WritableBundle
{

    /**
     * Spritesheet list
     */
    private List< Spritesheet > spritesheets;

    /**
     * Default constructor for creating a new instance of a {@code DataBundle}
     */
    protected DataBundle( )
    {
        spritesheets = new ArrayList<>( );
    }

    /**
     * Gets the spritesheets from the data bundle
     *
     * @return a list of spritesheets contained in the data bundle
     */
    public final List< Spritesheet > getSpritesheets( )
    {
        return spritesheets;
    }

    /**
     * Sets the spritesheets in the data bundle to the specified list
     *
     * @param spritesheets the specified list of spritesheets to put in the data bundle
     */
    protected final void setSpritesheets( List< Spritesheet > spritesheets )
    {
        this.spritesheets = spritesheets;
    }

}