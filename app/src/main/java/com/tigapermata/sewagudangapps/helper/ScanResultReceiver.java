package com.tigapermata.sewagudangapps.helper;

public interface ScanResultReceiver {
    public void scanResultData(String codeFormat, String codeContent);

    public void scanResultData(NoScanResultException noScanData);
}