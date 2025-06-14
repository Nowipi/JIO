package io.github.nowipi.jio;

import io.github.nowipi.jio.linux.SerialLinuxChannel;
import io.github.nowipi.jio.windows.SerialWindowsChannel;

import java.io.IOException;
import java.nio.ByteBuffer;

final class Main {
    public static void main(String[] args) {
        try(SerialChannel channel = new SerialLinuxChannel("COM3", (byte) 8)) {
            var buffer = ByteBuffer.allocate(1);
            buffer.put((byte) 0xFF);
            channel.write(buffer);
            buffer.flip();
            channel.read(buffer);
            System.out.println(buffer.get(0));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
