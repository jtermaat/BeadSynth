package com.beadsynth.buttons;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.view.View;

import com.beadsynth.util.ProportionalRect;
import com.beadsynth.view.R;

public class ButtonArrangementManager {
	int screenWidth;
	int screenHeight;
	ProportionalRect topButtonsRect;
	
	public ButtonArrangementManager(int screenWidth, int screenHeight) {
		this.screenWidth = screenWidth;
		this.screenHeight = screenHeight;
		topButtonsRect = null;
	}
	
	public void arrangeButtonsRelatively(Activity activity) {
		System.out.println("ARRANGING BUTTONS RELATIVELY!!!!");
		ProportionalRect connectButtonRect = getProportionalRect("(0.9, 0.5344827586206896); (1.0, 0.603448275862069)");
		setViewIdToProportionalRect(activity, R.id.connect_button, connectButtonRect);
		ProportionalRect lockButtonRect = getProportionalRect("(0.9, 0.46551724137931033); (1.0, 0.5344827586206896)");
		setViewIdToProportionalRect(activity, R.id.lock_button, lockButtonRect);
		ProportionalRect takeSnapshotRect = getProportionalRect("(0.8, 0.28735632183908044); (0.9, 0.3563218390804598)");
		setViewIdToProportionalRect(activity, R.id.take_snapshot, takeSnapshotRect);
		ProportionalRect clearSnapshotRect = getProportionalRect("(0.9, 0.28735632183908044); (1.0, 0.3563218390804598)");
		setViewIdToProportionalRect(activity, R.id.clear_snapshot, clearSnapshotRect);
		ProportionalRect keyboardSnapshotButtonRect = getProportionalRect("(0.8, 0.7341954022988506); (0.9, 0.8031609195402298)");
		setViewIdToProportionalRect(activity, R.id.keyboard_snapshot_button, keyboardSnapshotButtonRect);
		ProportionalRect keyboardClearButtonRect = getProportionalRect("(0.9, 0.7341954022988506); (1.0, 0.8031609195402298)");
		setViewIdToProportionalRect(activity, R.id.keyboard_clear_button, keyboardClearButtonRect);
		ProportionalRect removeBeadRect = getProportionalRect("(0.0, 0.46551724137931033); (0.05, 0.5344827586206896)");
		setViewIdToProportionalRect(activity, R.id.remove_bead, removeBeadRect);
		ProportionalRect addBeadRect = getProportionalRect("(0.0, 0.39655172413793105); (0.05, 0.46551724137931033)");
		setViewIdToProportionalRect(activity, R.id.add_bead, addBeadRect);
		ProportionalRect settingsButtonRect = getProportionalRect("(0.9, 0.021551724137931036); (0.95, 0.09051724137931035)");
		setViewIdToProportionalRect(activity, R.id.settings_button, settingsButtonRect);
		ProportionalRect recordButtonRect = getProportionalRect("(0.85, 0.021551724137931036); (0.9, 0.09051724137931035)"); 
		setViewIdToProportionalRect(activity, R.id.record_button, recordButtonRect);
		ProportionalRect helpButtonRect = getProportionalRect("(0.95, 0.021551724137931036); (1.0, 0.09051724137931035)");
		setViewIdToProportionalRect(activity, R.id.help_button, helpButtonRect);
		ProportionalRect beat0Rect = getProportionalRect("(0.078125, 0.021551724137931036); (0.1515625, 0.08764367816091954)");
		setViewIdToProportionalRect(activity, R.id.beat_0, beat0Rect);
		ProportionalRect beat1Rect = getProportionalRect("(0.1515625, 0.021551724137931036); (0.225, 0.08764367816091954)");
		setViewIdToProportionalRect(activity, R.id.beat_1, beat1Rect);
		ProportionalRect beat2Rect = getProportionalRect("(0.225, 0.021551724137931036); (0.2984375, 0.08764367816091954)");
		setViewIdToProportionalRect(activity, R.id.beat_2, beat2Rect);
		ProportionalRect beat3Rect = getProportionalRect("(0.2984375, 0.021551724137931036); (0.371875, 0.08764367816091954)");
		setViewIdToProportionalRect(activity, R.id.beat_3, beat3Rect);
		ProportionalRect beat4Rect = getProportionalRect("(0.371875, 0.021551724137931036); (0.4453125, 0.08764367816091954)");
		setViewIdToProportionalRect(activity, R.id.beat_4, beat4Rect);
		ProportionalRect beat5Rect = getProportionalRect("(0.4453125, 0.021551724137931036); (0.51875, 0.08764367816091954)");
		setViewIdToProportionalRect(activity, R.id.beat_5, beat5Rect);
		ProportionalRect beat6Rect = getProportionalRect("(0.51875, 0.021551724137931036); (0.5921875, 0.08764367816091954)");
		setViewIdToProportionalRect(activity, R.id.beat_6, beat6Rect);
		ProportionalRect beat7Rect = getProportionalRect("(0.5921875, 0.021551724137931036); (0.665625, 0.08764367816091954)");
		setViewIdToProportionalRect(activity, R.id.beat_7, beat7Rect);
		ProportionalRect noteSnap1Rect = getProportionalRect("(0.8, 0.8031609195402298); (0.85, 0.8721264367816092)");
		setViewIdToProportionalRect(activity, R.id.note_snap_1, noteSnap1Rect);
		ProportionalRect noteSnap2Rect = getProportionalRect("(0.85, 0.8031609195402298); (0.9, 0.8721264367816092)");
		setViewIdToProportionalRect(activity, R.id.note_snap_2, noteSnap2Rect);
		ProportionalRect noteSnap3Rect = getProportionalRect("(0.9, 0.8031609195402298); (0.95, 0.8721264367816092)");
		setViewIdToProportionalRect(activity, R.id.note_snap_3, noteSnap3Rect);
		ProportionalRect noteSnap4Rect = getProportionalRect("(0.95, 0.8031609195402298); (1.0, 0.8721264367816092)");
		setViewIdToProportionalRect(activity, R.id.note_snap_4, noteSnap4Rect);
		ProportionalRect noteSnap5Rect = getProportionalRect("(0.8, 0.8721264367816092); (0.85, 0.9410919540229885");
		setViewIdToProportionalRect(activity, R.id.note_snap_5, noteSnap5Rect);
		ProportionalRect noteSnap6Rect = getProportionalRect("(0.85, 0.8721264367816092); (0.9, 0.9410919540229885)");
		setViewIdToProportionalRect(activity, R.id.note_snap_6, noteSnap6Rect);
		ProportionalRect noteSnap7Rect = getProportionalRect("(0.9, 0.8721264367816092); (0.95, 0.9410919540229885)");
		setViewIdToProportionalRect(activity, R.id.note_snap_7, noteSnap7Rect);
		ProportionalRect noteSnap8Rect = getProportionalRect("(0.95, 0.8721264367816092); (1.0, 0.9410919540229885)");
		setViewIdToProportionalRect(activity, R.id.note_snap_8, noteSnap8Rect);
		ProportionalRect snap1Rect = getProportionalRect("(0.8, 0.14942528735632185); (0.85, 0.21839080459770116)");
		setViewIdToProportionalRect(activity, R.id.snap_1, snap1Rect);
		ProportionalRect snap2Rect = getProportionalRect("(0.85, 0.14942528735632185); (0.9, 0.21839080459770116)"); 
		setViewIdToProportionalRect(activity, R.id.snap_2, snap2Rect);
		ProportionalRect snap3Rect = getProportionalRect("(0.9, 0.14942528735632185); (0.95, 0.21839080459770116)");
		setViewIdToProportionalRect(activity, R.id.snap_3, snap3Rect);
		ProportionalRect snap4Rect = getProportionalRect("(0.95, 0.14942528735632185); (1.0, 0.21839080459770116)");
		setViewIdToProportionalRect(activity, R.id.snap_4, snap4Rect);
		ProportionalRect snap5Rect = getProportionalRect("(0.8, 0.21839080459770116); (0.85, 0.28735632183908044)");
		setViewIdToProportionalRect(activity, R.id.snap_5, snap5Rect);
		ProportionalRect snap6Rect = getProportionalRect("(0.85, 0.21839080459770116); (0.9, 0.28735632183908044)");
		setViewIdToProportionalRect(activity, R.id.snap_6, snap6Rect);
		ProportionalRect snap7Rect = getProportionalRect("(0.9, 0.21839080459770116); (0.95, 0.28735632183908044)");
		setViewIdToProportionalRect(activity, R.id.snap_7, snap7Rect);
		ProportionalRect snap8Rect = getProportionalRect("(0.95, 0.21839080459770116); (1.0, 0.28735632183908044)");
		setViewIdToProportionalRect(activity, R.id.snap_8, snap8Rect);
		topButtonsRect = getProportionalRect("(0.078125, 0.0215517241); (0.66484375, 0.0905172414)");
	}
	
	private static void setViewIdToProportionalRect(Activity activity, int id, ProportionalRect pRect) {
		View view = activity.findViewById(id);
		setViewToRect(view, pRect.getRect());
	}
	
	public static void setViewToRect(View view, Rect rect) {
		view.setTop(rect.top);
		view.setLeft(rect.left);
		view.setBottom(rect.bottom);
		view.setRight(rect.right);
	}
	
	private ProportionalRect getProportionalRect(String coordinates) {
		String strippedCoords = coordinates.replaceAll("[(),;]", "");
		String[] parts = strippedCoords.split(" ");
		double left = Double.parseDouble(parts[0]);
		double top = Double.parseDouble(parts[1]);
		double right = Double.parseDouble(parts[2]);
		double bottom = Double.parseDouble(parts[3]);
		return new ProportionalRect(screenWidth, screenHeight, top, left, bottom, right);
	}
	
	public ProportionalRect getTopProportionalRect() {
		return topButtonsRect;
	}
}


//
//view id in hex: 7f080020 
//
//(beat_0)
//(0.078125, 0.021551724137931036); (0.1515625, 0.08764367816091954)
//view id in hex: 7f080021 
//
//(beat_1)
//(0.1515625, 0.021551724137931036); (0.225, 0.08764367816091954)
//view id in hex: 7f080022 
//
//(beat_2)
//(0.225, 0.021551724137931036); (0.2984375, 0.08764367816091954)
//view id in hex: 7f080023 
//
//(beat_3)
//(0.2984375, 0.021551724137931036); (0.371875, 0.08764367816091954)
//view id in hex: 7f080024 
//
//(beat_4)
//(0.371875, 0.021551724137931036); (0.4453125, 0.08764367816091954)
//view id in hex: 7f080025 
//
//(beat_5)
//(0.4453125, 0.021551724137931036); (0.51875, 0.08764367816091954)
//view id in hex: 7f080026 
//
//(beat_6)
//(0.51875, 0.021551724137931036); (0.5921875, 0.08764367816091954)
//view id in hex: 7f080027 
//
//(beat_7)
//(0.5921875, 0.021551724137931036); (0.665625, 0.08764367816091954)
//view id in hex: 7f08002b 
//
//(note_snap_1)
//(0.8, 0.8031609195402298); (0.85, 0.8721264367816092)
//view id in hex: 7f080033 
//
//(connect_button)
//(0.9, 0.5344827586206896); (1.0, 0.603448275862069)
//view id in hex: 7f080029 
//
//(snap_1)
//(0.8, 0.14942528735632185); (0.85, 0.21839080459770116)
//view id in hex: 7f080039 
//
//(take_snapshot)
//(0.8, 0.28735632183908044); (0.9, 0.3563218390804598)
//view id in hex: 7f08003c 
//
//(clear_snapshot)
//(0.9, 0.28735632183908044); (1.0, 0.3563218390804598)
//view id in hex: 7f08003d 
//
//(keyboard_snapshot_button)
//(0.8, 0.7341954022988506); (0.9, 0.8031609195402298)
//view id in hex: 7f08003e 
//
//(keyboard_clear_button)
//(0.9, 0.7341954022988506); (1.0, 0.8031609195402298)
//view id in hex: 7f080034 
//
//(lock_button)
//(0.9, 0.46551724137931033); (1.0, 0.5344827586206896)
//view id in hex: 7f08003f 
//
//(remove_bead)
//(0.0, 0.46551724137931033); (0.05, 0.5344827586206896)
//view id in hex: 7f080040 
//
//(add_bead)
//(0.0, 0.39655172413793105); (0.05, 0.46551724137931033)
//view id in hex: 7f080041 
//
//(settings_button)
//(0.9, 0.021551724137931036); (0.95, 0.09051724137931035)
//view id in hex: 7f08003b 
//
//(record_button)
//(0.85, 0.021551724137931036); (0.9, 0.09051724137931035)
//view id in hex: 7f080042 
//
//(help_button)
//(0.95, 0.021551724137931036); (1.0, 0.09051724137931035)
//view id in hex: 7f080020 
//
//view id in hex: 7f080028 
//
//(snap_5)
//(0.8, 0.21839080459770116); (0.85, 0.28735632183908044)
//view id in hex: 7f08002b 
//
//(note_snap_1)
//(0.8, 0.8031609195402298); (0.85, 0.8721264367816092)
//view id in hex: 7f08002c 
//
//(note_snap_2)
//(0.85, 0.8031609195402298); (0.9, 0.8721264367816092)
//view id in hex: 7f08002d 
//
//(note_snap_3)
//(0.9, 0.8031609195402298); (0.95, 0.8721264367816092)
//view id in hex: 7f08002e 
//
//(note_snap_4)
//(0.95, 0.8031609195402298); (1.0, 0.8721264367816092)
//view id in hex: 7f08002f 
//
//(note_snap_5)
//(0.8, 0.8721264367816092); (0.85, 0.9410919540229885)
//view id in hex: 7f080030 
//
//(note_snap_6)
//(0.85, 0.8721264367816092); (0.9, 0.9410919540229885)
//view id in hex: 7f080031 
//
//(note_snap_7)
//(0.9, 0.8721264367816092); (0.95, 0.9410919540229885)
//view id in hex: 7f080032 
//
//(note_snap_8)
//(0.95, 0.8721264367816092); (1.0, 0.9410919540229885)
//view id in hex: 7f080033 
//
//(connect_button)
//(0.9, 0.5344827586206896); (1.0, 0.603448275862069)
//view id in hex: 7f080035 
//
//(snap_3)
//(0.9, 0.14942528735632185); (0.95, 0.21839080459770116)
//view id in hex: 7f080038 
//
//(snap_4)
//(0.95, 0.14942528735632185); (1.0, 0.21839080459770116)
//view id in hex: 7f080037 
//
//(snap_8)
//(0.95, 0.21839080459770116); (1.0, 0.28735632183908044)
//view id in hex: 7f080036 
//
//(snap_7)
//(0.9, 0.21839080459770116); (0.95, 0.28735632183908044)
//view id in hex: 7f08003a 
//
//(snap_6)
//(0.85, 0.21839080459770116); (0.9, 0.28735632183908044)
//view id in hex: 7f08002a 
//
//(snap_2)
//(0.85, 0.14942528735632185); (0.9, 0.21839080459770116)
//
//
