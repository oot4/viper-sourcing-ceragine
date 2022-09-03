package utility_library.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.LOCAL_VARIABLE;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.MODULE;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE;

/**
 * An annotation interface used to provide critical information about the use or functionality an element of code
 */
@Target( { TYPE , FIELD , METHOD , PARAMETER , CONSTRUCTOR , LOCAL_VARIABLE , MODULE } )
@Retention( RetentionPolicy.SOURCE )
public @interface Note
{

    /**
     * A descriptive text containing the critical information
     *
     * @return a string value containing descriptive text about the code element
     */
    String value( );

}