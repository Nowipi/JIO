package io.github.nowipi.jio.linux;

import java.lang.foreign.FunctionDescriptor;
import java.lang.foreign.Linker;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.SymbolLookup;
import java.lang.invoke.MethodHandle;
import java.util.HashMap;
import java.util.Map;

public class LibC {

    public static final int O_RDWR = 0;
    public static final int CS5 = 0x00000000;
    public static final int CS6 = 0x00000010;
    public static final int CS7 = 0x00000020;
    public static final int CS8 = 0x00000030;
    public static final int B9600 = 015;
    public static final int TCSANOW = 0;

    public static final SymbolLookup lookup;

    private static final Map<String, MethodHandle> loadedMethodHandles;

    static {
        lookup = Linker.nativeLinker().defaultLookup();
        loadedMethodHandles = new HashMap<>();
    }

    private static final FunctionDescriptor openDescriptor = FunctionDescriptor.of(C.INT,
            C.POINTER,
            C.INT
    );
    public static int open(MemorySegment pathname, int flags) {
        try {
            return (int) getMethodHandle("open", openDescriptor).invokeExact(pathname, flags);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private static final FunctionDescriptor tcgetattrDescriptor = FunctionDescriptor.of(C.INT,
            C.INT,
            C.POINTER
    );
    public static int tcgetattr(int fildes, MemorySegment termios_p) {
        try {
            return (int) getMethodHandle("tcgetattr", tcgetattrDescriptor).invokeExact(fildes, termios_p);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private static final FunctionDescriptor cfsetispeedDescriptor = FunctionDescriptor.of(C.INT,
            C.POINTER,
            C.INT
    );
    public static int cfsetispeed(MemorySegment termios_p, int speed) {
        try {
            return (int) getMethodHandle("cfsetispeed", cfsetispeedDescriptor).invokeExact(termios_p, speed);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private static final FunctionDescriptor cfsetospeedDescriptor = FunctionDescriptor.of(C.INT,
            C.POINTER,
            C.INT
    );
    public static int cfsetospeed(MemorySegment termios_p, int speed) {
        try {
            return (int) getMethodHandle("cfsetospeed", cfsetospeedDescriptor).invokeExact(termios_p, speed);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private static final FunctionDescriptor tcsetattrDescriptor = FunctionDescriptor.of(C.INT,
            C.INT,
            C.INT,
            C.POINTER
    );
    public static int tcsetattr(int filedes, int optional_actions, MemorySegment termios_p) {
        try {
            return (int) getMethodHandle("tcsetattr", tcsetattrDescriptor).invokeExact(filedes, optional_actions, termios_p);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private static final FunctionDescriptor readDescriptor = FunctionDescriptor.of(C.INT,
            C.INT,
            C.INT,
            C.POINTER
    );
    public static int read(int filedes, MemorySegment buf, int nbyte) {
        try {
            return (int) getMethodHandle("read", readDescriptor).invokeExact(filedes, buf, nbyte);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private static final FunctionDescriptor writeDescriptor = FunctionDescriptor.of(C.INT,
            C.INT,
            C.INT,
            C.POINTER
    );
    public static int write(int fd, MemorySegment buf, int count) {
        try {
            return (int) getMethodHandle("write", writeDescriptor).invokeExact(fd, buf, count);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private static final FunctionDescriptor closeDescriptor = FunctionDescriptor.of(C.INT,
            C.INT
    );
    public static int close(int fd) {
        try {
            return (int) getMethodHandle("close", closeDescriptor).invokeExact(fd);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private static MethodHandle getMethodHandle(String methodName, FunctionDescriptor methodDescriptor) {
        var handle = loadedMethodHandles.get(methodName);
        if (handle == null) {
            MemorySegment address = lookup.find(methodName).orElseThrow();
            handle = Linker.nativeLinker().downcallHandle(address, methodDescriptor);
            loadedMethodHandles.put(methodName, handle);
            return handle;
        }
        return handle;
    }
}
