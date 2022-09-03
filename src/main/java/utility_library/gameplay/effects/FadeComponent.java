package utility_library.gameplay.effects;

import ecs_architecture.standard.GameComponent;

public final class FadeComponent extends GameComponent
{

    private final EffectTimerProxy proxy;

    private FadeComponent( EffectTimerProxy proxy )
    {
        this.proxy = proxy;
    }

    public static FadeComponent from( long minimum , long maximum , boolean loop , boolean reverse )
    {
        EffectTimerProxy proxy = EffectTimerProxy.from( 0 , 255 , minimum , maximum , loop , reverse );
        return new FadeComponent( proxy );
    }

    EffectTimerProxy proxy( )
    {
        return proxy;
    }

    public void setTimer( int phase , long value )
    {
        proxy.setTimer( phase , value );
    }

    public void setAllTimers( long startDelayTimer , long startTransitionTimer , long stopTransitionTimer , long stopDelayTimer )
    {
        proxy.setAllTimers( startDelayTimer , startTransitionTimer , stopTransitionTimer , stopDelayTimer );
    }

    public void deactivate( int phase )
    {
        proxy.deactivate( phase );
    }

    public void setLooped( boolean loop )
    {
        proxy.setLooped( loop );
    }

    public void setReversed( boolean reverse )
    {
        proxy.setReversed( reverse );
    }

}