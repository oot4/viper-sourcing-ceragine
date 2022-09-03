package utility_library.gameplay.effects;

import ecs_architecture.standard.GameComponent;
import ecs_architecture.standard.GameSystem;
import utility_library.algorithms.api.Interpolator;
import utility_library.algorithms.core.Quotient;
import utility_library.gameplay.rendering.api.SpriteContainer;
import utility_library.gameplay.rendering.core.CharacterSpriteComponent;
import utility_library.gameplay.rendering.core.EnvironmentSpriteComponent;
import utility_library.gameplay.rendering.core.FloorSpriteComponent;
import utility_library.gameplay.rendering.core.OverlaySpriteComponent;
import utility_library.gameplay.rendering.core.UserInterfaceSpriteComponent;
import utility_library.structures.EffectTimer;

import java.util.Map;

public final class EffectSystem extends GameSystem
{

    private Map< String, GameComponent > floorSprites;

    private Map< String, GameComponent > environmentSprites;

    private Map< String, GameComponent > characterSprites;

    private Map< String, GameComponent > userInterfaceSprites;

    private Map< String, GameComponent > overlaySprites;

    private double getResult( boolean isReversed , double timer1 , double timer2 )
    {
        return isReversed ? timer1 : timer2;
    }

    private int processEffect( EffectTimer timer )
    {
        double result = 0;
        long duration = timer.getDuration( );
        counter( ).setMaxIfGreater( duration );
        if ( duration > 0 && counter( ).currentFrame( ) > 0 )
        {
            long elapsedStartDelay = counter( ).currentFrame( ) % duration;
            long elapsedStartTransition = elapsedStartDelay - timer.getTimer( EffectTimer.START_DELAY_PHASE );
            long elapsedStopTransition = elapsedStartTransition - timer.getTimer( EffectTimer.START_TRANSITION_PHASE );
            long elapsedStopDelay = elapsedStopTransition - timer.getTimer( EffectTimer.STOP_TRANSITION_PHASE );
            if ( timer.isActive( EffectTimer.START_DELAY_PHASE ) && elapsedStartDelay <= timer.getTimer( EffectTimer.START_DELAY_PHASE ) )
            {
                result = getResult( timer.isReversed( ) , timer.getMaximum( ) , timer.getMinimum( ) );
            }
            else if ( timer.isActive( EffectTimer.START_TRANSITION_PHASE ) && elapsedStartTransition <= timer.getTimer( EffectTimer.START_TRANSITION_PHASE ) )
            {
                double modifier = Quotient.toDouble( elapsedStartTransition , timer.getTimer( EffectTimer.START_TRANSITION_PHASE ) );
                result = getResult( timer.isReversed( ) , Interpolator.standard( ).toDouble( timer.getMaximum( ) , timer.getMinimum( ) , modifier ) , Interpolator.standard( ).toDouble( timer.getMinimum( ) , timer.getMaximum( ) , modifier ) );
            }
            else if ( timer.isActive( EffectTimer.STOP_DELAY_PHASE ) && elapsedStopTransition <= timer.getTimer( EffectTimer.STOP_DELAY_PHASE ) )
            {
                result = getResult( timer.isReversed( ) , timer.getMinimum( ) , timer.getMaximum( ) );
            }
            else if ( timer.isActive( EffectTimer.STOP_TRANSITION_PHASE ) && elapsedStopDelay <= timer.getTimer( EffectTimer.STOP_TRANSITION_PHASE ) )
            {
                double modifier = Quotient.toDouble( elapsedStopDelay , timer.getTimer( EffectTimer.STOP_TRANSITION_PHASE ) );
                result = getResult( timer.isReversed( ) , Interpolator.standard( ).toLong( timer.getMinimum( ) , timer.getMaximum( ) , modifier ) , Interpolator.standard( ).toLong( timer.getMaximum( ) , timer.getMinimum( ) , modifier ) );
            }
        }
        return ( int ) result;
    }

    private SpriteContainer getEntitySpriteContainer( String entity )
    {
        if ( overlaySprites.containsKey( entity ) ) { return ( SpriteContainer ) overlaySprites.get( entity ); }
        if ( userInterfaceSprites.containsKey( entity ) ) { return ( SpriteContainer ) userInterfaceSprites.get( entity ); }
        if ( characterSprites.containsKey( entity ) ) { return ( SpriteContainer ) characterSprites.get( entity ); }
        if ( environmentSprites.containsKey( entity ) ) { return ( SpriteContainer ) environmentSprites.get( entity ); }
        return ( SpriteContainer ) floorSprites.get( entity );
    }

    private void updateAlpha( String entity , EffectTimer timer )
    {
        SpriteContainer spriteContainer = getEntitySpriteContainer( entity );
        spriteContainer.sprite( ).setAlpha( processEffect( timer ) );
        if ( counter( ).reachedInterval( timer.getDuration( ) ) && ! timer.isLooped( ) ) { remove( entity , FadeComponent.class ); }
    }

    private void updateScale( String entity , EffectTimer timer )
    {
        SpriteContainer spriteContainer = getEntitySpriteContainer( entity );
        spriteContainer.sprite( ).setScale( processEffect( timer ) );
        if ( counter( ).reachedInterval( timer.getDuration( ) ) && ! timer.isLooped( ) ) { remove( entity , ResizeComponent.class ); }
    }

    @Override
    protected void internalInitialize( )
    {
        floorSprites = get( FloorSpriteComponent.class );
        environmentSprites = get( EnvironmentSpriteComponent.class );
        characterSprites = get( CharacterSpriteComponent.class );
        userInterfaceSprites = get( UserInterfaceSpriteComponent.class );
        overlaySprites = get( OverlaySpriteComponent.class );
    }

    @Override
    protected void internalTerminate( )
    {
        //Nothing to terminate
    }

    @Override
    protected void internalPerform( )
    {
        Map< String, GameComponent > fadeFX = get( FadeComponent.class );
        Map< String, GameComponent > resizeFX = get( ResizeComponent.class );
        fadeFX.forEach( ( entity , fadeComponent ) -> {
            FadeComponent fadeEffect = ( FadeComponent ) fadeComponent;
            updateAlpha( entity , fadeEffect.proxy( ).getTimer( ) );
        } );
        resizeFX.forEach( ( entity , resizeComponent ) -> {
            ResizeComponent resizeEffect = ( ResizeComponent ) resizeComponent;
            updateScale( entity , resizeEffect.proxy( ).getTimer( ) );
        } );
    }


}