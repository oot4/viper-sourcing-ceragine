/*
 * Copyright (c) 2022. The Ceragine project files, related assets and related source code
 * were created by, Ogesoka (Oge) Tasie and should not be copied or distributed without
 * consent of the creator (Ogesoka Tasie). All Rights Reserved.
 */

package utility_library.gameplay.gameclock;

import ecs_architecture.standard.GameComponent;

public final class ClockComponent extends GameComponent
{

    private int hours;

    private int minutes;

    private int seconds;

    private double currentNanoseconds;

    private double previousNanoseconds;

    private String time;

    public ClockComponent( )
    {
        this( "" );
    }

    public ClockComponent( String name )
    {
        super( name );
        hours = 0;
        minutes = 0;
        seconds = 0;
        previousNanoseconds = System.nanoTime( );
        currentNanoseconds = System.nanoTime( );
        time = "";
    }

    int getHours( ) { return hours; }

    void setHours( int hours ) { this.hours = hours; }

    int getMinutes( ) { return minutes; }

    void setMinutes( int minutes ) { this.minutes = minutes; }

    int getSeconds( ) { return seconds; }

    void setSeconds( int seconds ) { this.seconds = seconds; }

    double getNanoseconds( ) { return currentNanoseconds - previousNanoseconds; }

    public void setNanoseconds( double nanoseconds )
    {
        previousNanoseconds = currentNanoseconds;
        currentNanoseconds = nanoseconds;
    }

    public String getTime( ) { return time; }

    void setTime( String time ) { this.time = time; }

}