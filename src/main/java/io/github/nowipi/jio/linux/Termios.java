package io.github.nowipi.jio.linux;

import java.lang.foreign.GroupLayout;
import java.lang.foreign.MemoryLayout;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.SegmentAllocator;

import static java.lang.foreign.ValueLayout.*;

public class Termios {

    public static final GroupLayout layout;

    static {
        layout = MemoryLayout.structLayout(
                JAVA_INT.withName("c_iflag"),
                JAVA_INT.withName("c_oflag"),
                JAVA_INT.withName("c_cflag"),
                JAVA_INT.withName("c_lflag"),
                JAVA_BYTE.withName("c_line"),
                MemoryLayout.sequenceLayout(32, JAVA_BYTE).withName("c_cc")
        ).withName("termios");
    }

    public static MemorySegment allocate(SegmentAllocator allocator) {
        return allocator.allocate(layout);
    }

    public static void c_cflag(MemorySegment termios, int v) {
        termios.set(JAVA_INT, layout.byteOffset(MemoryLayout.PathElement.groupElement("c_cflag")), v);
    }

    public static int c_cflag(MemorySegment termios) {
        return termios.get(JAVA_INT, layout.byteOffset(MemoryLayout.PathElement.groupElement("c_cflag")));
    }

    public static void c_lflag(MemorySegment termios, int v) {
        termios.set(JAVA_INT, layout.byteOffset(MemoryLayout.PathElement.groupElement("c_lflag")), v);
    }

    public static void c_iflag(MemorySegment termios, int v) {
        termios.set(JAVA_INT, layout.byteOffset(MemoryLayout.PathElement.groupElement("c_iflag")), v);
    }

    public static void c_oflag(MemorySegment termios, int v) {
        termios.set(JAVA_INT, layout.byteOffset(MemoryLayout.PathElement.groupElement("c_oflag")), v);
    }

    public static void c_cc(MemorySegment termios, int offset, byte v) {
        termios.set(JAVA_BYTE, layout.byteOffset(MemoryLayout.PathElement.groupElement("c_cc")) + offset, v);
    }
}
