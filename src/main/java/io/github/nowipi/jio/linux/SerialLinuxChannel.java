package io.github.nowipi.jio.linux;

import io.github.nowipi.jio.SerialChannel;
import io.github.nowipi.jio.Util;

import java.io.IOException;
import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.ValueLayout;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

import static io.github.nowipi.jio.linux.LibC.*;

public class SerialLinuxChannel implements SerialChannel {

    private final int fd;
    private static final LibC libC = new LibCImpl();

    public SerialLinuxChannel(String portName, byte wordSizeInBits) throws IOException {

        try(var arena = Arena.ofConfined()) {

            fd = libC.open(arena.allocateFrom(portName, StandardCharsets.US_ASCII), O_RDWR);
            if (fd < 0) {
                MemorySegment errorMessage = libC.strerror(libC.errno());
                throw new IOException("Failed to open port " + errorMessage.reinterpret(libC.strlen(errorMessage)));
            }

            MemorySegment tty = Termios.allocate(arena);
            if (libC.tcgetattr(fd, tty) != 0) {
                throw new IOException("Failed to read existing settings");
            }

            int c_cflag = Termios.c_cflag(tty);

            c_cflag &= ~PARENB;
            c_cflag &= ~CSTOPB;
            c_cflag &= ~CSIZE;
            c_cflag |= switch (wordSizeInBits) {
                case 5 -> CS5;
                case 6 -> CS6;
                case 7 -> CS7;
                case 8 -> CS8;
                default -> throw new IllegalStateException("Unexpected value: " + wordSizeInBits);
            };
            c_cflag &= ~CRTSCTS;
            c_cflag |= CREAD | CLOCAL;

            Termios.c_cflag(tty, c_cflag);
            Termios.c_lflag(tty, 0);
            Termios.c_iflag(tty, 0);
            Termios.c_oflag(tty, 0);

            Termios.c_cc(tty, VTIME, (byte) 10);
            Termios.c_cc(tty, VMIN, (byte) 0);

            libC.cfsetispeed(tty, B9600);
            libC.cfsetospeed(tty, B9600);

            if (libC.tcsetattr(fd, TCSANOW, tty) != 0) {
                throw new IOException("Failed set settings");
            }
        }

    }

    @Override
    public int read(ByteBuffer dst) {
        MemorySegment segment = Util.toMemorySegment(dst);
        int read =  libC.read(fd, segment, dst.remaining());

        if (!dst.isDirect()) {
            // copy bytes back from native segment to Java buffer
            byte[] temp = segment.toArray(ValueLayout.JAVA_BYTE);
            dst.put(temp);
        } else {
            // direct buffer already updated
            dst.position(dst.position() + read);
        }
        return read;
    }

    @Override
    public int write(ByteBuffer src) {
        MemorySegment segment = Util.toMemorySegment(src);

        return libC.write(fd, segment, src.remaining());
    }

    @Override
    public boolean isOpen() {
        return fd >= 0;
    }

    @Override
    public void close() {
        libC.close(fd);
    }
}
