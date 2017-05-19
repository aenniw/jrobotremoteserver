/* Copyright 2014 Kevin Ormbrek
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
/* This code is derived from JavalibCore 
 * Copyright 2008 Nokia Siemens Networks Oyj
 */
package org.robotframework.remoteserver.keywords;

import com.google.common.collect.Iterables;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OverloadedKeywordImpl implements OverloadedKeyword {

    protected static final Logger LOG = LoggerFactory.getLogger(OverloadedKeywordImpl.class.getName());

    private final Map<Integer, List<CheckedKeyword>> keywordMap = new HashMap<>();
    private final String keywordName;
    private final Object keywordClass;

    public OverloadedKeywordImpl(Object keywordClass, Method method) {
        this.keywordName = method.getName();
        this.keywordClass = keywordClass;
        addOverload(method);
    }

    @Override public Object execute(Object[] arguments) {
        final int argCount = arguments.length;
        if (keywordMap.containsKey(argCount)) {
            for (CheckedKeyword checkedKeyword : keywordMap.get(argCount)) {
                if (checkedKeyword.canExecute(arguments)) {
                    LOG.debug("EXECUTED {} args{}", keywordName, checkedKeyword.getArgumentNames().length);
                    return checkedKeyword.execute(arguments);
                }
                LOG.debug("EXECUTION SKIPPED {} args {}", keywordName, checkedKeyword.getArgumentNames().length);
            }
            throw new IllegalArgumentException(
                    String.format("%s cannot be executed with args %s.", keywordName, Arrays.toString(arguments)));
        } else if (keywordMap.size() == 1) {
            throw new IllegalArgumentException(String.format("%s takes %d argument(s), received %d.", keywordName,
                    Iterables.get(keywordMap.keySet(), 0), argCount));
        }
        throw new IllegalArgumentException(
                String.format("No overload of %s takes %d argument(s).", keywordName, argCount));
    }

    @Override public void addOverload(Method method) {
        final int argCount = method.getParameterTypes().length;
        if (hasVariableArgs(method)) {
            LOG.warn(String.format("Overloads with variable arguments not supported. Ignoring overload %s",
                    method.toString()));
        } else if (!keywordMap.containsKey(argCount)) {
            keywordMap.put(argCount, new ArrayList<>());
            keywordMap.get(argCount).add(new CheckedKeywordImpl(keywordClass, method));
        } else {
            keywordMap.get(argCount).add(new CheckedKeywordImpl(keywordClass, method));
        }
    }

    @Override public String[] getArgumentNames() {
        final int min = Collections.min(keywordMap.keySet());
        final int max = Collections.max(keywordMap.keySet());
        final String[] arguments = new String[max], minNames = Iterables.get(keywordMap.get(min), 0).getArgumentNames(),
                maxNames =
                        Iterables.get(keywordMap.get(max), 0).getArgumentNames();
        for (int i = 0; i < max; i++) {
            if (i < min)
                arguments[i] = minNames[i];
            else
                arguments[i] = maxNames[i] + "=";
        }
        return arguments;
    }

    private boolean hasVariableArgs(Method method) {
        final int argCount = method.getParameterTypes().length;
        return (argCount > 0 && method.getParameterTypes()[argCount - 1].isArray());
    }

    @Override public String getDocumentation() {
        for (List<CheckedKeyword> keywords : keywordMap.values()) {
            for (CheckedKeyword keyword : keywords) {
                if (!keyword.getDocumentation().isEmpty()) {
                    return keyword.getDocumentation();
                }
            }
        }
        return "";
    }

    @Override public String[] getTags() {
        Set<String> tags = new HashSet<>();
        for (List<CheckedKeyword> keywords : keywordMap.values()) {
            for (CheckedKeyword keyword : keywords) {
                Arrays.stream(keyword.getTags()).filter(Objects::nonNull).collect(Collectors.toCollection(() -> tags));
            }
        }
        return tags.toArray(new String[tags.size()]);
    }
}
