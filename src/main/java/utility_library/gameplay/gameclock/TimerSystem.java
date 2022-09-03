/*
 * Copyright (c) 2022. The Ceragine project files, related assets and related source code
 * were created by, Ogesoka (Oge) Tasie and should not be copied or distributed without
 * consent of the creator (Ogesoka Tasie). All Rights Reserved.
 */

package utility_library.gameplay.gameclock;

import ecs_architecture.standard.GameComponent;
import ecs_architecture.standard.GameSystem;

import java.util.Map;

public final class TimerSystem extends GameSystem
{

    private void updateTimers( )
    {
        Map< String, GameComponent > clocks = get( ClockComponent.class );
        Map< String, GameComponent > timers = get( TimerComponent.class );
        clocks.forEach( ( entity0 , gameComponent0 ) -> {
            ClockComponent clock = ( ClockComponent ) gameComponent0;
            if ( clock.getName( ).equals( "|clock_name=>6yHwJKGe0-ehHQlkAPyh_XDZ|" ) )
            {
                timers.forEach( ( entity1 , gameComponent1 ) -> {
                    TimerComponent timer = ( TimerComponent ) gameComponent1;
                    double secondsLeft = ( timer.getSecondsLeftAsDouble( ) * 1.0E9D ) - clock.getNanoseconds( );
                    if ( secondsLeft <= 0.0D )
                    {
                        secondsLeft = 0.0D;
                    }
                    timer.setSecondsLeft( secondsLeft * 1.0E-9D );
                } );
            }
        } );
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
        updateTimers( );
    }

}