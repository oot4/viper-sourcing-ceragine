/*
 * Copyright (c) 2022. The Ceragine project files, related assets and related source code
 * were created by, Ogesoka (Oge) Tasie and should not be copied or distributed without
 * consent of the creator (Ogesoka Tasie). All Rights Reserved.
 */

package game_debugger;

import java.util.logging.Level;

/**
 * An enum class representing an alert with severity levels
 */
public enum Alert
{
    /**
     * Information level
     */
    INFO( Level.INFO ),
    /**
     * Warning level indicating a potential problem
     */
    WARN( Level.WARNING ),
    /**
     * Severe level indicating a serious failure
     */
    DIRE( Level.SEVERE ),
    ;

    /**
     * The alert level
     */
    private final Level level;

    /**
     * Creates a new {@code Alert}
     *
     * @param level The specified {@link Level level} to assign to this alert
     */
    Alert( Level level )
    {
        this.level = level;
    }

    /**
     * An alert level indicating severity
     *
     * @return the specified level of this alert
     */
    Level getLevel( )
    {
        return level;
    }
}