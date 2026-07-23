package org.leaf.utils;

/// Annotation indicating a specific is unimplemented. This can be used as placeholders in documentation or decorate a method / class that has yet to be written.
/// <br><br>{@link Unimplemented#since()}: Represents the date when this entity has been declared as {@link Unimplemented}.
/// <br>{@link Unimplemented#expectedImplementation()}: Represents the expected time when this entity should be implemented.
@Deprecated
public @interface Unimplemented {
    String since() default "";
    String expectedImplementation() default "";
}
