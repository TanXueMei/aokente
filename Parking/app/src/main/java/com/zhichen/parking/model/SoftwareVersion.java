package com.zhichen.parking.model;

/**
 * 版本更新
 */
public class SoftwareVersion {

	String release_date;
	String release_notes;
	int version_code ;
	String version_name ;

	public int getVersion_code() {
		return version_code;
	}

	public void setVersion_code(int version_code) {
		this.version_code = version_code;
	}

	public String getVersion_name() {
		return version_name;
	}

	public void setVersion_name(String version_name) {
		this.version_name = version_name;
	}

	public String getRelease_date() {
		return release_date;
	}

	public void setRelease_date(String release_date) {
		this.release_date = release_date;
	}

	public String getRelease_notes() {
		return release_notes;
	}

	public void setRelease_notes(String release_notes) {
		this.release_notes = release_notes;
	}
}
