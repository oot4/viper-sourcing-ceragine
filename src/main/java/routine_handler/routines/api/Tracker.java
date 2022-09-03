package routine_handler.routines.api;

/**
 * A functional interface to be implemented when updates and frames need to be tracked
 */
@FunctionalInterface
public interface Tracker
{

    /**
     * Tracks the number of updates and number of frames
     *
     * @param updates the specified number of updates to track
     * @param frames  the specified number of frames to track
     */
    void track( long updates , long frames );

}