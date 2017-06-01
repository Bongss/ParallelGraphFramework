package atomic;

import java.util.function.DoubleBinaryOperator;

import static java.lang.Double.doubleToRawLongBits;
import static java.lang.Double.longBitsToDouble;

public class AtomicDoubleArray implements java.io.Serializable {
    private static final long serialVersionUID = -2308431214976778248L;

    private final long[] array;

    private long checkedByteOffset(int i) {
        if (i < 0 || i >= array.length) {
            throw new IndexOutOfBoundsException("index " + i);
        }

        return byteOffset(i);
    }

    private static long byteOffset(int i) {
        return ((long) i << shift) + base;
    }

    public AtomicDoubleArray(int length) {
        array = new long[length];
    }

    public final int length() {
        return array.length;
    }

    public final double get(int i) {
        return longBitsToDouble(getRaw(checkedByteOffset(i)));
    }

    public final double asyncGet(int i) {
        return longBitsToDouble(array[i]);
    }

    private long getRaw(long offset) {
        return unsafe.getLongVolatile(array, offset);
    }

    public final void set(int i, double newValue) {
        long next = doubleToRawLongBits(newValue);
        unsafe.putLongVolatile(array, checkedByteOffset(i), next);
    }

    public final void asyncSet(int i, double newValue) {
        array[i] = doubleToRawLongBits(newValue);
    }

    public final void lazySet(int i, double newValue) {
        long next = doubleToRawLongBits(newValue);
        unsafe.putOrderedLong(array, checkedByteOffset(i), next);
    }

    public final boolean compareAndSet(int i, double expect, double update) {
        return compareAndSetRaw(checkedByteOffset(i), doubleToRawLongBits(expect), doubleToRawLongBits(update));
    }

    private boolean compareAndSetRaw(long offset, long expect, long update) {
        return unsafe.compareAndSwapLong(array, offset, expect, update);
    }

    public final double getAndAccumulate(int i, double x, DoubleBinaryOperator accumulatorFunction) {
        long offset = checkedByteOffset(i);
        double prev, next;
        do {
            prev = longBitsToDouble(getRaw(offset));
            next = accumulatorFunction.applyAsDouble(prev, x);
        }
        while (!compareAndSetRaw(offset, doubleToRawLongBits(prev), doubleToRawLongBits(next)));
        return prev;
    }

    public final double asyncGetAndAccumulate(int i, double x, DoubleBinaryOperator accumulatorFunction) {
        double prev, next;
        prev = longBitsToDouble(array[i]);
        next = accumulatorFunction.applyAsDouble(prev, x);
        array[i] = doubleToRawLongBits(next);
        return prev;
    }

    public String toString() {
        int iMax = array.length - 1;
        if (iMax == -1) {
            return "[]";
        }

        StringBuilder b = new StringBuilder();
        b.append('[');
        for (int i = 0; ; i++) {
            b.append(longBitsToDouble(getRaw(byteOffset(i))));
            if (i == iMax) {
                return b.append(']').toString();
            }
            b.append(',').append(' ');
        }
    }

    // Unsafe mechanics
    private static final sun.misc.Unsafe unsafe = getUnsafe();
    private static final int base = unsafe.arrayBaseOffset(long[].class);
    private static final int shift;

    static {
        int scale = unsafe.arrayIndexScale(long[].class);
        if ((scale & (scale - 1)) != 0) {
            throw new Error("data type scale not a power of two");
        }
        shift = 31 - Integer.numberOfLeadingZeros(scale);
    }

    /**
     * Returns a sun.misc.Unsafe.  Suitable for use in a 3rd party package.
     * Replace with a simple call to Unsafe.getUnsafe when integrating
     * into a jdk.
     *
     * @return a sun.misc.Unsafe
     */
    private static sun.misc.Unsafe getUnsafe() {
        try {
            return sun.misc.Unsafe.getUnsafe();
        }
        catch (SecurityException se) {
            try {
                return java.security.AccessController.doPrivileged(new java.security.PrivilegedExceptionAction<sun.misc.Unsafe>() {
                    public sun.misc.Unsafe run()
                            throws Exception {
                        java.lang.reflect.Field f = sun.misc.Unsafe.class.getDeclaredField("theUnsafe");
                        f.setAccessible(true);
                        return (sun.misc.Unsafe) f.get(null);
                    }
                });
            }
            catch (java.security.PrivilegedActionException e) {
                throw new RuntimeException("Could not initialize intrinsics", e.getCause());
            }
        }
    }
}

