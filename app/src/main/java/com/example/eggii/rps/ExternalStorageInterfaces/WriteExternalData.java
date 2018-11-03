package com.example.eggii.rps.ExternalStorageInterfaces;

public interface WriteExternalData {
    boolean isExternalStorageWritable();
    boolean chkPermission(String permission);
    void writeData(String fileName, String text);
}
