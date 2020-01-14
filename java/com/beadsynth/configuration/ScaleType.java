package com.beadsynth.configuration;

import java.io.Serializable;

import com.beadsynth.util.CommonFunctions;

public enum ScaleType implements Serializable {
	CHROMATIC("Chromatic"), WHOLE_TONE("Whole Tone"), PENTATONIC("Pentatonic"), MAJOR(
			"Major"), HARMONIC_MINOR("Harmonic Minor"), NATURAL_MINOR(
			"Natural Minor"), JAZZ_MINOR("Jazz Minor"), ACOUSTIC_SCALE("Acoustic"),
			ALTERED_DOMINANT("Altered Dominant"),  HUNGARIAN("Hungarian");

	private final String scaleName;
	private static final long serialVersionUID = 2375026592838509648L;
												 
	ScaleType(String scaleName) {
		this.scaleName = scaleName;
	}

	public String scaleName() {
		return scaleName;
	}
	
	public boolean partOfScale(ScaleNote tonic, ScaleNote noteToCheck, boolean ascending) {
		int chromaticStepsApart = noteToCheck.chromaticNumber() - tonic.chromaticNumber();
		return partOfScale(chromaticStepsApart, ascending);
	}

	public boolean partOfScale(int chromaticStepsApart, boolean ascending) {
		int[] allowedChromaticParts = new int[0];
		int[] allowedChromaticPartsDescending = new int[0];
		if (this == CHROMATIC) {
			return true;
		} else if (this == WHOLE_TONE) {
			return (chromaticStepsApart % 2 == 0);
		} else if (this == PENTATONIC) {
			allowedChromaticParts = new int[] { 0, 2, 4, 7, 9 };
			allowedChromaticPartsDescending = allowedChromaticParts;
		} else if (this == MAJOR) {
			allowedChromaticParts = new int[] { 0, 2, 4, 5, 7, 9, 11 };
			allowedChromaticPartsDescending = allowedChromaticParts;
		} else if (this == HARMONIC_MINOR) {
			allowedChromaticParts = new int[] { 0, 2, 3, 5, 7, 8, 11 };
			allowedChromaticPartsDescending = allowedChromaticParts;
//		} else if (this == MELODIC_MINOR) {
//			allowedChromaticParts = new int[] { 0, 2, 3, 5, 7, 9, 11 };
//			allowedChromaticPartsDescending = new int[] { 0, 2, 3, 5, 7, 8, 10 };
		} else if (this == NATURAL_MINOR) {
			allowedChromaticParts = new int[] { 0, 2, 3, 5, 7, 8, 10 };
			allowedChromaticPartsDescending = allowedChromaticParts;
		} else if (this == ACOUSTIC_SCALE) {
			allowedChromaticParts = new int[] { 0, 2, 4, 6, 7, 9, 10 };
			allowedChromaticPartsDescending = allowedChromaticParts;
		} else if (this == JAZZ_MINOR) {
			allowedChromaticParts = new int[] { 0, 2, 3, 5, 7, 9, 11 };
			allowedChromaticPartsDescending = allowedChromaticParts;
		} else if (this == ALTERED_DOMINANT) {
			allowedChromaticParts = new int[] { 0, 1, 3, 4, 6, 8, 10 };
			allowedChromaticPartsDescending = allowedChromaticParts;
		} else if (this == HUNGARIAN) {
			allowedChromaticParts = new int[] {  0, 2, 3, 6, 7, 8, 11 };
			allowedChromaticPartsDescending = allowedChromaticParts;
		}
		
		
		int[] chromaticPartsToConsider;
		if (ascending) {
			chromaticPartsToConsider = allowedChromaticParts;
		} else {
			chromaticPartsToConsider = allowedChromaticPartsDescending;
		}
		
//		for (int i = 0;i<chromaticPartsToConsider.length;i++) {
//			System.out.println("chromatic part: " + chromaticPartsToConsider[i]);
//		}
//		System.out.println("chromaticStepsApart: " + chromaticStepsApart);
		
		
		int mod = CommonFunctions.positiveModulus(chromaticStepsApart, 12);
		if (CommonFunctions.arrayContainsInt(chromaticPartsToConsider, mod)) {
			return true;
		} else {
			return false;
		}

	}
	
	public static ScaleType getFromString(String scaleName) {
		for (ScaleType type : ScaleType.values()) {
			if (scaleName.equals(type.scaleName())) {
				return type;
			}
		}
		return CHROMATIC;
	}

}
