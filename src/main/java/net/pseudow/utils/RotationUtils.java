package net.pseudow.utils;

public class RotationUtils {
    public static byte getCompressedAngle(float angle) {
        return (byte)(int)(angle * 256f / 360f);
    }
}
