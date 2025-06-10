package io.github.nowipi.jio.windows;

import java.lang.foreign.*;

public final class DCB {

    public static final GroupLayout layout;

    static {
        layout = MemoryLayout.structLayout(
                Win32.DWORD.withName("DCBlength"),     // DWORD
                Win32.DWORD.withName("BaudRate"),      // DWORD
                Win32.DWORD.withName("Flags"),         // DWORD bitfield flags as full int
                Win32.WORD.withName("wReserved"),   // WORD
                Win32.WORD.withName("XonLim"),      // WORD
                Win32.WORD.withName("XoffLim"),     // WORD
                Win32.BYTE.withName("ByteSize"),     // BYTE
                Win32.BYTE.withName("Parity"),       // BYTE
                Win32.BYTE.withName("StopBits"),     // BYTE
                Win32.CHAR.withName("XonChar"),      // CHAR
                Win32.CHAR.withName("XoffChar"),     // CHAR
                Win32.CHAR.withName("ErrorChar"),    // CHAR
                Win32.CHAR.withName("EofChar"),      // CHAR
                Win32.CHAR.withName("EvtChar"),      // CHAR
                Win32.WORD.withName("wReserved1")   // WORD
        ).withName("DCB");
    }

    public static MemorySegment allocate(SegmentAllocator allocator) {
        return allocator.allocate(layout);
    }

    public static void setDCBlength(MemorySegment dcb, int DCBlength) {
        dcb.set(Win32.DWORD, layout.byteOffset(MemoryLayout.PathElement.groupElement("DCBlength")), DCBlength);
    }

    public static void setBaudRate(MemorySegment dcb, int BaudRate) {
        dcb.set(Win32.DWORD, layout.byteOffset(MemoryLayout.PathElement.groupElement("BaudRate")), BaudRate);
    }

    public static void setByteSize(MemorySegment dcb, byte ByteSize) {
        dcb.set(Win32.BYTE, layout.byteOffset(MemoryLayout.PathElement.groupElement("ByteSize")), ByteSize);
    }

    public static void setStopBits(MemorySegment dcb, byte StopBits) {
        dcb.set(Win32.BYTE, layout.byteOffset(MemoryLayout.PathElement.groupElement("StopBits")), StopBits);
    }

    public static void setParity(MemorySegment dcb, byte Parity) {
        dcb.set(Win32.BYTE, layout.byteOffset(MemoryLayout.PathElement.groupElement("Parity")), Parity);
    }
}
