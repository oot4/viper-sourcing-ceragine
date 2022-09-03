/*
 * Copyright (c) 2022. The Ceragine project files, related assets and related source code
 * were created by, Ogesoka (Oge) Tasie and should not be copied or distributed without
 * consent of the creator (Ogesoka Tasie). All Rights Reserved.
 */

package resource_manager.writable.api;

/**
 * An interface to be implemented by a container of game specific information
 */
public interface GameInformationSet
{

    /**
     * Gets the organization name
     *
     * @return the organization that created the game
     */
    String organization( );

    /**
     * Sets the organization name
     *
     * @param organization the specified organization name to set
     */
    void setOrganization( String organization );

    /**
     * Gets the name of the game
     *
     * @return the game name
     */
    String name( );

    /**
     * Sets the game name to the one specified
     *
     * @param name the specified name of the game
     */
    void setName( String name );

    /**
     * Gets the subtitle of the game
     *
     * @return the subtitle of the game
     */
    String subtitle( );

    /**
     * Sets the subtitle of the game
     *
     * @param subtitle the specified subtitle of the game
     */
    void setSubtitle( String subtitle );

    /**
     * Gets the contextual description of the game
     *
     * @return the game description that provides context to the game
     */
    String description( );

    /**
     * Sets the game description
     *
     * @param description the specified game description to set
     */
    void setDescription( String description );

    /**
     * The current version of the game
     *
     * @return the game version
     */
    String version( );

    /**
     * Sets the current version of the game
     *
     * @param version the specified version to set
     */
    void setVersion( String version );

    /**
     * Gets the game project webpage URL
     *
     * @return the website address of game project webpage
     */
    String website( );

    /**
     * Sets the game project webpage URL
     *
     * @param website the specified web address to set
     */
    void setWebsite( String website );

    /**
     * Gets the developers assigned to the game project
     *
     * @return the game project developers
     */
    String[] developers( );

    /**
     * Sets the developers assigned to the game project
     *
     * @param developers the specified developers assigned to the game project
     */
    void setDevelopers( String... developers );

}