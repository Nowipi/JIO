package io.github.nowipi.jio;

import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.ValueLayout;
import java.nio.*;

public final class Util {

    public static MemorySegment toMemorySegment(Buffer b) {
        if (b.isDirect()) {
            return MemorySegment.ofBuffer(b);
        }

        var arena = Arena.ofAuto();

        return switch (b) {
            case ByteBuffer bb -> {
                ByteBuffer dup = bb.duplicate();
                byte[] temp = new byte[dup.remaining()];
                dup.get(temp);
                yield arena.allocateFrom(ValueLayout.JAVA_BYTE, temp);
            }
            case CharBuffer cb -> {
                CharBuffer dup = cb.duplicate();
                char[] temp = new char[dup.remaining()];
                dup.get(temp);
                yield arena.allocateFrom(ValueLayout.JAVA_CHAR, temp);
            }
            case ShortBuffer sb -> {
                ShortBuffer dup = sb.duplicate();
                short[] temp = new short[dup.remaining()];
                dup.get(temp);
                yield arena.allocateFrom(ValueLayout.JAVA_SHORT, temp);
            }
            case IntBuffer ib -> {
                IntBuffer dup = ib.duplicate();
                int[] temp = new int[dup.remaining()];
                dup.get(temp);
                yield arena.allocateFrom(ValueLayout.JAVA_INT, temp);
            }
            case FloatBuffer fb -> {
                FloatBuffer dup = fb.duplicate();
                float[] temp = new float[dup.remaining()];
                dup.get(temp);
                yield arena.allocateFrom(ValueLayout.JAVA_FLOAT, temp);
            }
            case LongBuffer lb -> {
                LongBuffer dup = lb.duplicate();
                long[] temp = new long[dup.remaining()];
                dup.get(temp);
                yield arena.allocateFrom(ValueLayout.JAVA_LONG, temp);
            }
            case DoubleBuffer db -> {
                DoubleBuffer dup = db.duplicate();
                double[] temp = new double[dup.remaining()];
                dup.get(temp);
                yield arena.allocateFrom(ValueLayout.JAVA_DOUBLE, temp);
            }
        };
    }

}
