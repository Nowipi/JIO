package io.github.nowipi.jio.linux;

import java.lang.foreign.AddressLayout;
import java.lang.foreign.ValueLayout;

final class C {

    public static final ValueLayout.OfInt INT = ValueLayout.JAVA_INT;
    public static final AddressLayout POINTER = ValueLayout.ADDRESS;
    public static final ValueLayout.OfByte CHAR = ValueLayout.JAVA_BYTE;

    private C() {
    }
}
