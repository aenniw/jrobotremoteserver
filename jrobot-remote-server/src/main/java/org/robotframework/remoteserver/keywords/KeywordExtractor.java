package org.robotframework.remoteserver.keywords;

import java.util.Map;
import org.robotframework.javalib.keyword.Keyword;
import org.robotframework.remoteserver.library.RemoteLibrary;

/**
 * Provides extraction of {@link Keyword} from {@link RemoteLibrary} containing definitions of Keywords
 *
 * @param <T> Specific Keyword type
 */
public interface KeywordExtractor<T extends Keyword> {

    /**
     * Extract {@link Keyword} and verify their correctness
     *
     * @param keywordBean Instance containing definitions of Keywords
     * @return Names of {@link Keyword}  associated with their implementation
     */
    Map<String, T> extractKeywords(RemoteLibrary keywordBean);
}
