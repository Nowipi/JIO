package io.github.nowipi.jio.linux;



import io.github.nowipi.ffm.processor.Capture;
import io.github.nowipi.ffm.processor.CaptureState;
import io.github.nowipi.ffm.processor.Function;
import io.github.nowipi.ffm.processor.Library;

import java.lang.foreign.*;

@Library
interface LibC {

    int O_RDWR = 2;
    int CS5 = 0x00000000;
    int CS6 = 0x00000010;
    int CS7 = 0x00000020;
    int CS8 = 0x00000030;
    int B9600 = 015;
    int TCSANOW = 0;
    int PARENB = 256;
    int CSTOPB = 64;
    int CSIZE = 48;
    int CRTSCTS = 0x80000000;
    int CREAD = 128;
    int CLOCAL = 2048;
    int VTIME = 5;
    int VMIN = 6;

    @Capture("errno")
    @Function("open")
    int open(MemorySegment pathname, int flags);

    @Capture("errno")
    @Function("tcgetattr")
    int tcgetattr(int fildes, MemorySegment termios_p);

    @Function("cfsetispeed")
    int cfsetispeed(MemorySegment termios_p, int speed);

    @Function("cfsetospeed")
    int cfsetospeed(MemorySegment termios_p, int speed);

    @Capture("errno")
    @Function("tcsetattr")
    int tcsetattr(int filedes, int optional_actions, MemorySegment termios_p);

    @Capture("errno")
    @Function("read")
    int read(int filedes, MemorySegment buf, int nbyte);

    @Capture("errno")
    @Function("write")
    int write(int fd, MemorySegment buf, int count);

    @Capture("errno")
    @Function("close")
    int close(int fd);

    @Function("strerror")
    MemorySegment strerror(int errnum);

    @CaptureState("errno")
    int errno();

    @Function("strlen")
    int strlen(MemorySegment str);
}
