package io.github.nowipi.jio.windows;

import io.github.nowipi.ffm.processor.Function;
import io.github.nowipi.ffm.processor.Library;

import java.lang.foreign.MemorySegment;

@Library(value = "kernel32")
interface Kernel32 {

    int GENERIC_READ = 0x80000000;
    int GENERIC_WRITE = 0x40000000;
    int OPEN_EXISTING = 3;
    long INVALID_HANDLE_VALUE = -1L;
    int CBR_9600 = 9600;
    byte ONESTOPBIT = 0;
    byte NOPARITY = 0;

    @Function("CreateFileW")
    MemorySegment createFileW(MemorySegment lpFileName,
                              int dwDesiredAccess,
                              int dwShareMode,
                              MemorySegment lpSecurityAttributes,
                              int dwCreationDisposition,
                              int dwFlagsAndAttributes,
                              MemorySegment hTemplateFile);

    @Function("GetCommState")
    int getCommState(MemorySegment hFile, MemorySegment lpDCB);

    @Function("CloseHandle")
    int closeHandle(MemorySegment hObject);

    @Function("SetCommState")
    int setCommState(MemorySegment hFile, MemorySegment lpDCB);

    @Function("WriteFile")
    int writeFile(MemorySegment hFile, MemorySegment lpBuffer, int nNumberOfBytesToWrite, MemorySegment lpNumberOfBytesWritten, MemorySegment lpOverlapped);

    @Function("ReadFile")
    int readFile(MemorySegment hFile, MemorySegment lpBuffer, int nNumberOfBytesToRead, MemorySegment lpNumberOfBytesRead, MemorySegment lpOverlapped);

}
