package io.github.nowipi.jio.linux;

import java.io.IOException;
import java.lang.foreign.*;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.VarHandle;
import java.util.HashMap;
import java.util.Map;

public class LibC {

    public static final int O_RDWR = 2;
    public static final int CS5 = 0x00000000;
    public static final int CS6 = 0x00000010;
    public static final int CS7 = 0x00000020;
    public static final int CS8 = 0x00000030;
    public static final int B9600 = 015;
    public static final int TCSANOW = 0;
    public static final int PARENB = 256;
    public static final int CSTOPB = 64;
    public static final int CSIZE = 48;
    public static final int CRTSCTS = 0x80000000;
    public static final int CREAD = 128;
    public static final int CLOCAL = 2048;
    public static final int VTIME = 5;
    public static final int VMIN = 6;

    public static final SymbolLookup lookup;

    private static final Map<String, MethodHandle> loadedMethodHandles;
    private static final Linker.Option ccs;
    private static final StructLayout capturedStateLayout;
    private static final VarHandle errnoHandle;

    static {
        ccs = Linker.Option.captureCallState("errno");
        capturedStateLayout = Linker.Option.captureStateLayout();
        errnoHandle = capturedStateLayout.varHandle(MemoryLayout.PathElement.groupElement("errno"));
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
            C.POINTER,
            C.INT
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
            C.POINTER,
            C.INT
    );
    public static int write(int fd, MemorySegment buf, int count) {
        try(var arena = Arena.ofConfined()) {
            MemorySegment state = arena.allocate(capturedStateLayout);
            int written = (int) getMethodHandle("write", writeDescriptor, ccs).invokeExact(state, fd, buf, count);
            if (written < 0) {
                throw new IOException(strerror(errno(state)).reinterpret(Long.MAX_VALUE).getString(0));
            }
            return written;
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

    private static final FunctionDescriptor strerrorDescriptor = FunctionDescriptor.of(C.POINTER,
            C.INT
    );
    public static MemorySegment strerror(int errnum) {
        try {
            return (MemorySegment) getMethodHandle("strerror", strerrorDescriptor).invokeExact(errnum);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private static int errno(MemorySegment state) {

        return (int) errnoHandle.get(state, 0);
    }

    private static MethodHandle getMethodHandle(String methodName, FunctionDescriptor methodDescriptor, Linker.Option ...options) {
        var handle = loadedMethodHandles.get(methodName);
        if (handle == null) {
            MemorySegment address = lookup.find(methodName).orElseThrow();
            handle = Linker.nativeLinker().downcallHandle(address, methodDescriptor, options);
            loadedMethodHandles.put(methodName, handle);
            return handle;
        }
        return handle;
    }
}
