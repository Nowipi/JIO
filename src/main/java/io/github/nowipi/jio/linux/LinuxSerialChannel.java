package io.github.nowipi.jio.linux;

import io.github.nowipi.jio.SerialChannel;
import io.github.nowipi.jio.Util;

import java.io.IOException;
import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;
import java.nio.ByteBuffer;

import static io.github.nowipi.jio.linux.LibC.*;

public class LinuxSerialChannel implements SerialChannel {

    private final int fd;

    public LinuxSerialChannel(String portName, int baudRate, byte wordSizeInBits) throws IOException {

        try(var arena = Arena.ofConfined()) {

            fd = open(arena.allocateFrom(portName), O_RDWR);

            MemorySegment tty = Termios.allocate(arena);
            if (tcgetattr(fd, tty) != 0) {
                throw new IOException("Failed to read existing settings");
            }

            Termios.c_cflag(tty, Termios.c_cflag(tty) | switch (wordSizeInBits) {
                case 5 -> CS5;
                case 6 -> CS6;
                case 7 -> CS7;
                case 8 -> CS8;
                default -> throw new IllegalStateException("Unexpected value: " + wordSizeInBits);
            });

            cfsetispeed(tty, B9600);
            cfsetospeed(tty, B9600);

            if (tcsetattr(fd, TCSANOW, tty) != 0) {
                throw new IOException("Failed set settings");
            }
        }

    }

    @Override
    public int read(ByteBuffer dst) throws IOException {
        MemorySegment segment = Util.toMemorySegment(dst);
        return LibC.read(fd, segment, (int) segment.byteSize());
    }

    @Override
    public int write(ByteBuffer src) throws IOException {
        MemorySegment segment = Util.toMemorySegment(src);

        return LibC.write(fd, segment, (int) segment.byteSize());
    }

    @Override
    public boolean isOpen() {
        return fd < 0;
    }

    @Override
    public void close() throws IOException {
        LibC.close(fd);
    }
}
