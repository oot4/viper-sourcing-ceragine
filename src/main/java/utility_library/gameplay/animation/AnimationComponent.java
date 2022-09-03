package utility_library.gameplay.animation;

import ecs_architecture.standard.GameComponent;
import utility_library.gameplay.rendering.api.SpriteContainer;
import utility_library.structures.Animation;
import utility_library.structures.Sprite;

import java.util.HashMap;

public final class AnimationComponent extends GameComponent implements SpriteContainer
{

    private int index;

    private int interval;

    private String currentAnimation;

    private String nextAnimation;

    private boolean changingAnimation;

    private boolean loop;

    private boolean stopLoop;

    private final HashMap< String, Animation > animations = new HashMap<>( );

    private AnimationComponent( )
    {
        index = 0;
        interval = 15;
        currentAnimation = "";
        nextAnimation = "";
        changingAnimation = false;
        loop = false;
        stopLoop = false;
    }

    public static AnimationComponent newInstance( )
    {
        return new AnimationComponent( );
    }

    public boolean addAnimation( String animationName , Animation animation )
    {
        Animation addedAnimation = animations.putIfAbsent( animationName , animation );
        return addedAnimation != null;
    }

    public boolean removeAnimation( String animationName )
    {
        return animations.remove( animationName ) != null;
    }

    public boolean replaceAnimation( String animationName , Animation animation )
    {
        return animations.replace( animationName , animation ) != null;
    }

    int index( )
    {
        return index;
    }

    void setIndex( int index )
    {
        this.index = index;
    }

    int interval( )
    {
        return interval;
    }

    public void setInterval( int interval )
    {
        this.interval = interval;
    }

    public void setCurrentAnimation( String animationName )
    {
        this.currentAnimation = animationName;
    }

    public void setNextAnimation( String animationName )
    {
        nextAnimation = animationName;
        changingAnimation = true;
    }

    void changeAnimation( )
    {
        currentAnimation = nextAnimation;
        this.changingAnimation = false;
    }

    boolean isChangingAnimation( )
    {
        return changingAnimation;
    }

    boolean isLoopedAnimation( )
    {
        Animation animation = animations.getOrDefault( currentAnimation , null );
        if ( animation != null )
        {
            return loop;
        }
        return false;
    }

    public void setAsLoopedAnimation( boolean loop )
    {
        this.loop = loop;
    }

    boolean stopLoop( )
    {
        return stopLoop;
    }

    public void toggleLoop( )
    {
        stopLoop = ! stopLoop;
    }

    public boolean isEmpty( )
    {
        return animations.isEmpty( ) || currentAnimation.isBlank( ) || size( ) <= 0 || sprite( ) == null;
    }

    int size( )
    {
        Animation animation = animations.getOrDefault( currentAnimation , null );
        if ( animation != null )
        {
            return animation.size( );
        }
        return 0;
    }

    @Override
    public Sprite sprite( )
    {
        Animation animation = animations.getOrDefault( currentAnimation , null );
        if ( animation != null )
        {
            return animation.sprite( index );
        }
        return null;
    }

}