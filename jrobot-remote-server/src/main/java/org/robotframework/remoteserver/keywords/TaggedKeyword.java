package org.robotframework.remoteserver.keywords;

import org.robotframework.javalib.keyword.DocumentedKeyword;

/**
 * Represent extension of {@link DocumentedKeyword} providing option to for tagging of {@link org.robotframework.javalib.keyword.Keyword}
 */
public interface TaggedKeyword extends DocumentedKeyword {

    /**
     * @return Tags associated with current {@link org.robotframework.javalib.keyword.Keyword}
     */
    String[] getTags();

}
