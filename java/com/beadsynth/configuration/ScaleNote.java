package com.beadsynth.configuration;

import java.io.Serializable;

import com.beadsynth.util.CommonFunctions;

public enum ScaleNote implements Serializable {
	A_FLAT ("A Flat", "tonic_a_flat", 11, "A", 1),
	A ("A", "tonic_a", 0, "A", 0),
	A_SHARP ("A Sharp", "tonic_a_sharp", 1, "A", 2),
	B_FLAT ("B Flat", "tonic_b_flat", 1, "B", 1),
	B ("B", "tonic_b", 2, "B", 0),
	B_SHARP ("B Sharp", "tonic_b_sharp", 3, "B", 2),
	C_FLAT ("C Flat", "tonic_c_flat", 2, "C", 1),
    C ("C", "tonic_c", 3, "C", 0),
    C_SHARP   ("C Sharp", "tonic_c_sharp", 4, "C", 2),
    D_FLAT    ("D Flat", "tonic_d_flat", 4, "D", 1),
    D   ("D", "tonic_d", 5, "D", 0),
    D_SHARP ("D Sharp", "tonic_d_sharp", 6, "D", 2),
    E_FLAT  ("E Flat", "tonic_e_flat", 6, "E", 1),
    E ("E", "tonic_e", 7, "E", 0),
    E_SHARP ("E Sharp", "tonic_e_sharp", 8, "E", 2),
    F_FLAT ("F Flat", "tonic_f_flat", 7, "F", 1),
	F ("F", "tonic_f", 8, "F", 0),
	F_SHARP ("F Sharp", "tonic_f_sharp", 9, "F", 2),
	G_FLAT ("G Flat", "tonic_g_flat", 9, "G", 1),
	G ("G", "tonic_g", 10, "G", 0),
	G_SHARP ("G_SHARP", "tonic_g_sharp", 11, "G", 2);

	public static int NATURAL = 0;
	public static int FLAT = 1;
	public static int SHARP = 2;
	
    private final String noteName;   
    private final String imageFileName;
    private final int chromaticNumber;
    private final String basicNote;
    private final int modifier;
    
	private static final long serialVersionUID = 3292835650982263498L; 
												    
    ScaleNote(String noteName, String imageFileName, int chromaticNumber, String basicNote, int modifier) {
        this.noteName = noteName;
        this.imageFileName = imageFileName;
        this.chromaticNumber = chromaticNumber;
        this.basicNote = basicNote;
        this.modifier = modifier;
    }
    public String noteName() { return noteName; }
    public String imageFileName() { return imageFileName; }
    public int chromaticNumber() { return chromaticNumber; }
    public String basicNote() { return basicNote; }
    public int modifier() { return modifier; }
    
    public static ScaleNote getDefaultByChromaticNumber(int number) {
    	number = CommonFunctions.positiveModulus(number, 12);
    	if (number == 0) {
    		return ScaleNote.A;
    	} else if (number == 1) {
    		return ScaleNote.B_FLAT;
    	} else if (number == 2) {
    		return ScaleNote.B;
    	} else if (number == 3) {
    		return ScaleNote.C;
    	} else if (number == 4) {
    		return ScaleNote.D_FLAT;
    	} else if (number == 5) {
    		return ScaleNote.D;
    	} else if (number == 6) {
    		return ScaleNote.E_FLAT;
    	} else if (number == 7) {
    		return ScaleNote.E;
    	} else if (number == 8) {
    		return ScaleNote.F;
    	} else if (number == 9) {
    		return ScaleNote.G_FLAT;
    	} else if (number == 10) {
    		return ScaleNote.G;
    	} else if (number == 11) {
    		return ScaleNote.A_FLAT;
    	} else {
    		return null;
    	}
    }
    
    public static ScaleNote getByNoteAndModifier(String noteString, int modifier) {
    	for (ScaleNote note : ScaleNote.values()) {
    		if (noteString.equals(note.basicNote()) && modifier == note.modifier()) {
    			return note;
    		}
    	}
    	return A;
    }
    
    public static ScaleNote getByName(String noteName) {
    	for (ScaleNote note : ScaleNote.values()) {
    		if (noteName.equals(note.noteName())) {
    			return note;
    		}
    	}
    	return A;
    }
}
