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
package org.robotframework.remoteserver.javalib;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import org.robotframework.javalib.beans.annotation.IKeywordExtractor;

public class SimpleKeywordExtractor implements IKeywordExtractor<OverloadableKeyword> {

    public Map<String, OverloadableKeyword> extractKeywords(Object keywordBean) {
        Map<String, OverloadableKeyword> overloadableKeywords = new HashMap<>();
        Method[] methods = keywordBean.getClass().getMethods();

        for (final Method method : methods) {
            if (method.getDeclaringClass() != Object.class && Modifier.isPublic(method.getModifiers())) {
                createOrUpdateKeyword(overloadableKeywords, keywordBean, method);
            }
        }
        return overloadableKeywords;
    }

    private void createOrUpdateKeyword(Map<String, OverloadableKeyword> extractedKeywords, Object keywordBean,
            Method method) {
        String name = method.getName();
        if (extractedKeywords.containsKey(name)) {
            extractedKeywords.get(name).addOverload(method);
        } else {
            extractedKeywords.put(name, new OverloadableKeyword(keywordBean, method));
        }
    }

}
