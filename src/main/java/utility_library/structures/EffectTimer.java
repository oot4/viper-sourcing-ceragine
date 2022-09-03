package utility_library.structures;

import utility_library.algorithms.core.Sum;

/**
 * A class representing a timer for an effect that transitions from one magnitude to another
 */
public final class EffectTimer
{

    /**
     * Index of the delay phase at the start of the effect timer
     */
    public static final int START_DELAY_PHASE = 0;

    /**
     * Index of the transition phase after the starting delay phase of the effect timer
     */
    public static final int START_TRANSITION_PHASE = 1;

    /**
     * Index of the delay phase at the end of the effect timer
     */
    public static final int STOP_DELAY_PHASE = 2;

    /**
     * Index of the transition phase before the ending delay phase of the effect timer
     */
    public static final int STOP_TRANSITION_PHASE = 3;

    /**
     * Total number of phases
     */
    private static final int NUMBER_OF_PHASES = 4;

    /**
     * Should the transition effect be looped?
     */
    private boolean loop = false;

    /**
     * Should the effect transition in the opposite direction
     */
    private boolean reverse = false;

    /**
     * Minimum possible effect magnitude
     */
    private final long minimum;

    /**
     * Maximum possible effect magnitude
     */
    private final long maximum;

    /**
     * Timers for each phase
     */
    private final long[] phaseTimers = new long[ NUMBER_OF_PHASES ];

    /**
     * Switches to turn on or off each phase
     */
    private final boolean[] phaseSwitches = new boolean[ NUMBER_OF_PHASES ];

    /**
     * Constructor for creating a new instance of an {@code EffectTimer}
     *
     * @param minimum the specified minimum effect magnitude the transition will start or end with
     * @param maximum the specified maximum effect magnitude the transition will start or end with
     */
    private EffectTimer( long minimum , long maximum )
    {
        this.minimum = minimum;
        this.maximum = maximum;
    }

    /**
     * Creates a new instance of an {@code EffectTimer}
     *
     * @param minimum the specified minimum effect magnitude the transition will start or end with
     * @param maximum the specified maximum effect magnitude the transition will start or end with
     *
     * @return a new {@link EffectTimer effect timer} object
     */
    public static EffectTimer from( long minimum , long maximum )
    {
        return new EffectTimer( minimum , maximum );
    }

    /**
     * Gets the timer value of the specified phase
     *
     * @param phase the specified phase to get the timer value of
     *
     * @return the number of update ticks taken to complete the phase
     */
    public long getTimer( int phase )
    {
        return phaseTimers[ phase ];
    }

    /**
     * Sets the timer of the specified phase to the value specified
     * <p>Setting a value of zero will disable the phase timer and set its switch to {@code False}
     * <br>
     * Setting a value greater than zero will enable the phase timer and set its switch to {@code True}
     * </p>
     *
     * @param phase the specified phase to get the timer value of
     * @param value the specified value to assign to the phases timer
     */
    public void setTimer( int phase , long value )
    {
        phaseTimers[ phase ] = Math.max( 0 , value );
        phaseSwitches[ phase ] = value > 0;
    }

    /**
     * Sets the timer for each phase all at once
     * <p>Setting a value of zero will disable the phase timer and set its switch to {@code False}
     * <br>
     * Setting a value greater than zero will enable the phase timer and set its switch to {@code True}
     * </p>
     *
     * @param startDelayTimer      the specified value to set the start delay phase timer to
     * @param startTransitionTimer the specified value to set the start transition phase timer to
     * @param stopTransitionTimer  the specified value to set the stop transition phase timer to
     * @param stopDelayTimer       the specified value to set the stop delay phase timer to
     */
    public void setAllTimers( long startDelayTimer , long startTransitionTimer , long stopTransitionTimer , long stopDelayTimer )
    {
        setTimer( START_DELAY_PHASE , startDelayTimer );
        setTimer( START_TRANSITION_PHASE , startTransitionTimer );
        setTimer( STOP_TRANSITION_PHASE , stopTransitionTimer );
        setTimer( STOP_DELAY_PHASE , stopDelayTimer );
    }

    /**
     * Is the specified phase active?
     *
     * @param phase the specified phase to test if active
     *
     * @return {@code True} if the switch for the phase is on and the phase is active. {@code False} otherwise
     */
    public boolean isActive( int phase )
    {
        return phaseSwitches[ phase ];
    }

    /**
     * Deactivates a phase timer if it is not already deactivated. A phase timer can be activated by setting a timer value greater than zero
     *
     * @param phase the specified phase to deactivate
     *
     * @see EffectTimer#setTimer(int , long) setTimer
     * @see EffectTimer#setAllTimers(long , long , long , long) setAllTimers
     */
    public void deactivate( int phase )
    {
        if ( phaseSwitches[ phase ] )
        {
            setTimer( phase , - 1 );
        }
    }

    /**
     * Is this effect timer a looped timer?
     *
     * @return {@code True} if the effect timer is looped. {@code False} otherwise
     */
    public boolean isLooped( )
    {
        return loop;
    }

    /**
     * Sets whether this effect timer is looped to {@code True} or {@code False}
     *
     * @param loop is this effect timer a looped timer?
     */
    public void setLooped( boolean loop )
    {
        this.loop = loop;
    }

    /**
     * Is the effect timer transitioning in the opposite direction?
     *
     * @return {@code True} if the effect timer will transition in the opposite direction. {@code False} otherwise
     */
    public boolean isReversed( )
    {
        return reverse;
    }

    /**
     * Sets whether this effect timer transitions in the opposite direction to {@code True} or {@code False}
     *
     * @param reverse is this effect timer a reversed timer?
     */
    public void setReversed( boolean reverse )
    {
        this.reverse = reverse;
    }

    /**
     * Gets the minimum magnitude of the transition effect
     *
     * @return the minimum value the magnitude of the effect can ever be
     */
    public long getMinimum( )
    {
        return minimum;
    }

    /**
     * Gets the maximum magnitude of the transition effect
     *
     * @return the minimum value the magnitude of the effect can ever be
     */
    public long getMaximum( )
    {
        return maximum;
    }

    /**
     * Gets the total sum of time taken to perform all phases
     *
     * @return the combined sum of time to complete each phase
     */
    public long getDuration( )
    {
        return Sum.of( phaseTimers[ START_DELAY_PHASE ] , phaseTimers[ START_TRANSITION_PHASE ] , phaseTimers[ STOP_TRANSITION_PHASE ] , phaseTimers[ STOP_DELAY_PHASE ] );
    }

}