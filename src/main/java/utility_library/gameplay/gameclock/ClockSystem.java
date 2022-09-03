/*
 * Copyright (c) 2022. The Ceragine project files, related assets and related source code
 * were created by, Ogesoka (Oge) Tasie and should not be copied or distributed without
 * consent of the creator (Ogesoka Tasie). All Rights Reserved.
 */

package utility_library.gameplay.gameclock;

import ecs_architecture.standard.GameComponent;
import ecs_architecture.standard.GameSystem;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

public final class ClockSystem extends GameSystem
{

    private void updateGameClocks( )
    {
        SimpleDateFormat formatter = new SimpleDateFormat( "hh:mm:ss aa" );
        Calendar calendar = Calendar.getInstance( );
        Date date = calendar.getTime( );
        String timeString = formatter.format( date );
        int seconds = calendar.get( Calendar.SECOND );
        int minutes = calendar.get( Calendar.MINUTE );
        int hours = calendar.get( Calendar.HOUR_OF_DAY );
        if ( hours > 12 )
        {
            hours -= 12;
        }
        int finalHours = hours;
        Map< String, GameComponent > clocks = get( ClockComponent.class );
        double nanoseconds = System.nanoTime( );
        clocks.forEach( ( entity , gameComponent ) -> {
            ClockComponent clock = ( ClockComponent ) gameComponent;
            clock.setHours( finalHours );
            clock.setMinutes( minutes );
            clock.setSeconds( seconds );
            clock.setNanoseconds( nanoseconds );
            clock.setTime( timeString );
        } );
    }

    @Override
    protected void internalInitialize( )
    {
        add( new ClockComponent( "|clock_name=>6yHwJKGe0-ehHQlkAPyh_XDZ|" ) );
    }

    @Override
    protected void internalTerminate( )
    {
        //Nothing to terminate
    }

    @Override
    protected void internalPerform( )
    {
        updateGameClocks( );
    }

}