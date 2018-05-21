package display;

import model.GridPart;
import model.Traffic;
import model.streetpart.SingleLight;
import model.streetpart.StreetPart;
import sim.display.Console;
import sim.display.Controller;
import sim.display.Display2D;
import sim.display.GUIState;
import sim.engine.SimState;
import sim.field.continuous.Continuous2D;
import sim.field.grid.ObjectGrid2D;
import sim.portrayal.DrawInfo2D;
import sim.portrayal.continuous.ContinuousPortrayal2D;
import sim.portrayal.grid.ObjectGridPortrayal2D;
import sim.portrayal.simple.CircledPortrayal2D;
import sim.portrayal.simple.LabelledPortrayal2D;
import sim.portrayal.simple.OvalPortrayal2D;
import sim.util.MutableInt2D;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;


public class TrafficDisplay extends GUIState {

    private Display2D display;
    private JFrame displayFrame;
    private ObjectGridPortrayal2D streetPortrayal = new ObjectGridPortrayal2D();
    private ContinuousPortrayal2D vehiclesPortrayal = new ContinuousPortrayal2D();
    private ContinuousPortrayal2D streetLightsPortrayal = new ContinuousPortrayal2D();

    //    private BufferedImage crossroadsImage;
    private static int DISPLAY_WIDTH = 720, DISPLAY_HEIGHT = 720;

    private BufferedImage[] imgsArray = new BufferedImage[12];

    private TrafficDisplay() {
        super(new Traffic(System.currentTimeMillis()));
        initImgsArray();
    }


    public TrafficDisplay(SimState simState) {
        super(simState);
        initImgsArray();
    }

    private void initImgsArray() {
        try {
            imgsArray[0] = ImageIO.read(new File("resources//northsouth.png"));
            imgsArray[1] = ImageIO.read(new File("resources//westeast.png"));
            imgsArray[2] = ImageIO.read(new File("resources//northwest.png"));
            imgsArray[3] = ImageIO.read(new File("resources//northeast.png"));
            imgsArray[4] = ImageIO.read(new File("resources//southwest.png"));
            imgsArray[5] = ImageIO.read(new File("resources//southeast.png"));
            imgsArray[6] = ImageIO.read(new File("resources//northsouthwest.png"));
            imgsArray[7] = ImageIO.read(new File("resources//northsoutheast.png"));
            imgsArray[8] = ImageIO.read(new File("resources//southwesteast.png"));
            imgsArray[9] = ImageIO.read(new File("resources//northwesteast.png"));
            imgsArray[10] = ImageIO.read(new File("resources//crossroads.png"));
            imgsArray[11] = ImageIO.read(new File("resources//empty.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        TrafficDisplay vid = new TrafficDisplay();
        Console c = new Console(vid);
        c.setVisible(true);
    }

    public void start() {
        super.start();
        setupPortrayals();
    }


    private void setupPortrayals() {
        ObjectGrid2D streetsGrids = ((Traffic) state).getAllStreetsGrids();
        Continuous2D vehiclesYardLayer = ((Traffic) state).getVehiclesYardLayer();
        Continuous2D streetLightsYardLayer = ((Traffic) state).getStreetLightsYardLayer();
        streetLightsPortrayal.setField(streetLightsYardLayer);
        vehiclesPortrayal.setField(vehiclesYardLayer);
        streetPortrayal.setField(streetsGrids);
        streetPortrayal.setPortrayalForAll(new OvalPortrayal2D(Color.BLUE) {
            @Override
            public void draw(Object object, Graphics2D graphics, DrawInfo2D info) {
                MutableInt2D location = (MutableInt2D) info.location;

                int tileWidth = DISPLAY_WIDTH / Traffic.COLUMNS;
                int tileHeight = DISPLAY_HEIGHT / Traffic.ROWS;

                int j = location.x % Traffic.TILE_SIZE;
                int i = location.y % Traffic.TILE_SIZE;

                if (j == 0 && i == 0) {
                    java.awt.geom.Rectangle2D.Double draw = info.draw;
                    double width = draw.width * this.scale + this.offset;
                    double height = draw.height * this.scale + this.offset;

                    int imgX = (int) (draw.x - width / 2.0D);
                    int imgY = (int) (draw.y - height / 2.0D);

                    StreetPart streetPart = ((GridPart) object).getStreetPart();
                    graphics.drawImage(
                            TrafficDisplay.this.imgsArray[streetPart.tileIndex()].getScaledInstance((int) (tileWidth * display.getScale()), (int) (tileHeight * display.getScale()), 0),
                            imgX, imgY, null
                    );

                }

            }
        });
        streetLightsPortrayal.setPortrayalForAll(
                new OvalPortrayal2D() {
                    @Override
                    public void draw(Object object, Graphics2D graphics, DrawInfo2D info) {
                        SingleLight singleLight = (SingleLight) object;
                        paint = singleLight.turnedOn() ? Color.green : Color.red;
                        super.draw(object, graphics, info);
                    }
                }
        );
        vehiclesPortrayal.setPortrayalForAll(
                new CircledPortrayal2D(
                        new LabelledPortrayal2D(new OvalPortrayal2D(Color.BLUE), 5.0, null, Color.black, true),
                        0, 5.0, Color.green, true
                ));
        display.reset();
        display.setBackdrop(Color.white);
        display.repaint();
        displayFrame.setSize(DISPLAY_WIDTH * 3 / 2, DISPLAY_HEIGHT * 3 / 2);
    }

    public void load(SimState state) {
        super.load(state);
        setupPortrayals();
    }

    public void init(Controller c) {
        super.init(c);
        display = new Display2D(DISPLAY_WIDTH, DISPLAY_HEIGHT, this);
        display.setClipping(false);
        displayFrame = display.createFrame();
        displayFrame.setTitle("Traffic Display");
        c.registerFrame(displayFrame); // so the frame appears in the "Display" list
        displayFrame.setVisible(true);
        display.attach(streetPortrayal, "Streets");
        display.attach(streetLightsPortrayal, "StreetLights");
        display.attach(vehiclesPortrayal, "Vehicles");
    }

    public void quit() {
        super.quit();
        if (displayFrame != null) displayFrame.dispose();
        displayFrame = null;
        display = null;
    }
}
