/*
 * Copyright (c) 2022. The Ceragine project files, related assets and related source code
 * were created by, Ogesoka (Oge) Tasie and should not be copied or distributed without
 * consent of the creator (Ogesoka Tasie). All Rights Reserved.
 */

package resource_manager.writable.core;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;
import resource_manager.writable.api.ClientInformationSet;

/**
 * A class representing a container for client specific information
 */
public final class ClientInfoBundle implements ClientInformationSet
{

    /**
     * Modifier for frames per second
     */
    private double fpsModifier;

    /**
     * Creates a new instance of a {@code ClientInfoBundle}
     */
    private ClientInfoBundle( ) { }

    /**
     * Creates a new instance of a {@code ClientInfoBundle} with default values
     *
     * @return a new instance of a {@link ClientInfoBundle client information bundle}
     */
    public static ClientInfoBundle standard( )
    {
        ClientInfoBundle standard = new ClientInfoBundle( );
        standard.setFpsModifier( 60.0D );
        return standard;
    }

    /**
     * {@inheritDoc}
     */
    @JsonGetter( "fpsModifier" )
    @Override
    public double fpsModifier( )
    {
        return fpsModifier;
    }

    /**
     * {@inheritDoc}
     */
    @JsonSetter( "fpsModifier" )
    @Override
    public void setFpsModifier( double fpsModifier )
    {
        this.fpsModifier = fpsModifier;
    }

}