/*
 * Copyright (c) 2022. The Ceragine project files, related assets and related source code
 * were created by, Ogesoka (Oge) Tasie and should not be copied or distributed without
 * consent of the creator (Ogesoka Tasie). All Rights Reserved.
 */

package resource_manager.writable.core;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;
import resource_manager.writable.api.DebugInformationSet;

/**
 * A class representing a container for debug specific information
 */
public final class DebugInfoBundle implements DebugInformationSet
{

    /**
     * Is debug mode enabled?
     */
    private boolean debugMode;

    /**
     * Rate at which debug information is displayed
     */
    private int debugRate;

    /**
     * Creates a new instance of a {@code DebugInfoBundle}
     */
    private DebugInfoBundle( ) { }

    /**
     * Creates a new instance of a {@code DebugInfoBundle}
     *
     * @return a new instance of a {@link DebugInfoBundle debug information bundle}
     */
    public static DebugInfoBundle standard( )
    {
        DebugInfoBundle standard = new DebugInfoBundle( );
        standard.enableDebugMode( true );
        standard.setDebugRate( 360 );
        return standard;
    }

    /**
     * {@inheritDoc}
     */
    @JsonGetter( "debugMode" )
    @Override
    public boolean debugMode( )
    {
        return debugMode;
    }

    /**
     * {@inheritDoc}
     */
    @JsonSetter( "debugMode" )
    @Override
    public void enableDebugMode( boolean debugMode )
    {
        this.debugMode = debugMode;
    }

    /**
     * {@inheritDoc}
     */
    @JsonGetter
    @Override
    public int debugRate( )
    {
        return debugRate;
    }

    /**
     * {@inheritDoc}
     */
    @JsonSetter
    @Override
    public void setDebugRate( int debugRate )
    {
        this.debugRate = debugRate;
    }

}