package com.dm.npi;

public enum PrinterStatus {
	
	// Printer Errors
	
	COVER_OPEN(0b00000010),
	
	PAPER_OUT(0b00000100),
	
	PRESENTER_JAM(0b00001000),
	
	HEAD_OVERHEAT(0b00001000),
	
	CUTTER_JAM(0b00010000),
	
	PAPER_MISSING(0b00100000),
	
	JAM(0b00011000),
	
	// Printer Warnings
	
	PAPER_LOW(0b00000001),
	
	PRESENTER_FULL(0b01000000),
	
	PRINTER_BUSY(0b10000000);
	
	private PrinterStatus(int status) {
		this.status = status;
	}
	
	private int status;

	public int getStatus() {
		return status;
	}

	public boolean equals(int statusByte) {
		return status == statusByte;
	}
	
	
}