package io.github.nowipi.jio.windows;

import java.lang.foreign.FunctionDescriptor;
import java.lang.foreign.Linker;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.SymbolLookup;
import java.lang.invoke.MethodHandle;
import java.util.HashMap;
import java.util.Map;

public final class Kernel32 {

    public static final SymbolLookup lookup;

    public static final int GENERIC_READ = 0x80000000;
    public static final int GENERIC_WRITE = 0x40000000;
    public static final int OPEN_EXISTING = 3;
    public static final long INVALID_HANDLE_VALUE = -1L;
    public static final int CBR_9600 = 9600;
    public static final byte ONESTOPBIT = 0;
    public static final byte NOPARITY = 0;

    static {
        lookup = SymbolLookup.libraryLookup(System.mapLibraryName("kernel32"), Win32.arena);
        loadedMethodHandles = new HashMap<>();
    }

    private static final FunctionDescriptor createFileWDescriptor = FunctionDescriptor.of(Win32.HANDLE,
            Win32.LPCWSTR,
            Win32.DWORD,
            Win32.DWORD,
            Win32.ADDRESS,
            Win32.DWORD,
            Win32.DWORD,
            Win32.HANDLE
    );
    public static MemorySegment createFileW(MemorySegment lpFileName,
                                   int dwDesiredAccess,
                                   int dwShareMode,
                                   MemorySegment lpSecurityAttributes,
                                   int dwCreationDisposition,
                                   int dwFlagsAndAttributes,
                                   MemorySegment hTemplateFile) {
        try {
            return (MemorySegment) getMethodHandle("CreateFileW", createFileWDescriptor).invokeExact(lpFileName, dwDesiredAccess, dwShareMode, lpSecurityAttributes, dwCreationDisposition, dwFlagsAndAttributes,
                    hTemplateFile);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private static final FunctionDescriptor getCommStateDescriptor = FunctionDescriptor.of(Win32.BOOL,
            Win32.HANDLE,
            Win32.ADDRESS
    );
    public static int getCommState(MemorySegment hFile, MemorySegment lpDCB) {
        try {
            return (int) getMethodHandle("GetCommState", getCommStateDescriptor).invokeExact(hFile, lpDCB);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private static final FunctionDescriptor closeHandleDescriptor = FunctionDescriptor.of(Win32.BOOL,
            Win32.HANDLE
    );
    public static int closeHandle(MemorySegment hObject) {
        try {
            return (int) getMethodHandle("CloseHandle", closeHandleDescriptor).invokeExact(hObject);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private static final FunctionDescriptor setCommStateDescriptor = FunctionDescriptor.of(Win32.BOOL,
            Win32.HANDLE,
            Win32.ADDRESS
    );
    public static int setCommState(MemorySegment hFile, MemorySegment lpDCB) {
        try {
            return (int) getMethodHandle("SetCommState", setCommStateDescriptor).invokeExact(hFile, lpDCB);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private static final FunctionDescriptor writeFileDescriptor = FunctionDescriptor.of(Win32.BOOL,
            Win32.HANDLE,
            Win32.ADDRESS,
            Win32.DWORD,
            Win32.ADDRESS,
            Win32.ADDRESS
    );
    public static int writeFile(MemorySegment hFile, MemorySegment lpBuffer, int nNumberOfBytesToWrite, MemorySegment lpNumberOfBytesWritten, MemorySegment lpOverlapped) {
        try {
            return (int) getMethodHandle("WriteFile", writeFileDescriptor).invokeExact(hFile, lpBuffer, nNumberOfBytesToWrite, lpNumberOfBytesWritten, lpOverlapped);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private static final FunctionDescriptor readFileDescriptor = FunctionDescriptor.of(Win32.BOOL,
            Win32.HANDLE,
            Win32.ADDRESS,
            Win32.DWORD,
            Win32.ADDRESS,
            Win32.ADDRESS
    );
    public static int readFile(MemorySegment hFile, MemorySegment lpBuffer, int nNumberOfBytesToRead, MemorySegment lpNumberOfBytesRead, MemorySegment lpOverlapped) {
        try {
            return (int) getMethodHandle("ReadFile", readFileDescriptor).invokeExact(hFile, lpBuffer, nNumberOfBytesToRead, lpNumberOfBytesRead, lpOverlapped);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private static final Map<String, MethodHandle> loadedMethodHandles;
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
