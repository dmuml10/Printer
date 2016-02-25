package com.dm.npi;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

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

	public void loadUserFont(byte[] fontData) throws IOException {
		out.write(new byte[] { 0x1D, 0x26, 0x00 });
		out.write(fontData);
	}
	
}