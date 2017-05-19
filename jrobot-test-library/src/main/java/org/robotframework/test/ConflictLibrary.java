package org.robotframework.test;

import org.robotframework.javalib.annotation.RobotKeyword;
import org.robotframework.javalib.annotation.RobotKeywordOverload;
import org.robotframework.javalib.annotation.RobotKeywords;
import org.robotframework.remoteserver.RemoteServer;
import org.robotframework.remoteserver.library.AbstractClassLibrary;

@RobotKeywords public class ConflictLibrary extends AbstractClassLibrary {

    public ConflictLibrary(RemoteServer server) {
        super(server);
    }

    @Override public String getURI() {
        return getClass().getSimpleName();
    }

    @Override public void close() {
    }

    public void conflictMethod(final RemoteServer s, final ConflictLibrary a) {
        throw new RuntimeException("Invalid Keyword executed");
    }

    @RobotKeyword public String conflictMethod(final String s, final String a) {
        return "[" + s + "],[" + a + "]";
    }

    @RobotKeyword public void conflictOverloadedMethod() {
        throw new RuntimeException("Invalid Keyword executed");
    }

    public void conflictOverloadedMethod(final String s, final boolean b, final int a) {
        throw new RuntimeException("Invalid Keyword executed");
    }

    @RobotKeywordOverload public String conflictOverloadedMethod(final String s, final int a, final boolean b) {
        return "[" + s + "],[" + a + "],[" + b + "]";
    }

}
