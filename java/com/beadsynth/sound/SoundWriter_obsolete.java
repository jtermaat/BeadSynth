///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//
//package soundphysics.sound;
//import javax.sound.sampled.*;
//
///**
// *
// * @author John
// */
//public class SoundWriter {
//
//    SourceDataLine[] sourceLines;
//    int numLines;
//    int bufferSize;
//
//    static float SAMPLE_RATE = 22050;
//    static int SAMPLE_SIZE = 16;
//    static int CHANNELS = 1;
//    static boolean SIGNED = true;
//    static boolean BIG_ENDIAN = true;
//
//    static int DEFAULT_BUFFER_SIZE = 10000;
//
//    static AudioFormat FORMAT = new AudioFormat(SAMPLE_RATE, SAMPLE_SIZE, CHANNELS, SIGNED, BIG_ENDIAN);
//
//    public SoundWriter(int NumLines, int BufferSize) {
//        numLines = NumLines;
//        sourceLines = new SourceDataLine[numLines];
//        bufferSize = BufferSize;
//
//        Mixer.Info[] infos = AudioSystem.getMixerInfo();
//        Mixer speakers = AudioSystem.getMixer(infos[0]);
//        Line.Info[] lineInfos = speakers.getSourceLineInfo();
//        for (int i = 0;i<numLines;i++) {
//            try {
//                sourceLines[i] = (SourceDataLine)AudioSystem.getLine(lineInfos[0]);
//                sourceLines[i].open(FORMAT, bufferSize);
//            }
//            catch(Exception e) {
//                sourceLines[i] = null;
//                e.printStackTrace();
//            }
//        }
//    }
//
//
//
//    public void start() {
//        for (int i = 0;i<numLines;i++) {
//            sourceLines[i].start();
//        }
//    }
//
//    public int bufferSpace(int LineNum) {
//        return sourceLines[LineNum].available();
//    }
//
//    public void loadData(short[] Data, int LineNum) {
//        byte[] byteData = shortsToBytes(Data);
//        sourceLines[LineNum].write(byteData, 0, byteData.length);
//    }
//
//    public void loadData(short[] Data) {
//        loadData(Data, 0);
//    }
//
////    protected static byte[] shortsToBytes(short[] shortData) {
////        byte[] byteData = new byte[shortData.length * 2];
////        for (int i = 0;i<shortData.length;i++) {
////            byte[] byteValues = shortToBytes(shortData[i]);
////            byteData[i*2 + 1] = byteValues[0];
////            byteData[i*2] = byteValues[1];
////        }
////        return byteData;
////    }
////
////    protected static byte[] shortToBytes(short oneShort) {
////        byte[] bytes = new byte[2];
////        byte hexBase = (byte)0xff;
////        bytes[0] = (byte)(hexBase & oneShort);
////        bytes[1] = (byte)(((hexBase << 8) & oneShort) >> 8);
////        return bytes;
////    }
//
//        // FROM TOTEM
//
//        protected byte[] shortsToBytes(short[] shortData) {
//        byte[] byteData = new byte[shortData.length * 2];
//        for (int i = 0;i<shortData.length;i++) {
//            byte[] byteValues = shortToBytes(shortData[i]);
//            byteData[i*2 + 1] = byteValues[1];
//            byteData[i*2] = byteValues[0];
//        }
//        return byteData;
//    }
//
//    protected byte[] shortToBytes(short oneShort) {
//        if (!BIG_ENDIAN) {
//            byte[] bytes = new byte[2];
//            byte hexBase = (byte)0xff;
//            bytes[0] = (byte)(hexBase & oneShort);
//            bytes[1] = (byte)(((hexBase << 8) & oneShort) >> 8);
//            return bytes;
//        }
//        else {
//            byte[] bytes = new byte[2];
//            byte hexBase = (byte)0xff;
//            bytes[1] = (byte)(hexBase & oneShort);
//            bytes[0] = (byte)(((hexBase << 8) & oneShort) >> 8);
//            return bytes;
//        }
//    }
//
//}