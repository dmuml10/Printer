package com.dm.npi;

import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;

public class Printer {

	private RandomAccessFile file;
	
	private byte status;
	
	public Printer(String filePath) throws FileNotFoundException {
		initCommunication(filePath);
	}

	private void initCommunication(String filePath) throws FileNotFoundException {
		file = new RandomAccessFile(filePath, "rw");
	}
	
	public List<PrinterStatus> readStatus() throws IOException {
		List<PrinterStatus> statuses = new ArrayList<PrinterStatus>();
		file.write(new byte[] {0x1B, 0x73, 0x01});
		status = file.readByte();
		if (PrinterStatus.COVER_OPEN.equals(status)) {
			statuses.add(PrinterStatus.COVER_OPEN);
		} else if(PrinterStatus.PAPER_OUT.equals(status)) {
			statuses.add(PrinterStatus.PAPER_OUT);
		} else if(PrinterStatus.HEAD_OVERHEAT.equals(status)) {
			statuses.add(PrinterStatus.HEAD_OVERHEAT);
		} else if(PrinterStatus.CUTTER_JAM.equals(status)) {
			statuses.add(PrinterStatus.CUTTER_JAM);
		} else if(PrinterStatus.PAPER_MISSING.equals(status)) {
			statuses.add(PrinterStatus.PAPER_MISSING);
		} else if (PrinterStatus.PAPER_LOW.equals(status)) {
			statuses.add(PrinterStatus.PAPER_LOW);
		} else if (PrinterStatus.PRINTER_BUSY.equals(status)) {
			statuses.add(PrinterStatus.PRINTER_BUSY);
		} else if (PrinterStatus.PRESENTER_FULL.equals(status)) {
			statuses.add(PrinterStatus.PRINTER_BUSY);
		}
		return statuses;
	}

	public void cutPaper() throws IOException {
		file.write(new byte[] { 0x1B, 0x6D });
	}

	public void loadCustomFont(byte[] fontData) throws IOException {
		file.write(new byte[] { 0x1D, 0x26, 0x00 });
	}

	public void loadCustomFont(String fontFilePath) throws IOException {
		byte[] fontData = FileUtils.readFileToByteArray(new File(fontFilePath));
		file.write(new byte[] { 0x1D, 0x26, 0x00 });
		file.write(fontData);
	}

	public void printBitmap(String bitmapFilePath, int width, int height) throws IOException {
		byte[] bitmapData = FileUtils.readFileToByteArray(new File(bitmapFilePath));
		file.write(new byte[] { 0x1B, 0x62, (byte)(width/8) , (byte)(height%256), (byte)(height/256)});
		file.write(bitmapData);
	}
	
	public void printImage(String image, int width, int height) throws IOException {
		BufferedImage originalImage = ImageIO.read(new File(image));
		width = originalImage.getWidth();
		height = originalImage.getHeight();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ImageIO.write(originalImage, "BMP", baos);
		BufferedOutputStream o = new BufferedOutputStream(new FileOutputStream(new File(image)));
		o.write(baos.toByteArray());
		o.flush();
		o.close();
		file.write(new byte[] { 0x1B, 0x62, (byte)(width/8), (byte)(height%256), (byte)(height/256)});
		file.write(baos.toByteArray());
		baos.close();
	}

	public void printNewLine() throws IOException {
		file.write(new byte[] { 0xA });
	}
	
	public void printText(String text) throws IOException {
		file.write(convertText(text));
	}
	
	//[00 <= n <= 24]hex
	public void backFeed(byte n) throws IOException {
		file.write(new byte[] {0x1B, 0x42, n});
	}
	
	public void printLine(int n) throws IOException {
		byte[] array = new byte[n * 54];
		for (int i = 0; i < array.length; i++) {
			array[i] = (byte) 0xFF;
		}
		file.write(array);
	}
	
	public byte[] convertText(String text) {
		byte[] array = new byte[text.length()];
		for (int i = 0; i < array.length; i++) {
			array[i] = (byte) convert(text.charAt(i));
		}
		return array;
	}
	
	/*
	 * Converts to English Georgian Font
	 */
	public int convert(char c) {
		switch (c) {
			case '\n': return 0xA;
	        case '\r': return  0x20;case '!': return  0x21;case '"': return  0x22;case '#': return  0x23;case '$': return  0x24;case '%': return  0x25;case '&': return  0x26;case '\'': return  0x27;case '(': return  0x28;
	        case ')': return  0x29;case '*': return  0x2A;case '+': return  0x2B;case ',': return  0x2C;case '-': return  0x2D;case '.': return  0x2E;case '/': return  0x2F;
	        case '0': return  0x30;case '1': return  0x31;case '2': return  0x32;case '3': return  0x33;case '4': return  0x34;case '5': return  0x35;case '6': return  0x36;case '7': return  0x37;case '8': return  0x38;
	        case '9': return  0x39;case ':': return 0x3A; case ';': return  0x3B;case '<': return  0x3C;case '=': return  0x3D;case '>': return  0x3E;case '?': return  0x3F;
	        case '@': return  0x40;case 'A': return  0x41;case 'B': return  0x42;case 'C': return  0x43;case 'D': return  0x44;case 'E': return  0x45;case 'F': return  0x46;case 'G': return  0x47;case 'H': return  0x48;
	        case 'I': return  0x49;case 'J': return  0x4A;case 'K': return  0x4B;case 'L': return  0x4C;case 'M': return  0x4D;case 'N': return  0x4E;case 'O': return  0x4F;
	        case 'P': return  0x50;case 'Q': return  0x51;case 'R': return  0x52;case 'S': return  0x53;case 'T': return  0x54;case 'U': return  0x55;case 'V': return  0x56;case 'W': return  0x57;case 'X': return  0x58;
	        case 'Y': return  0x59;case 'Z': return  0x5A;case '[': return  0x5B;case '\\': return  0x5C;case ']': return  0x5D;case '^': return  0x5E;case '_': return  0x5F;
	        case '`': return  0x60;case 'a': return  0x61;case 'b': return  0x62;case 'c': return  0x63;case 'd': return  0x64;case 'e': return  0x65;case 'f': return  0x66;case 'g': return  0x67;case 'h': return  0x68;
	        case 'i': return  0x69;case 'j': return  0x6A;case 'k': return  0x6B;case 'l': return  0x6C;case 'm': return  0x6D;case 'n': return  0x6E;case 'o': return  0x6F;
	        case 'p': return  0x70;case 'q': return  0x71;case 'r': return  0x72;case 's': return  0x73;case 't': return  0x74;case 'u': return  0x75;case 'v': return  0x76;case 'w': return  0x77;case 'x': return  0x78;
	        case 'y': return  0x79;case 'z': return  0x7A;case '{': return  0x7B;case '|': return  0x7C;case '}': return  0x7D;case '~': return  0x7E;case '⌂': return  0x7F;
	        case 'ა': return  0x80;case 'ბ': return  0x81;case 'გ': return  0x82;case 'დ': return  0x83;case 'ე': return  0x84;case 'ვ': return  0x85;case 'ზ': return  0x86;case 'თ': return  0x87;case 'ი': return  0x88;
	        case 'კ': return  0x89;case 'ლ': return  0x8A;case 'მ': return  0x8B;case 'ნ': return  0x8C;case 'ო': return  0x8D;case 'პ': return  0x8E;case 'ჟ': return  0x8F;
	        case 'რ': return  0x90;case 'ს': return  0x91;case 'ტ': return  0x92;case 'უ': return  0x93;case 'ფ': return  0x94;case 'ქ': return  0x95;case 'ღ': return  0x96;case 'ყ': return  0x97;case 'შ': return  0x98;
	        case 'ჩ': return  0x99;case 'ც': return  0x9A;case 'ძ': return  0x9B;case 'წ': return  0x9C;case 'ჭ': return  0x9D;case 'ხ': return  0x9E;case 'ჯ': return  0x9F;
	        case 'ჰ': return  0xA0;
			default: return (int)c;
		}
	}
	
	public void selectUserCodePage() throws IOException {
		file.write(new byte[] {0x1B, 0x74, 0x07});
	}
	
	public void powerOff() throws IOException {
		file.write(new byte[] {0x1D, 0x4F});
	}
	
	public void setReleaseUnderline(byte n) throws IOException {
		file.write(new byte[] {0x1B, 0x2D, n});
	}
}