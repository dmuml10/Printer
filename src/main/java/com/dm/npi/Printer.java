package com.dm.npi;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

public class Printer {

	private final String devicePath = "/dev/usb/lp0";

	private BufferedOutputStream out;

	private BufferedInputStream in;

	public Printer() throws FileNotFoundException {
		initDataReadCommunication();
		initDataWriteCommunication();
	}

	private void initDataWriteCommunication() throws FileNotFoundException {
		FileOutputStream fos = new FileOutputStream(devicePath);
		out = new BufferedOutputStream(fos);
	}

	private void initDataReadCommunication() throws FileNotFoundException {
		FileInputStream fis = new FileInputStream(devicePath);
		in = new BufferedInputStream(fis);
	}

	public void cutPaper() throws IOException {
		out.write(new byte[] { 0x1B, 0x6D });
	}

	public void loadCustomFont(byte[] fontData) throws IOException {
		out.write(new byte[] { 0x1D, 0x26, 0x00 });
		out.write(fontData);
	}

	public void loadCustomFont(String fontFilePath) throws IOException {
		byte[] fontData = FileUtils.readFileToByteArray(new File(fontFilePath));
		out.write(new byte[] { 0x1D, 0x26, 0x00 });
		out.write(fontData);
	}

}