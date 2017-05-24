package org.robotframework.remoteserver.keywords;

import java.lang.reflect.Method;
import org.robotframework.javalib.keyword.DocumentedKeyword;

/**
 * Represent extension of {@link DocumentedKeyword} and {@link TaggedKeyword}
 * capable of managing multiple implementations of keyword
 */
public interface OverloadedKeyword extends DocumentedKeyword, TaggedKeyword {

    /**
     * Adds alternative {@link org.robotframework.javalib.keyword.Keyword} implementation,
     * later used for execution of keyword if appropriate arguments are provided
     *
     * @param method Implementation that will be used as alternative,
     *               for {@link org.robotframework.javalib.keyword.Keyword} execution
     */
    void addOverload(Method method);
}
