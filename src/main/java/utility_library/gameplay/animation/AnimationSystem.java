package utility_library.gameplay.animation;

import ecs_architecture.standard.GameComponent;
import ecs_architecture.standard.GameSystem;
import utility_library.gameplay.rendering.api.SpriteContainer;
import utility_library.gameplay.rendering.core.CharacterSpriteComponent;
import utility_library.gameplay.rendering.core.EnvironmentSpriteComponent;
import utility_library.gameplay.rendering.core.FloorSpriteComponent;
import utility_library.gameplay.rendering.core.OverlaySpriteComponent;
import utility_library.gameplay.rendering.core.UserInterfaceSpriteComponent;

import java.util.Map;
import java.util.Objects;

public final class AnimationSystem extends GameSystem
{

    private static int getNewIndex( AnimationComponent animation )
    {
        if ( animation.isChangingAnimation( ) )
        {
            animation.changeAnimation( );
            return 0;
        }
        else
        {
            return animation.isLoopedAnimation( ) && ! animation.stopLoop( ) ? 0 : animation.size( ) - 1;
        }
    }

    private SpriteContainer getSprite( String entity )
    {
        if ( get( OverlaySpriteComponent.class ).containsKey( entity ) )
        {
            return ( ( SpriteContainer ) get( OverlaySpriteComponent.class ).get( entity ) );
        }
        if ( get( UserInterfaceSpriteComponent.class ).containsKey( entity ) )
        {
            return ( ( SpriteContainer ) get( UserInterfaceSpriteComponent.class ).get( entity ) );
        }
        if ( get( CharacterSpriteComponent.class ).containsKey( entity ) )
        {
            return ( ( SpriteContainer ) get( CharacterSpriteComponent.class ).get( entity ) );
        }
        if ( get( EnvironmentSpriteComponent.class ).containsKey( entity ) )
        {
            return ( ( SpriteContainer ) get( EnvironmentSpriteComponent.class ).get( entity ) );
        }
        if ( get( FloorSpriteComponent.class ).containsKey( entity ) )
        {
            return ( ( SpriteContainer ) get( FloorSpriteComponent.class ).get( entity ) );
        }
        return null;
    }

    @Override
    protected void internalInitialize( )
    {
        //Nothing to initialize
    }

    @Override
    protected void internalTerminate( )
    {
        //Nothing to terminate
    }

    @Override
    protected void internalPerform( )
    {
        Map< String, GameComponent > animations = get( AnimationComponent.class );
        animations.forEach( ( entity , gameComponent ) -> {
            AnimationComponent animation = ( AnimationComponent ) gameComponent;
            SpriteContainer spriteContainer = getSprite( entity );
            if ( ! animation.isEmpty( ) && spriteContainer != null && counter( ).reachedInterval( animation.interval( ) ) )
            {
                int index = animation.index( );
                spriteContainer.sprite( ).replaceTiles( Objects.requireNonNull( animation.sprite( ) ).tiles( ) );
                spriteContainer.sprite( ).replaceMirrored( Objects.requireNonNull( animation.sprite( ) ).mirrored( ) );
                spriteContainer.sprite( ).offset( ).setLocation( Objects.requireNonNull( animation.sprite( ) ).offset( ) );
                spriteContainer.sprite( ).setAlpha( Objects.requireNonNull( animation.sprite( ) ).alpha( ) );
                spriteContainer.sprite( ).setScale( Objects.requireNonNull( animation.sprite( ) ).scale( ) );
                if ( animation.index( ) >= animation.size( ) - 1 )
                {
                    index = getNewIndex( animation );
                }
                else
                {
                    index++;
                }
                animation.setIndex( index );
            }
        } );
    }


}