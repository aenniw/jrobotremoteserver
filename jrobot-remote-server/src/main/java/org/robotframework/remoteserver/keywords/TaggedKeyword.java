package org.robotframework.remoteserver.keywords;

import org.robotframework.javalib.keyword.DocumentedKeyword;

public interface TaggedKeyword extends DocumentedKeyword {

    String[] getTags();

}
