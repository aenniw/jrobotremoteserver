package org.robotframework.test;

import org.robotframework.javalib.annotation.ArgumentNames;
import org.robotframework.javalib.annotation.RobotKeywords;
import org.robotframework.remoteserver.RemoteServer;

@RobotKeywords public class ArgumentsLibrary extends BaseLibrary {

    public ArgumentsLibrary(RemoteServer server) {
        super(server);
    }

    @Override @ArgumentNames({"element_1", "element_2"}) public double add(double a, double b) {
        return super.add(a, b);
    }

    @Override @ArgumentNames({"element_1", "element_2"}) public int add(int a, int b) {
        return super.add(a, b);
    }

    @Override @ArgumentNames({"element_1", "element_2"}) public double sub(double a, double b) {
        return super.sub(a, b);
    }

    @Override @ArgumentNames({"element_1", "element_2"}) public int sub(int a, int b) {
        return super.sub(a, b);
    }
}
