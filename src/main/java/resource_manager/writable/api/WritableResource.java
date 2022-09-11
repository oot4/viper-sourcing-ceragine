/*
 * Copyright (c) 2022. The Ceragine project files, related assets and related source code
 * were created by, Ogesoka (Oge) Tasie and should not be copied or distributed without
 * consent of the creator (Ogesoka Tasie). All Rights Reserved.
 */

package resource_manager.writable.api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * An annotation interface used to tag a class field as being a writable resource with a filename extension and version number
 */
@Target( ElementType.TYPE )
@Retention( RetentionPolicy.RUNTIME )
@Inherited
public @interface WritableResource
{

    /**
     * The name given to an output file this writable object is written to
     *
     * @return a string value containing the writable resource filename
     */
    String filename( ) default "##default";

    /**
     * Gets the extension type of the writable resource
     *
     * @return a string value containing the writable resource file extension
     */
    String extension( ) default "json";

    /**
     * The current version of the writable object. Example of good version string semantics:
     * <ul>
     *      <li>0.1.0</li>
     *      <li>10.2200.1</li>
     * </ul>
     *
     * @return a string value corresponding to the version of the writable object
     */
    String version( ) default "1.0.0";

}