package com.jason.database;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 
 * @author hao
 *
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DBField {

    public String name();

    public Type type();

    public int length() default 255;

    public String fineldName() default "" ;

    public boolean primaryKey() default false;

    public enum Type {
        INTEGER,
        VARCHAR,
        DATE,
        DECIMAL,
        DATETIME,
        TEXT,
        UTCDATETIME
    };

}
