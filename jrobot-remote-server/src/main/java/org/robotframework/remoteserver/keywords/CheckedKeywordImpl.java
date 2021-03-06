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

import java.lang.reflect.Method;
import java.util.Objects;
import org.robotframework.javalib.annotation.ArgumentNames;
import org.robotframework.javalib.reflection.ArgumentConverter;
import org.robotframework.javalib.reflection.ArgumentGrouper;
import org.robotframework.javalib.reflection.IArgumentConverter;
import org.robotframework.javalib.reflection.IArgumentGrouper;
import org.robotframework.remoteserver.anotations.KeywordDocumentation;
import org.robotframework.remoteserver.anotations.KeywordTags;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class providing implementation of {@link CheckedKeyword}
 */
public class CheckedKeywordImpl implements CheckedKeyword {

    protected static final Logger LOG = LoggerFactory.getLogger(CheckedKeywordImpl.class.getName());

    private final IArgumentConverter argumentConverter;
    private final IArgumentGrouper argumentGrouper;
    private final Method method;
    private final Object obj;

    /**
     * Constructor creating {@link CheckedKeyword} providing {@link Object} and {@link Method},
     * that are associated together
     *
     * @param obj    {@link Object} instance used for execution of {@link Method}
     * @param method {@link Method} providing execution routine
     */
    public CheckedKeywordImpl(Object obj, Method method) {
        this.obj = obj;
        this.method = method;
        this.argumentConverter = new ArgumentConverter(method.getParameterTypes());
        this.argumentGrouper = new ArgumentGrouper(method.getParameterTypes());
    }

    @Override public Object execute(Object[] args) {
        try {
            Object[] groupedArguments = getArgumentGrouper().groupArguments(args);
            Object[] convertedArguments = getArgumentConverter().convertArguments(groupedArguments);
            return method.invoke(obj, convertedArguments);
        } catch (Exception e) {
            LOG.error("Error invoking {} with {}", method.getName(), args, e);
            throw new RuntimeException(e);
        }
    }

    @Override public boolean canExecute(Object[] args) {
        try {
            Object[] groupedArguments = getArgumentGrouper().groupArguments(args);
            Object[] convertedArguments = getArgumentConverter().convertArguments(groupedArguments);
            for (int i = 0; i < args.length; i++) {
                if ((convertedArguments[i] == null) && (args[i] != null)) {
                    return false;
                }
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override public Class<?>[] getArguments() {
        return method.getParameterTypes();
    }

    @Override public String[] getTags() {
        KeywordTags tags = method.getAnnotation(KeywordTags.class);
        if (Objects.nonNull(tags) && tags.value().length > 0) {
            return tags.value();
        }
        return new String[0];
    }

    /**
     * @return {@link IArgumentConverter}used for conversion of {@link CheckedKeyword} arguments
     */
    protected IArgumentConverter getArgumentConverter() {
        return argumentConverter;
    }

    /**
     * @return {@link IArgumentGrouper} used for grouping of {@link CheckedKeyword} arguments
     */
    protected IArgumentGrouper getArgumentGrouper() {
        return argumentGrouper;
    }

    @Override public String getDocumentation() {
        return method.getAnnotation(KeywordDocumentation.class) != null ? method.getAnnotation(
                KeywordDocumentation.class).value() : "";
    }

    @Override public String[] getArgumentNames() {
        if (method.isAnnotationPresent(ArgumentNames.class)) {
            return method.getAnnotation(ArgumentNames.class).value();
        }
        String[] names = new String[method.getParameterCount()];
        if (method.isAnnotationPresent(ArgumentNames.class)) {
            final ArgumentNames argumentNames = method.getAnnotation(ArgumentNames.class);
            if (argumentNames.value().length != names.length) {
                throw new IllegalStateException(
                        String.format("%s has invalid argument count than method %s [%d != %d]", argumentNames, method,
                                argumentNames.value().length, names.length));
            }
            for (int i = 0; i < method.getParameters().length; i++) {
                names[i] = argumentNames.value()[i];
            }
        } else {
            for (int i = 0; i < method.getParameters().length; i++) {
                names[i] = method.getParameters()[i].getName();
            }
        }
        return names;
    }
}
