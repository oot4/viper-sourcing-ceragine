/*
 * Copyright (c) 2022. The Ceragine project files, related assets and related source code
 * were created by, Ogesoka (Oge) Tasie and should not be copied or distributed without
 * consent of the creator (Ogesoka Tasie). All Rights Reserved.
 */

package utility_library.gameplay.gameclock;

import ecs_architecture.standard.GameComponent;

public final class TimerComponent extends GameComponent
{

    private final double duration;

    private double secondsLeft;

    public TimerComponent( int duration )
    {
        this( "" , duration );
    }

    public TimerComponent( String name , int duration )
    {
        super( name );
        secondsLeft = duration;
        this.duration = duration;
    }

    public long getSecondsLeft( ) { return Math.round( secondsLeft ); }

    void setSecondsLeft( double seconds )
    {
        secondsLeft = seconds;
    }

    public long elapsedSeconds( ) { return Math.round( duration - secondsLeft ); }

    double getSecondsLeftAsDouble( ) { return secondsLeft; }

}