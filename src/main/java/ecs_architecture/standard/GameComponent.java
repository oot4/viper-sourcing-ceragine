package ecs_architecture.standard;

/**
 * An abstract class to be extended by a component for a game
 */
public abstract class GameComponent
{

    /**
     * Component name
     */
    private String name = "";

    /**
     * Default constructor for creating a new {@code GameComponent}
     */
    protected GameComponent( )
    {
    }

    /**
     * Default constructor for creating a new {@code GameComponent} with the specified name
     *
     * @param name the specified name to give to the game component
     */
    protected GameComponent( String name )
    {
        this.name = name;
    }

    /**
     * Gets the name of the clock component
     *
     * @return the string value corresponding to the component name
     */
    public final String getName( )
    {
        return name;
    }

}