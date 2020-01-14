///*
// *  * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//
//package soundphysics.testing;
//
//import soundphysics.physics.Bead;
//import java.awt.*;
//import java.awt.event.*;
//import soundphysics.*;
//import soundphysics.sound.*;
//
///**
// *
// * @author John
// */
//public class BeadTest extends java.applet.Applet implements Runnable, MouseMotionListener, MouseListener, KeyListener {
//    int frame;
//    int delay;
//    Thread animator;
//
//    Dimension offDimension;
//    Image offImage;
//    Graphics offGraphics;
//
//    WaveStringWriter synth;
//
//    WaveString[] strings;
//    int numStrings;
//    BeadString userBeads;
//
//    short[] previousArray;
//    short[] currentArray;
//    short[] invertedBackwardArray;
//    short[] totalArray;
//
//    static int MAX_WAVE_LENGTH = 300;
//    static int MIN_WAVE_LENGTH = 60;
//
//    static int ARRAY_LENGTH = 80;
//    static int WAVE_LENGTH = ARRAY_LENGTH;
//
//    static int SAMPLE_RATE = 22050;
//
//    int arrayLength;
//    boolean arrayLengthRising;
//
//    Rectangle[] waveSpaces = new Rectangle[4];
//
//
//
//    Bead bound;
//    Bead connector;
//
//    static boolean GRAVITY_ON = true;
//
//    boolean gravityOn;
//
//    double boundX;
//    double boundY;
//
//    static int NORMAL_MODE = 0;
//    static int BUILD_MODE = 1;
//    static int CONNECT_MODE = 2;
//    static int LOCK_MODE = 3;
//
//    int mode;
//
//    static boolean oscillating = false;
//
//    static double XSTART = 30;
//    static double XEND = 700;
//    //static double YSTART = 328;
//    //static double YEND = 328;
//    static double YLEVEL = 150;
//
//    static int SPACE_DIFF = 100;
//
//    static double MAX_AMP = 700; //600
//
//    static double DEFAULT_WEIGHT_CHANGE = 0.01;
//
//    static int DEFAULT_NUM_BEADS = 30;
//
//
//    public void init() {
//	//String str = getParameter("fps");
//	//int fps = (str != null) ? Integer.parseInt(str) : 10;
//	//delay = (fps > 0) ? (1000 / fps) : 100;
//        delay = 30;
//
//        // my code here
//        this.resize(900,700);
//
//        gravityOn = GRAVITY_ON;
//        numStrings = 2;
//
//        for (int i = 0;i<numStrings;i++) {
//            waveSpaces[i] = new Rectangle(700,100+(i * 100),200,100);
//        }
//
//        double frequency1 = 110.000;
//        double frequency2 = 164.814;
//        double frequency3 = 220.000;
//        double frequency4 = 659.255;    //659.255
//
//        int waveLength1 = (int)((double)SAMPLE_RATE / frequency1);
//        int waveLength2 = (int)((double)SAMPLE_RATE / frequency2);
//        int waveLength3 = (int)((double)SAMPLE_RATE / frequency3);
//        int waveLength4 = (int)((double)SAMPLE_RATE / frequency4);
//
//        WaveString string = new WaveString(XSTART, XEND, YLEVEL, DEFAULT_NUM_BEADS, MAX_AMP);
//        WaveString string2 = new WaveString(XSTART, XEND, YLEVEL + SPACE_DIFF, DEFAULT_NUM_BEADS, MAX_AMP);
//        WaveString string3 = new WaveString(XSTART, XEND, YLEVEL + SPACE_DIFF * 2, DEFAULT_NUM_BEADS, MAX_AMP);
//        WaveString string4 = new WaveString(XSTART, XEND, YLEVEL + SPACE_DIFF * 3, DEFAULT_NUM_BEADS, MAX_AMP);
//
//        userBeads = new BeadString(10, 10, 10, 10, 0, false);
//
//        strings = new WaveString[numStrings];
//        strings[0] = string;
//        strings[1] = string2;
//        //strings[2] = string3;
//        //strings[3] = string4;
//
//        bound = null;
//        connector = null;
//
//        mode = NORMAL_MODE;
//
//        previousArray = strings[0].getShortArray(ARRAY_LENGTH);
//        currentArray = strings[0].getShortArray(ARRAY_LENGTH);
//
//        synth = new WaveStringWriter();
//        synth.addString(strings[0], waveLength1);
//        synth.addString(strings[1], waveLength2);
//        //synth.addString(strings[2], waveLength3);
//        //synth.addString(strings[3], waveLength4);
//
//
//        arrayLength = 100;
//        arrayLengthRising = true;
//
//        addKeyListener(this);
//        addMouseMotionListener(this);
//        addMouseListener(this);
//    }
//
//    public void start() {
//	animator = new Thread(this);
//	animator.start();
//        try {
//            synth.start();
//        }
//        catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    public void run() {
//	// Remember the starting time
//	long tm = System.currentTimeMillis();
//	while (Thread.currentThread() == animator) {
//	    // Display the next frame of animation.
//	    repaint();
//
//	    // Delay depending on how far we are behind.
//	    try {
//		tm += delay;
//		Thread.sleep(Math.max(0, tm - System.currentTimeMillis()));
//	    } catch (InterruptedException e) {
//		break;
//	    }
//
//	    // Advance the frame
//	    frame++;
//	}
//    }
//
//    public void stop() {
//	animator = null;
//    }
//
//    public void paint(Graphics g) {
//	if (offImage != null) {
//	    g.drawImage(offImage, 0, 0, null);
//	}
//    }
//
//    public void update(Graphics g) {
//         Dimension d = getSize();
//
//	// Create the offscreen graphics context
//	if ((offGraphics == null)
//	 || (d.width != offDimension.width)
//	 || (d.height != offDimension.height)) {
//	    offDimension = d;
//	    offImage = createImage(d.width, d.height);
//	    offGraphics = offImage.getGraphics();
//	}
//
//	// Erase the previous image
//	offGraphics.setColor(getBackground());
//	offGraphics.fillRect(0, 0, d.width, d.height);
//	offGraphics.setColor(Color.black);
//
//	// Paint the frame into the image
//	paintFrame(offGraphics);
//
//	// Paint the image onto the screen
//	g.drawImage(offImage, 0, 0, null);
//        
//        /*
//        if (oscillating) {
//            if (arrayLengthRising)
//               arrayLength++;
//            else
//                arrayLength--;
//            if (arrayLength > MAX_WAVE_LENGTH)
//                arrayLengthRising = false;
//            if (arrayLength <= MIN_WAVE_LENGTH)
//                arrayLengthRising = true;
//            synth.setWaveLength(0,arrayLength);
//            System.out.println("arrayLength: " + arrayLength);
//        }
//         * */
//
//        synth.loadAsPossible();
//
///*
//        g.setColor(Color.white);
//        for (int i = 0;i<numStrings;i++) {
//            strings[i].draw(g);
//            userBeads.draw(g);
//            g.fillRect((int)waveSpaces[i].getX(), (int)waveSpaces[i].getY(), (int)waveSpaces[i].getWidth(), (int)waveSpaces[i].getHeight());
//        }
//
//        
//        //WaveString.drawWaveArray(previousArray, WAVE_SPACE, g);
//        //player.adjustVelocity(barr);
//
//        if (bound != null)
//           bound.drawRing(g);
//*/
//        if (bound != null) {
//            bound.setX(boundX);
//            bound.setY(boundY);
//        }
//
//
//        for (int i = 0;i<numStrings;i++) {
//            strings[i].nextFrame();
//        }
//        userBeads.nextFrame();
//        g.setColor(Color.black);
///*
//        for (int i =0;i<numStrings;i++) {
//            //previousArray = currentArray;
//            currentArray = strings[i].getShortArrayTwo(arrayLength);
//            invertedBackwardArray = WaveString.getInvertedBackward(currentArray);
//            totalArray = WaveString.concatArrays(currentArray,invertedBackwardArray);
//            WaveString.drawWaveArray(totalArray, waveSpaces[i], g);
//            strings[i].draw(g);
//        }
//        userBeads.draw(g);
//
//        if (bound != null)
//            bound.drawRing(g);
//        if (connector != null) {
//            connector.drawRed(g);
//        }
//        Font f = new Font("Arial", Font.BOLD, 24);
//        g.setFont(f);
//        g.drawString("Waveshape", 705, 95);
//*/
//    }
//
//    public void paintFrame(Graphics g) {
//        //g.drawRect(4,4,895,695);
//        for (int i =0;i<numStrings;i++) {
//            //previousArray = currentArray;
//            currentArray = strings[i].getShortArrayTwo(arrayLength);
//            invertedBackwardArray = WaveString.getInvertedBackward(currentArray);
//            totalArray = WaveString.concatArrays(currentArray,invertedBackwardArray);
//            WaveString.drawWaveArray(totalArray, waveSpaces[i], g);
//            strings[i].draw(g);
//        }
//        userBeads.draw(g);
//
//        if (bound != null)
//            bound.drawRing(g);
//        if (connector != null) {
//            connector.drawRed(g);
//        }
//        Font f = new Font("Arial", Font.BOLD, 24);
//        g.setFont(f);
//        g.drawString("Waveshape", 705, 95);
//    }
//
//    private void addBead() {
//        for (int i = 0;i<numStrings;i++) {
//            strings[i].addBead();
//        }
//    }
//
//    private void removeBead() {
//        for (int i = 0;i<numStrings;i++) {
//            strings[i].removeBead();
//        }
//    }
//
//    public Bead getGrabbedBead(int X, int Y) {
//        Bead grabbed = null;
//        for (int i = 0;i<numStrings;i++) {
//            Bead[] array = strings[i].getArray();
//            for (int j = 0;j<strings[i].getNumBeads();j++) {
//                double beadX = array[j].getX();
//                double beadY = array[j].getY();
//                double beadRadius = array[j].getRadius();
//                if (getDistance(beadX, beadY, (double)X, (double)Y) <= beadRadius) {
//                    grabbed = array[j];
//                }
//            }
//        }
//        Bead[] userArray = userBeads.getArray();
//        for (int k = 0;k<userBeads.getNumBeads();k++) {
//            double beadX = userArray[k].getX();
//            double beadY = userArray[k].getY();
//            double beadRadius = userArray[k].getRadius();
//            if (getDistance(beadX, beadY, (double)X, (double)Y) <= beadRadius) {
//                grabbed = userArray[k];
//            }
//        }
//        return grabbed;
//    }
//
//    public static double getDistance(double X1, double Y1, double X2, double Y2) {
//        return Math.sqrt((X1-X2)*(X1-X2) + (Y1-Y2)*(Y1-Y2));
//    }
//
//
//    public void keyPressed(KeyEvent e) {
//        char c = e.getKeyChar();
//        if (c == 'b')
//            mode = BUILD_MODE;
//        else if (c == 'c')
//            mode = CONNECT_MODE;
//        else if (c == 'l')
//            mode = LOCK_MODE;
//    }
//
//    public void keyReleased(KeyEvent e) {
//        char c = e.getKeyChar();
//        if (c == 'b' && mode == BUILD_MODE)
//            mode = NORMAL_MODE;
//        else if (c == 'c' && mode == CONNECT_MODE)
//            mode = NORMAL_MODE;
//        else if (c == 'l' && mode == LOCK_MODE)
//            mode = NORMAL_MODE;
//    }
//
//    public void keyTyped(KeyEvent e) {
//        char c = e.getKeyChar();
//        if (c == '.' && bound != null)
//            bound.addWeight(DEFAULT_WEIGHT_CHANGE);
//        else if (c == ',' && bound != null)
//            bound.addWeight(DEFAULT_WEIGHT_CHANGE * -1.0);
//        else if (c == 'g') {
//            Bead[] array = strings[0].getArray();
//            for (int i = 0;i<strings[0].getNumBeads();i++) {
//                array[i].switchGravity();
//            }
//        }
//        else if (c == 'a') {
//            addBead();
//        }
//
//        else if (c == 'x') {
//            removeBead();
//        }
//
//    }
//
//
//    public void mousePressed(MouseEvent e) {
//        int mouseX = e.getX();
//        int mouseY = e.getY();
//        bound = getGrabbedBead(mouseX, mouseY);
//        boundX = mouseX;
//        boundY = mouseY;
//        if (bound != null)
//            bound.lock();
//        if (bound != null && connector != null) {
//            bound.connectTo(connector);
//            connector.connectTo(bound);
//            connector = null;
//        }
//
//        
//        if (bound == null && mode == BUILD_MODE) {
//            Bead newBead = new Bead(mouseX, mouseY);
//            userBeads.addBead(newBead);
//            bound = newBead;
//        }
//    }
//
//    public void mouseReleased(MouseEvent e) {
//        if (bound != null && mode != LOCK_MODE && mode != CONNECT_MODE)
//            bound.unlock();
//        if (mode == CONNECT_MODE)
//            connector = bound;
//
//        bound = null;
//    }
//
//    public void mouseEntered(MouseEvent e) {
//    }
//
//    public void mouseExited(MouseEvent e) {
//    }
//
//    public void mouseClicked(MouseEvent e) {
//    }
//
//    public void mouseMoved(MouseEvent e) {
//        boundX = e.getX();
//        boundY = e.getY();
//    }
//
//    public void mouseDragged(MouseEvent e) {
//        boundX = e.getX();
//        boundY = e.getY();
//    }
//
//
//
//}

