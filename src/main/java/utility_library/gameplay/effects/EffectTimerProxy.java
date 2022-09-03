package utility_library.gameplay.effects;

import utility_library.algorithms.core.Clamp;
import utility_library.structures.EffectTimer;

final class EffectTimerProxy
{

    private final EffectTimer timer;

    private EffectTimerProxy( EffectTimer timer )
    {
        this.timer = timer;
    }

    public static EffectTimerProxy from( long lowerBound , long upperBound , long minimum , long maximum , boolean loop , boolean reverse )
    {
        long clampedMin = Clamp.toLong( minimum , lowerBound , upperBound );
        long clampedMax = Clamp.toLong( maximum , lowerBound , upperBound );
        EffectTimer timer = EffectTimer.from( clampedMin , clampedMax );
        timer.setLooped( loop );
        timer.setReversed( reverse );
        timer.setAllTimers( 0 , 0 , 0 , 0 );
        return new EffectTimerProxy( timer );
    }

    EffectTimer getTimer( )
    {
        return timer;
    }

    public void setTimer( int phase , long value )
    {
        timer.setTimer( phase , value );
    }

    public void setAllTimers( long startDelayTimer , long startTransitionTimer , long stopTransitionTimer , long stopDelayTimer )
    {
        timer.setAllTimers( startDelayTimer , startTransitionTimer , stopTransitionTimer , stopDelayTimer );
    }

    public void deactivate( int phase )
    {
        timer.deactivate( phase );
    }

    public void setLooped( boolean loop )
    {
        timer.setLooped( loop );
    }

    public void setReversed( boolean reverse )
    {
        timer.setReversed( reverse );
    }

}