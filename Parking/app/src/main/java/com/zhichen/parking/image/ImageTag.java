package com.zhichen.parking.image;

import java.io.Serializable;

/**
 * @author linqi
 *
 */
public class ImageTag implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = -7771213351463358305L;

	private String imageUri;
	private String gotoUrl;
	private String conTitle;

	/**
	*
	*/
	public ImageTag() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param imageUri
	 * @param gotoUrl
	 */
	public ImageTag(String imageUri, String gotoUrl) {
		super();
		this.imageUri = imageUri;
		this.gotoUrl = gotoUrl;
	}

	public String getImageUri() {
		return imageUri;
	}

	public void setImageUri(String imageUri) {
		this.imageUri = imageUri;
	}

	public String getGotoUrl() {
		return gotoUrl;
	}

	public void setGotoUrl(String gotoUrl) {
		this.gotoUrl = gotoUrl;
	}

	public String getConTitle() {
		return conTitle;
	}

	public void setConTitle(String conTitle) {
		this.conTitle = conTitle;
	}

	@Override
	public String toString() {
		return "ImageTag [imageUri=" + imageUri + ", gotoUrl=" + gotoUrl + "]";
	}

}
