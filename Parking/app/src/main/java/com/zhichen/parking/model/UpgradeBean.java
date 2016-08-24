package com.zhichen.parking.model;

public class UpgradeBean {
	
	private boolean needToUpgrade;
	private String version;
	private String versionCode;
	private String uri;
	private String md5;	
	private String nestupdatepromt;
	
	public UpgradeBean(){}

	public boolean isNeedToUpgrade() {
		return needToUpgrade;
	}

	public void setNeedToUpgrade(boolean needToUpgrade) {
		this.needToUpgrade = needToUpgrade;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getVersionCode() {
		return versionCode;
	}

	public void setVersionCode(String versionCode) {
		this.versionCode = versionCode;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public String getMd5() {
		return md5;
	}

	public void setMd5(String md5) {
		this.md5 = md5;
	}

	public String getNestupdatepromt() {
		return nestupdatepromt;
	}

	public void setNestupdatepromt(String nestupdatepromt) {
		this.nestupdatepromt = nestupdatepromt;
	}

	@Override
	public String toString() {
		return "UpgradeEntity [needToUpgrade=" + needToUpgrade + ", version="
				+ version + ", versionCode=" + versionCode + ", uri=" + uri
				+ ", md5=" + md5 + ", nestupdatepromt=" + nestupdatepromt + "]";
	}
		
	
}
