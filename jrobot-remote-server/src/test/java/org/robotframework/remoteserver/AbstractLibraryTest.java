package org.robotframework.remoteserver;

import java.util.concurrent.atomic.AtomicBoolean;
import org.robotframework.javalib.annotation.ArgumentNames;
import org.robotframework.javalib.annotation.RobotKeyword;
import org.robotframework.javalib.annotation.RobotKeywordOverload;
import org.robotframework.javalib.annotation.RobotKeywords;
import org.robotframework.remoteserver.anotations.KeywordDocumentation;
import org.robotframework.remoteserver.anotations.KeywordTags;
import org.robotframework.remoteserver.library.AbstractClassLibrary;

@RobotKeywords public class AbstractLibraryTest extends AbstractClassLibrary {

    public static final AtomicBoolean closed = new AtomicBoolean(false);
    public static final String URI = "TestURI";

    public AbstractLibraryTest(RemoteServer server) {
        super(server);
    }

    @RobotKeyword @KeywordDocumentation(value = "plusDoc") @ArgumentNames(value = {"a", "b"})
    public int plus(int a, int b) {
        return a + b;
    }

    @RobotKeyword @KeywordDocumentation(value = "minusDoc") @KeywordTags(value = {"minus", "-"})
    public int minus(int a, int b) {
        return a - b;
    }

    @RobotKeyword @KeywordTags(value = {"default"}) public int defaultKeyword(int a, int b) {
        return a + b;
    }

    @RobotKeywordOverload @KeywordTags(value = {"default", "defaultOverload"})
    public int defaultKeyword(int a, int b, int c) {
        return a + b + c;
    }

    @Override public String getURI() {
        return URI;
    }

    @Override public void close() {
        closed.set(true);
    }
}
