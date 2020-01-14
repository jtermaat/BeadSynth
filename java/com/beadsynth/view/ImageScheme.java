package com.beadsynth.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;

import com.beadsynth.util.CommonFunctions;

public class ImageScheme {
	private Bitmap beadRegularImage;
	private String beadRegularImageFilename;
	private Bitmap beadSelectedImage;
	private String beadSelectedImageFilename;
	private Bitmap beadLockedImage;
	private String beadLockedImageFilename;
	private Bitmap beadConnectingImage;
	private String beadConnectingImageFilename;
	private Bitmap beadGlowImage;
	private String beadGlowImageFilename;

	private Bitmap keyboardBlackKeyImage;
	private String keyboardBlackKeyImageFilename;
	private Bitmap keyboardBlackKeySelectedImage;
	private String keyboardBlackKeySelectedImageFilename;
	private Bitmap keyboardWhiteKeyImage;
	private String keyboardWhiteKeyImageFilename;
	private Bitmap keyboardWhiteKeySelectedImage;
	private String keyboardWhiteKeySelectedImageFilename;
	
	private int waveColor;

//	public ImageScheme() {
//
//	}
	
	public ImageScheme(Context context, String beadRegularImageFilename, String beadSelectedImageFilename,
			String beadLockedImageFilename, String beadConnectingImageFilename, String beadGlowImageFilename,
			String keyboardBlackKeyImageFilename, String keyboardBlackKeySelectedImageFilename,
			String keyboardWhiteKeyImageFilename, String keyboardWhiteKeySelectedImageFilename, int waveColor) {
		this.beadRegularImageFilename = beadRegularImageFilename;
		this.beadSelectedImageFilename = beadSelectedImageFilename;
		this.beadLockedImageFilename = beadLockedImageFilename;
		this.beadConnectingImageFilename = beadConnectingImageFilename;
		this.setBeadGlowImageFilename(beadGlowImageFilename);
		this.keyboardBlackKeyImageFilename = keyboardBlackKeyImageFilename;
		this.keyboardBlackKeySelectedImageFilename = keyboardBlackKeySelectedImageFilename;
		this.keyboardWhiteKeyImageFilename = keyboardWhiteKeyImageFilename;
		this.keyboardWhiteKeySelectedImageFilename = keyboardWhiteKeySelectedImageFilename;
		this.beadRegularImage = CommonFunctions.loadBitmap(beadRegularImageFilename, context);
		this.beadSelectedImage = CommonFunctions.loadBitmap(beadSelectedImageFilename, context);
		this.beadLockedImage = CommonFunctions.loadBitmap(beadLockedImageFilename, context);
		this.beadConnectingImage = CommonFunctions.loadBitmap(beadConnectingImageFilename, context);
		this.setBeadGlowImage(CommonFunctions.loadBitmap(beadGlowImageFilename, context));
		this.keyboardBlackKeyImage = CommonFunctions.loadBitmap(keyboardBlackKeyImageFilename, context);
		this.keyboardBlackKeySelectedImage = CommonFunctions.loadBitmap(keyboardBlackKeySelectedImageFilename, context);
		this.keyboardWhiteKeyImage = CommonFunctions.loadBitmap(keyboardWhiteKeyImageFilename, context);
		this.keyboardWhiteKeySelectedImage = CommonFunctions.loadBitmap(this.keyboardWhiteKeySelectedImageFilename, context);
		this.setWaveColor(waveColor);
	}

//	public ImageScheme(Bitmap beadRegularImage, Bitmap beadSelectedImage,
//			Bitmap beadLockedImage, Bitmap beadConnectingImage,
//			Bitmap keyboardBlackKeyImage, Bitmap keyboardBlackKeySelectedImage,
//			Bitmap keyboardWhiteKeyImage, Bitmap keyboardWhiteKeySelectedImage) {
//		this.beadRegularImage = beadRegularImage;
//		this.beadSelectedImage = beadSelectedImage;
//		this.beadLockedImage = beadLockedImage;
//		this.beadConnectingImage = beadConnectingImage;
//		this.keyboardBlackKeyImage = keyboardBlackKeyImage;
//		this.keyboardBlackKeySelectedImage = keyboardBlackKeySelectedImage;
//		this.keyboardWhiteKeyImage = keyboardWhiteKeyImage;
//		this.keyboardWhiteKeySelectedImage = keyboardWhiteKeySelectedImage;
//	}

	public Bitmap getBeadRegularImage() {
		return beadRegularImage;
	}

	public void setBeadRegularImage(Bitmap beadRegularImage) {
		this.beadRegularImage = beadRegularImage;
	}

	public Bitmap getBeadSelectedImage() {
		return beadSelectedImage;
	}

	public void setBeadSelectedImage(Bitmap beadSelectedImage) {
		this.beadSelectedImage = beadSelectedImage;
	}

	public Bitmap getBeadLockedImage() {
		return beadLockedImage;
	}

	public void setBeadLockedImage(Bitmap beadLockedImage) {
		this.beadLockedImage = beadLockedImage;
	}

	public Bitmap getBeadConnectingImage() {
		return beadConnectingImage;
	}

	public void setBeadConnectingImage(Bitmap beadConnectingImage) {
		this.beadConnectingImage = beadConnectingImage;
	}

	public Bitmap getKeyboardBlackKeyImage() {
		return keyboardBlackKeyImage;
	}

	public void setKeyboardBlackKeyImage(Bitmap keyboardBlackKeyImage) {
		this.keyboardBlackKeyImage = keyboardBlackKeyImage;
	}

	public Bitmap getKeyboardBlackKeySelectedImage() {
		return keyboardBlackKeySelectedImage;
	}

	public void setKeyboardBlackKeySelectedImage(
			Bitmap keyboardBlackKeySelectedImage) {
		this.keyboardBlackKeySelectedImage = keyboardBlackKeySelectedImage;
	}

	public Bitmap getKeyboardWhiteKeyImage() {
		return keyboardWhiteKeyImage;
	}

	public void setKeyboardWhiteKeyImage(Bitmap keyboardWhiteKeyImage) {
		this.keyboardWhiteKeyImage = keyboardWhiteKeyImage;
	}

	public Bitmap getKeyboardWhiteKeySelectedImage() {
		return keyboardWhiteKeySelectedImage;
	}

	public void setKeyboardWhiteKeySelectedImage(
			Bitmap keyboardWhiteKeySelectedImage) {
		this.keyboardWhiteKeySelectedImage = keyboardWhiteKeySelectedImage;
	}

	public static ImageScheme getDefaultImageScheme(int imageSchemeNum,
			Context context) {
		if (imageSchemeNum == 0) {
			ImageScheme imageScheme = new ImageScheme(context, 
					"newred_unlockedv2",
//					"bead_top_regular",
					"bead_top_regular",
					"bead_red_locked",
					"bead_connected",
					"beadsnap_glow",
					"keyboard_red",
					"keyboard_red_pressed",
					"keyboard_white",
					"keyboard_white_pressed",
					Color.RED);
			return imageScheme;
		} else if (imageSchemeNum == 1) {
			ImageScheme imageScheme = new ImageScheme(context, 
//					"bead_middle_regular",
					"newgreen_unlockedv2",
					"bead_middle_regular",
					"bead_green_locked",
					"bead_connected",
					"beadsnap_glow",
					"keyboard_green",
					"keyboard_green_pressed",
					"keyboard_white",
					"keyboard_white_pressed",
					Color.GREEN);
			return imageScheme;			
		} else if (imageSchemeNum == 2) {
			ImageScheme imageScheme = new ImageScheme(context,
//					"bead_bottom_regular",
					"newblue_unlockedv2",
					"bead_bottom_regular",
					"bead_blue_locked",
					"bead_connected",
					"beadsnap_glow",
					"keyboard_blue",
					"keyboard_blue_pressed",
					"keyboard_white",
					"keyboard_white_pressed",
					Color.BLUE);
			return imageScheme;	
		} else {
			return null;
		}

	}

	public String getBeadRegularImageFilename() {
		return beadRegularImageFilename;
	}

	public void setBeadRegularImageFilename(String beadRegularImageFilename) {
		this.beadRegularImageFilename = beadRegularImageFilename;
	}

	public String getBeadSelectedImageFilename() {
		return beadSelectedImageFilename;
	}

	public void setBeadSelectedImageFilename(String beadSelectedImageFilename) {
		this.beadSelectedImageFilename = beadSelectedImageFilename;
	}

	public String getBeadLockedImageFilename() {
		return beadLockedImageFilename;
	}

	public void setBeadLockedImageFilename(String beadLockedImageFilename) {
		this.beadLockedImageFilename = beadLockedImageFilename;
	}

	public String getBeadConnectingImageFilename() {
		return beadConnectingImageFilename;
	}

	public void setBeadConnectingImageFilename(
			String beadConnectingImageFilename) {
		this.beadConnectingImageFilename = beadConnectingImageFilename;
	}

	public String getKeyboardBlackKeyImageFilename() {
		return keyboardBlackKeyImageFilename;
	}

	public void setKeyboardBlackKeyImageFilename(
			String keyboardBlackKeyImageFilename) {
		this.keyboardBlackKeyImageFilename = keyboardBlackKeyImageFilename;
	}

	public String getKeyboardBlackKeySelectedImageFilename() {
		return keyboardBlackKeySelectedImageFilename;
	}

	public void setKeyboardBlackKeySelectedImageFilename(
			String keyboardBlackKeySelectedImageFilename) {
		this.keyboardBlackKeySelectedImageFilename = keyboardBlackKeySelectedImageFilename;
	}

	public String getKeyboardWhiteKeyImageFilename() {
		return keyboardWhiteKeyImageFilename;
	}

	public void setKeyboardWhiteKeyImageFilename(
			String keyboardWhiteKeyImageFilename) {
		this.keyboardWhiteKeyImageFilename = keyboardWhiteKeyImageFilename;
	}

	public String getKeyboardWhiteKeySelectedImageFilename() {
		return keyboardWhiteKeySelectedImageFilename;
	}

	public void setKeyboardWhiteKeySelectedImageFilename(
			String keyboardWhiteKeySelectedImageFilename) {
		this.keyboardWhiteKeySelectedImageFilename = keyboardWhiteKeySelectedImageFilename;
	}

	public int getWaveColor() {
		return waveColor;
	}

	public void setWaveColor(int waveColor) {
		this.waveColor = waveColor;
	}

	public Bitmap getBeadGlowImage() {
		return beadGlowImage;
	}

	public void setBeadGlowImage(Bitmap beadGlowImage) {
		this.beadGlowImage = beadGlowImage;
	}

	public String getBeadGlowImageFilename() {
		return beadGlowImageFilename;
	}

	public void setBeadGlowImageFilename(String beadGlowImageFilename) {
		this.beadGlowImageFilename = beadGlowImageFilename;
	}

}
