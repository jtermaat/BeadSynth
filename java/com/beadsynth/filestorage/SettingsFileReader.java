package com.beadsynth.filestorage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Rect;

import com.beadsynth.configuration.ScaleNote;
import com.beadsynth.configuration.ScaleType;
import com.beadsynth.keyboard.DynamicKeyboard;
import com.beadsynth.keyboard.KeyboardSnapshot;
import com.beadsynth.physics.Bead;
import com.beadsynth.physics.BeadSnapshot;
import com.beadsynth.physics.BeadString;
import com.beadsynth.physics.RecordableBead;
import com.beadsynth.physics.RecordableWaveString;
import com.beadsynth.physics.Velocity;
import com.beadsynth.physics.WaveStringSnapshot;
import com.beadsynth.util.CommonFunctions;
import com.beadsynth.view.ImageScheme;

public class SettingsFileReader {
	// Parts read from this class
	double bpm;
	ScaleType scaleType;
	ScaleNote scaleNote;
	List<RecordableWaveString> strings;
	int numSnapshots;
	BeadIndexer beadIndexer;

	// Hardcoded imageschemes, lazy I know.
	List<ImageScheme> orderedImageSchemes;

	// Internal work
	int numStrings;
	int numBeads;
	List<RecordableBead> beads;
	List<BeadSnapshot> allBeadSnapshots;
	List<DynamicKeyboard> orderedKeyboards;
	int numKeyboardSnapshots;
	String bgFileName;
	Context context;

	public SettingsFileReader() {
		beads = new ArrayList<RecordableBead>();
		allBeadSnapshots = new ArrayList<BeadSnapshot>();
		strings = new ArrayList<RecordableWaveString>();
		orderedKeyboards = new ArrayList<DynamicKeyboard>();
		orderedImageSchemes = new ArrayList<ImageScheme>();
	}

	public SavedSettings readFile(String filename, Context context)
			throws IOException {
		this.context = context;
		String data = getStringDataFromFile(filename, context);
		System.out.println("ALL DATA: ");
		String[] dataParts = data.split(";;");
		for (String dataPart : dataParts) {
			System.out.println("dataPart: " + dataPart);
		}
		readDataString(data);
		SavedSettings settings = new SavedSettings();
		settings.setBpm(bpm);
		settings.setScaleNote(scaleNote);
		settings.setScaleType(scaleType);
		settings.setStrings(strings);
		settings.setNumSnapshots(numSnapshots);
		settings.setBeadIndexer(beadIndexer);
		settings.setKeyboards(orderedKeyboards);
		settings.setNumNoteSnapshots(numKeyboardSnapshots);
		Bead[] beadsTest = strings.get(0).getBeads();
		return settings;
	}

	private void readDataString(String data) {
		String basicSettings = data.substring(data.indexOf(":") + 1,
				data.indexOf(";;"));
		String waveStringsString = data.substring(data.indexOf("WaveString:"),
				data.indexOf("Bead:"));
		String beadsString = data.substring(data.indexOf("Bead:"), data.indexOf("Keyboard:"));
		String keyboardsString = data.substring(data.indexOf("Keyboard"));
		readBasicSettings(basicSettings);
		allBeadSnapshots = new ArrayList<BeadSnapshot>();
		beadIndexer = new BeadIndexer();
		readBeads(beadsString);
		readWaveStrings(waveStringsString);
		readKeyboards(keyboardsString);
	}

	private void readBasicSettings(String basicSettings) {
		String[] parts = basicSettings.split(";");
		bpm = Double.parseDouble(parts[0]);
		scaleType = ScaleType.getFromString(parts[1]);
		scaleNote = ScaleNote.getByName(parts[2]);
		numStrings = Integer.parseInt(parts[3]);
		numBeads = Integer.parseInt(parts[4]);
		bgFileName = parts[5];
	}
	
	private void readKeyboards(String keyboardStringSection) {
		System.out.println("keyboardStringSection: " + keyboardStringSection);
		String[] individualKeyboards = keyboardStringSection.split("Keyboard:");
		for (int i = 1; i < individualKeyboards.length;i++) {
			System.out.println("Keyboardstring split by 'Keyboard:': " + individualKeyboards[i]);
			orderedKeyboards.add(readKeyboardString(individualKeyboards[i]));
		}
	}
	
	private DynamicKeyboard readKeyboardString(String keyboardString) {
		System.out.println("IndividualKeyboardString: " + keyboardString);
		String[] snapParts = keyboardString.split("KeyboardSnap:");
		String keyboardPart = snapParts[0];
		String keyboardParts[] = keyboardPart.split(";");
		System.out.println("keyboard parts length: " + keyboardParts.length);
		for (int i = 0;i<keyboardParts.length;i++) {
			System.out.println("keyboardParts[" + i + "]: " + keyboardParts[i]);
		}
		int keyNumber = Integer.parseInt(keyboardParts[0]);
		int startKeyNum = Integer.parseInt(keyboardParts[1]);
		int endKeyNum = Integer.parseInt(keyboardParts[2]);
		int bottom = Integer.parseInt(keyboardParts[3]);
		int left = Integer.parseInt(keyboardParts[4]);
		int right = Integer.parseInt(keyboardParts[5]);
		int top = Integer.parseInt(keyboardParts[6]);
		Rect keyboardPosition = new Rect(left, top, right, bottom);
		// Match ordered ImageSchemes to ordered Keyboards.
		ImageScheme imageScheme = orderedImageSchemes.get(orderedKeyboards.size());
		

		DynamicKeyboard keyboard = new DynamicKeyboard(keyboardPosition, imageScheme, startKeyNum, endKeyNum);
		
		keyboard.setStartPianoKeyNumber(startKeyNum);
		keyboard.setEndPianoKeyNumber(endKeyNum);
		
		List<KeyboardSnapshot> snapshots = new ArrayList<KeyboardSnapshot>();
		for (int i = 1; i < snapParts.length; i++) {
			snapshots.add(readKeyboardSnapshot(snapParts[i], keyboard));
			if (orderedKeyboards.size() == 0) {
				numKeyboardSnapshots++;
			}
		}
		keyboard.setScaleTonic(scaleNote);
		keyboard.setScaleType(scaleType);
		KeyboardSnapshot[] snapshotsArray = new KeyboardSnapshot[DynamicKeyboard.MAX_SNAPSHOTS];
		snapshots.toArray(snapshotsArray);
		for (int i = numKeyboardSnapshots;i<DynamicKeyboard.MAX_SNAPSHOTS;i++) {
			snapshotsArray[i] = new KeyboardSnapshot();
		}
		keyboard.setSnapshots(snapshotsArray);
		keyboard.setSelectedKeyByNumber(keyNumber);
		return keyboard;
	}
	
	private KeyboardSnapshot readKeyboardSnapshot(String keyboardSnapshotString, DynamicKeyboard keyboard) {
		String parts[] = keyboardSnapshotString.split(";");
		KeyboardSnapshot snapshot = new KeyboardSnapshot();
		int keyNumber = Integer.parseInt(parts[0]);
		snapshot.setKey(keyboard.getKeyByNumber(keyNumber));
		snapshot.setScale(ScaleType.getFromString(parts[1]));
		snapshot.setCleared(Boolean.getBoolean(parts[2]));
		snapshot.setTonic(ScaleNote.getByName(parts[3]));
		return snapshot;
	}

	private void readWaveStrings(String waveStringSection) {
		String[] individualWaveStrings = waveStringSection.split("WaveString:");
		// Skip 0 to avoid white space.
		for (int i = 1; i < individualWaveStrings.length; i++) {
			strings.add(readWaveString(individualWaveStrings[i]));
		}
	}
	
	private RecordableWaveString readWaveString(String individualWaveString) {
		String[] snapParts = individualWaveString.split("WaveStringSnap:");
		String imageSchemePart;
		try {
			imageSchemePart = individualWaveString.substring(individualWaveString.indexOf("ImageScheme:"), individualWaveString.indexOf("WaveStringSnap:"));
		} catch (StringIndexOutOfBoundsException se) {
			imageSchemePart = individualWaveString.substring(individualWaveString.indexOf("ImageScheme"));
		}
		imageSchemePart = imageSchemePart.replaceFirst("ImageScheme:", "");
		ImageScheme imageScheme = readImageScheme(imageSchemePart);
		orderedImageSchemes.add(imageScheme);
		String waveStringPart = snapParts[0];
		String[] waveStringParts = waveStringPart.split(";");
		int numBeads = Integer.parseInt(waveStringParts[1]);
		int yLevel = Integer.parseInt(waveStringParts[2]);
		int xStart = Integer.parseInt(waveStringParts[3]);
		int xEnd = Integer.parseInt(waveStringParts[4]);
		double maxAmp = Double.parseDouble(waveStringParts[5]);
		String numBeadsString = waveStringParts[6];
		String beadIdsString = waveStringParts[7];
		String[] beadIdStrings = beadIdsString.split(",");
		List<Integer> beadIds = new ArrayList<Integer>();
		for (String beadIdString : beadIdStrings) {
			beadIds.add(Integer.valueOf(Integer.parseInt(beadIdString)));
		}
		List<RecordableBead> stringBeads = getBeadsFromIdsAndSetImageSchemes(
				beadIds, imageScheme);
		List<WaveStringSnapshot> snapshots = new ArrayList<WaveStringSnapshot>();
		for (int i = 1; i < snapParts.length; i++) {
			snapshots.add(readWaveStringSnapshot(snapParts[i], stringBeads));
		}
		RecordableWaveString waveString = new RecordableWaveString(
				xStart,
				xEnd,
				yLevel,
				stringBeads.size(),
				maxAmp,
				imageScheme,
				snapshots
						.toArray(new WaveStringSnapshot[RecordableBead.MAX_SNAPSHOTS]),
				stringBeads
						.toArray(new RecordableBead[BeadString.MAX_ALLOWED_BEADS]),
				beadIndexer);

		return waveString;
	}

	private List<RecordableBead> getBeadsFromIdsAndSetImageSchemes(
			List<Integer> ids, ImageScheme scheme) {
		List<RecordableBead> beadList = new ArrayList<RecordableBead>();
		for (Integer id : ids) {
			for (RecordableBead bead : beads) {
				if (bead.getUniqueIndex() == id.intValue()) {
					bead.setImageScheme(scheme);
					beadList.add(bead);
					break;
				}
			}
		}
		return beadList;
	}

	private WaveStringSnapshot readWaveStringSnapshot(
			String waveStringSnapshotLine, List<RecordableBead> thisWaveBeads) {
		String[] parts = waveStringSnapshotLine.split(";");
		WaveStringSnapshot snapshot = new WaveStringSnapshot();
		snapshot.setGravityOn(false);
		snapshot.setBeads(thisWaveBeads
				.toArray(new RecordableBead[BeadString.MAX_ALLOWED_BEADS]));
		snapshot.setyLevel(Integer.parseInt(parts[0]));
		snapshot.setxStart(Integer.parseInt(parts[1]));
		snapshot.setxEnd(Integer.parseInt(parts[2]));
		snapshot.setMaxAmp(Double.parseDouble(parts[3]));
		snapshot.setCleared(Boolean.getBoolean(parts[4]));
		snapshot.setNumBeads(Integer.parseInt(parts[5]));
		return snapshot;
	}

	private void readBeads(String beadSection) {
		String[] individualBeadStrings = beadSection.split("Bead:");
		// First string is blank
		for (int i = 1; i < individualBeadStrings.length; i++) {
			readBeadString(individualBeadStrings[i]);
		}
		setCorrectConnectionsForSnapshots(allBeadSnapshots, beads);
		setCorrectConnectionsForBeads(beads);
	}
	
	private ImageScheme readImageScheme(String imageSchemeString) {
		System.out.println("IMAGESCHEMESTRING: " + imageSchemeString);
		String[] parts = imageSchemeString.split(";");
		ImageScheme scheme = new ImageScheme(context, 
				parts[0], 
				parts[1], 
				parts[2], 
				parts[3], 
				parts[4], 
				parts[5],
				parts[6], 
				parts[7],
				parts[8],
				Integer.parseInt(parts[9]));
		return scheme;
	}

	private void readBeadString(String beadString) {
		String[] parts = beadString.split("BeadSnap:");
		List<BeadSnapshot> thisBeadSnapshots = new ArrayList<BeadSnapshot>();
		for (int i = 1; i < parts.length; i++) {
			BeadSnapshot snapshot = readBeadSnapshotWithConnectionIDs(parts[i]);
			thisBeadSnapshots.add(snapshot);
		}
		String[] beadParts = parts[0].split(";");
		int uniqueID = Integer.parseInt(beadParts[0]);
		numSnapshots = Integer.parseInt(beadParts[1]);
		double x = Double.parseDouble(beadParts[2]);
		double y = Double.parseDouble(beadParts[3]);
		double weight = Double.parseDouble(beadParts[4]);
		double friction = Double.parseDouble(beadParts[5]);
		double radius = Double.parseDouble(beadParts[6]);
		boolean locked = Boolean.getBoolean(beadParts[7]);
		boolean xLocked = Boolean.getBoolean(beadParts[8]);
		boolean destroyed = Boolean.getBoolean(beadParts[9]);
		double velX = Double.parseDouble(beadParts[10]);
		double velY = Double.parseDouble(beadParts[11]);
		Velocity v = new Velocity(velX, velY);
		int numConnections = Integer.parseInt(beadParts[12]);
		List<Integer> connectionIdInts = new ArrayList<Integer>();
		if (beadParts.length > 13) {
			String connectionIds = beadParts[13];
			String[] connectionIdStrings = connectionIds.split(",");

			for (String connectionIdString : connectionIdStrings) {
				connectionIdInts.add(Integer.parseInt(connectionIdString));
			}
		}

		RecordableBead bead = new RecordableBead(x, y, false, beadIndexer);
		bead.setUniqueIndex(uniqueID);
		bead.setSnapshots(thisBeadSnapshots
				.toArray(new BeadSnapshot[RecordableBead.MAX_SNAPSHOTS]));
		bead.setWeight(weight);
		bead.setFriction(friction);
		bead.setLocked(locked);
//		bead.setXLocked(xLocked);
		bead.setDestroyed(destroyed);
		bead.setVelocity(v);
		bead.setConnectionIds(connectionIdInts);
		beads.add(bead);
	}

	private BeadSnapshot readBeadSnapshotWithConnectionIDs(String beadSnapString) {
		if (beadSnapString.startsWith("null")) {
			return null;
		}
		String[] parts = beadSnapString.split(";");
		BeadSnapshot snapshot = new BeadSnapshot();
		snapshot.setX(Double.parseDouble(parts[0]));
		snapshot.setY(Double.parseDouble(parts[1]));
		snapshot.setWeight(Double.parseDouble(parts[2]));
		snapshot.setFriction(Double.parseDouble(parts[3]));
		snapshot.setRadius(Double.parseDouble(parts[4]));
		snapshot.setLocked(Boolean.getBoolean(parts[5]));
		snapshot.setXLocked(Boolean.getBoolean(parts[6]));
		snapshot.setDestroyed(Boolean.getBoolean(parts[7]));
		snapshot.setCleared(Boolean.getBoolean(parts[8]));
		Velocity v = new Velocity(Double.parseDouble(parts[9]),
				Double.parseDouble(parts[10]));
		snapshot.setVelocity(v);
		int numSnapshots = Integer.parseInt(parts[11]);
		List<Integer> connectionIDs = new ArrayList<Integer>();
		if (parts.length > 12) {
			String[] connectionParts = parts[12].split(",");
			for (int i = 0; i < numSnapshots; i++) {
				connectionIDs.add(new Integer(Integer
						.parseInt(connectionParts[i])));
			}
		}
		snapshot.setConnectionIds(connectionIDs);
		allBeadSnapshots.add(snapshot);
		return snapshot;
	}

	private void setCorrectConnectionsForBeads(List<RecordableBead> allBeads) {
		for (Bead bead : allBeads) {
			List<Integer> connectionIds = bead.getConnectionIds();
			List<Bead> connections = new ArrayList<Bead>();
			for (Integer connectionId : connectionIds) {
				for (RecordableBead potentiallyConnectedBead : allBeads) {
					if (potentiallyConnectedBead.getUniqueIndex() == connectionId
							.intValue()) {
						connections.add(potentiallyConnectedBead);
						break;
					}
				}
			}
			bead.setConnections(connections);
		}
	}

	private void setCorrectConnectionsForSnapshots(
			List<BeadSnapshot> allSnapshots, List<RecordableBead> allBeads) {
		for (BeadSnapshot snapshot : allSnapshots) {
			List<Integer> connectionIds = snapshot.getConnectionIds();
			List<Bead> connections = new ArrayList<Bead>();
			for (Integer connectionId : connectionIds) {
				for (RecordableBead bead : allBeads) {
					if (bead.getUniqueIndex() == connectionId.intValue()) {
						connections.add(bead);
						break;
					}
				}
			}
			snapshot.setConnections(connections);
		}
	}

	private List<Bead> getBeadsFromListOfIds(List<RecordableBead> allBeads,
			String beadIds) {
		String[] parts = beadIds.split(",");
		List<Bead> listedBeads = new ArrayList<Bead>();

		for (int i = 0; i < parts.length; i++) {
			int beadId = Integer.parseInt(parts[i]);
			for (RecordableBead bead : allBeads) {
				if (bead.getUniqueIndex() == beadId) {
					listedBeads.add(bead);
					break;
				}
			}
		}
		return listedBeads;
	}

	public double getBpm() {
		return this.bpm;
	}

	public ScaleType getScaleType() {
		return this.scaleType;
	}

	public ScaleNote getScaleNote() {
		return this.scaleNote;
	}

	public List<RecordableWaveString> getStrings() {
		return this.strings;
	}
	
	public List<DynamicKeyboard> getKeyboards() {
		return this.orderedKeyboards;
	}

	public int getNumSnapshots() {
		return this.numSnapshots;
	}
	
	public int getNumKeyboardSnapshots() {
		return this.numKeyboardSnapshots;
	}

	private String getStringDataFromFile(String filename, Context context)
			throws IOException {

		InputStream inputStream = context.openFileInput(filename);

		if (inputStream != null) {
			InputStreamReader inputStreamReader = new InputStreamReader(
					inputStream);
			BufferedReader bufferedReader = new BufferedReader(
					inputStreamReader);
			String receiveString = "";
			StringBuilder stringBuilder = new StringBuilder();

			while ((receiveString = bufferedReader.readLine()) != null) {
				stringBuilder.append(receiveString);
			}

			inputStream.close();
			return stringBuilder.toString();
		} else {
			throw new IOException();
		}
	}

	public static String getStringFromStream(InputStream in, String encoding)
			throws IOException {
		InputStreamReader reader;
		if (encoding == null) {
			// This constructor sets the character converter to the encoding
			// specified in the "file.encoding" property and falls back
			// to ISO 8859_1 (ISO-Latin-1) if the property doesn't exist.
			reader = new InputStreamReader(in);
		} else {
			reader = new InputStreamReader(in, encoding);
		}

		StringBuilder sb = new StringBuilder();

		final char[] buf = new char[1024];
		int len;
		while ((len = reader.read(buf)) > 0) {
			sb.append(buf, 0, len);
		}

		return sb.toString();
	}
}
