package io.github.nowipi.jio.windows;

import io.github.nowipi.jio.SerialChannel;
import io.github.nowipi.jio.Util;

import java.io.IOException;
import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

import static io.github.nowipi.jio.windows.Kernel32.*;

public final class SerialWindowsChannel implements SerialChannel {

    private final MemorySegment hSerial;
    private static final Kernel32 kernel32 = new Kernel32Impl();

    public SerialWindowsChannel(String portName, int baudRate, byte wordSizeInBits) throws IOException {
        portName = portName.startsWith("\\\\.\\") ? portName : "\\\\.\\" + portName;

        try(var arena = Arena.ofConfined()) {
            hSerial = kernel32.createFileW(
                    arena.allocateFrom(portName, StandardCharsets.UTF_16LE),
                    GENERIC_READ | GENERIC_WRITE,
                    0,
                    MemorySegment.NULL,
                    OPEN_EXISTING,
                    0,
                    MemorySegment.NULL
            );

            if (hSerial.address() == INVALID_HANDLE_VALUE) {
                throw new IOException("Error opening serial port! " + portName);
            }

            MemorySegment dcbSerialParams = DCB.allocate(arena);
            DCB.setDCBlength(dcbSerialParams, (int) dcbSerialParams.byteSize());
            if (kernel32.getCommState(hSerial, dcbSerialParams) == 0) {
                throw new IOException("Failed to get current serial parameters!");
            }

            DCB.setBaudRate(dcbSerialParams, baudRate);
            DCB.setByteSize(dcbSerialParams, wordSizeInBits);
            DCB.setStopBits(dcbSerialParams, ONESTOPBIT);
            DCB.setParity(dcbSerialParams, NOPARITY);

            if (kernel32.setCommState(hSerial, dcbSerialParams) == 0) {
                throw new IOException("Could not set Serial Port parameters");
            }
        }
    }

    @Override
    public int read(ByteBuffer dst) throws IOException {
        MemorySegment segment = Util.toMemorySegment(dst);

        try(Arena arena = Arena.ofConfined()) {
            MemorySegment bytesRead = arena.allocate(Win32.DWORD);
            if (kernel32.readFile(hSerial, segment, dst.remaining(), bytesRead, MemorySegment.NULL) == 0) {
                throw new IOException("Failed to read from serial port!");
            }

            dst.put(segment.get(Win32.BYTE, 0));

            return bytesRead.get(Win32.DWORD, 0);
        }
    }

    @Override
    public int write(ByteBuffer src) throws IOException {
        MemorySegment segment = Util.toMemorySegment(src);

        try(Arena arena = Arena.ofConfined()) {
            MemorySegment bytesWritten = arena.allocate(Win32.DWORD);
            if (kernel32.writeFile(hSerial, segment, src.remaining(), bytesWritten, MemorySegment.NULL) == 0) {
                throw new IOException("Failed to write to serial port!");
            }
            return bytesWritten.get(Win32.DWORD, 0);
        }
    }

    @Override
    public boolean isOpen() {
        return hSerial.address() != INVALID_HANDLE_VALUE;
    }

    @Override
    public void close() {
        kernel32.closeHandle(hSerial);
    }
}
