/*
 * Copyright (c) 2022. The Ceragine project files, related assets and related source code
 * were created by, Ogesoka (Oge) Tasie and should not be copied or distributed without
 * consent of the creator (Ogesoka Tasie). All Rights Reserved.
 */

package resource_manager.writable.core;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;
import resource_manager.writable.api.GameInformationSet;

/**
 * A class representing a container for game or project specific information
 */
public final class GameInfoBundle implements GameInformationSet
{

    /**
     * The organization name
     */
    private String organization;

    /**
     * Game or project name
     */
    private String name;

    /**
     * Game or project subtitle
     */
    private String subtitle;

    /**
     * Game or project description
     */
    private String description;

    /**
     * Game or project version
     */
    private String version;

    /**
     * URL to project or game web page
     */
    private String website;

    /**
     * Name of the developers of the game or project
     */
    private String[] developers;

    /**
     * Creates a new instance of a {@code GameInfoBundle}
     */
    private GameInfoBundle( ) { }

    /**
     * Creates a new instance of a {@code GameInfoBundle}
     *
     * @return a new instance of a {@link GameInfoBundle game information bundle}
     */
    public static GameInfoBundle standard( )
    {
        GameInfoBundle standard = new GameInfoBundle( );
        standard.setOrganization( "Viper Sourcing" );
        standard.setName( "Ceragine" );
        standard.setSubtitle( "A true entity-component-system architecture" );
        standard.setDescription( "A game engine that provides an entity-component-system to make building a game simpler" );
        standard.setVersion( "0.4.0" );
        standard.setWebsite( "https://github.com/ViperSourcing" );
        standard.setDevelopers( "Ogesoka Tasie" );
        return standard;
    }

    /**
     * {@inheritDoc}
     */
    @JsonGetter( "organization" )
    @Override
    public String organization( )
    {
        return organization;
    }

    /**
     * {@inheritDoc}
     */
    @JsonSetter( "organization" )
    @Override
    public void setOrganization( String organization )
    {
        this.organization = organization;
    }

    /**
     * {@inheritDoc}
     */
    @JsonGetter( "name" )
    @Override
    public String name( )
    {
        return name;
    }

    /**
     * {@inheritDoc}
     */
    @JsonSetter( "name" )
    @Override
    public void setName( String name )
    {
        this.name = name;
    }

    /**
     * {@inheritDoc}
     */
    @JsonGetter( "subtitle" )
    @Override
    public String subtitle( )
    {
        return subtitle;
    }

    /**
     * {@inheritDoc}
     */
    @JsonSetter( "subtitle" )
    @Override
    public void setSubtitle( String subtitle )
    {
        this.subtitle = subtitle;
    }

    /**
     * {@inheritDoc}
     */
    @JsonGetter( "description" )
    @Override
    public String description( )
    {
        return description;
    }

    /**
     * {@inheritDoc}
     */
    @JsonSetter( "description" )
    @Override
    public void setDescription( String description )
    {
        this.description = description;
    }

    /**
     * {@inheritDoc}
     */
    @JsonGetter( "version" )
    @Override
    public String version( )
    {
        return version;
    }

    /**
     * {@inheritDoc}
     */
    @JsonSetter( "version" )
    @Override
    public void setVersion( String version )
    {
        String currentVersion = version.isBlank( ) ? "1.0.0" : version;
        if ( ! currentVersion.matches( "^(\\d*)\\.(\\d*)\\.(\\d*)$" ) )
        {
            throw new IllegalArgumentException( "Versioning does not follow the regular expression: '^(\\\\d*)\\\\.(\\\\d*)\\\\.(\\\\d*)$'" );
        }
        this.version = version;
    }

    /**
     * {@inheritDoc}
     */
    @JsonGetter( "website" )
    @Override
    public String website( )
    {
        return website;
    }

    /**
     * {@inheritDoc}
     */
    @JsonSetter( "website" )
    @Override
    public void setWebsite( String website )
    {
        this.website = website;
    }

    /**
     * {@inheritDoc}
     */
    @JsonGetter( "developers" )
    @Override
    public String[] developers( )
    {
        return developers;
    }

    /**
     * {@inheritDoc}
     */
    @JsonSetter( "developers" )
    @Override
    public void setDevelopers( String... developers )
    {
        this.developers = developers;
    }

}