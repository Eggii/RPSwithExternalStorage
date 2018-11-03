package com.example.eggii.rps.ExternalStorageInterfaces;

public interface SaveExternalData {
    boolean isExternalStorageReadable();
    void readData(String fileName, Object output);
}
