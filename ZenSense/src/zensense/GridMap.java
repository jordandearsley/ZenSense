
package zensense;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import static zensense.Control.HISTORICINDEX;
import static zensense.HeatMap.sensorData;
import static zensense.ZenSense.NUMHORIZONTAL;
import static zensense.ZenSense.NUMSENSORS;
import static zensense.ZenSense.RIPEDAYS;
import static zensense.ZenSense.RIPEVOLTAGE;
import static zensense.ZenSense.selectedSensor;


public class GridMap extends JPanel {
    public double mouseX;
    public double mouseY;
    public double WIDTH;
    public double HEIGHT;
    public GridMap() throws IOException{
        

        
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                mouseX=e.getX()*100/WIDTH;
                mouseY=e.getY()*100/HEIGHT;
                updateSensorData();
            }
        });
        this.setVisible(true);
        
    }
    
    public void updateSensorData(){
        for(int i = 0;i<ZenSense.NUMSENSORS;i++){
            if(Math.abs(Double.parseDouble(HeatMap.sensorData.get(HeatMap.sensorData.size()-i-1-NUMSENSORS*HISTORICINDEX)[3])-mouseX)<=21&&(Math.abs(Double.parseDouble(HeatMap.sensorData.get(HeatMap.sensorData.size()-i-1-NUMSENSORS*HISTORICINDEX)[4])-mouseY)<=21)){
                Control.displaySensor(HeatMap.sensorData.get(HeatMap.sensorData.size()-i-1-NUMSENSORS*HISTORICINDEX));
                ZenSense.selectedSensor = sensorData.get(sensorData.size()-i-1-NUMSENSORS*HISTORICINDEX);
                ZenSense.hm.panel.repaint();
                ZenSense.hm.batteryBars.repaint();
                ZenSense.hm.ripenessBars.repaint();
                ZenSense.hm.gridMap.repaint();
                break;
            }
        }
    }
    
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        try {
            BufferedImage image = ImageIO.read(new File("field.png"));
            g.drawImage(image, 0, 0,getWidth(), getHeight(), this);
        } catch (IOException ex) {
            Logger.getLogger(GridMap.class.getName()).log(Level.SEVERE, null, ex);
        }
        WIDTH = this.getWidth();
        HEIGHT = this.getHeight();
        
        ArrayList<String[]> sensorData = new ArrayList<String[]>();
        sensorData = ZenSense.fileData;
        //Time, Voltage, Battery, Xpos%, Ypos%, SensorID
        int windowHeight = (int)(this.getHeight()+this.getHeight()*.01);
        
        for(int i = 0;i<ZenSense.NUMSENSORS;i++){            
            g.setColor(getColor(0,ZenSense.RIPEVOLTAGE,Double.parseDouble(sensorData.get(sensorData.size()-i-1-NUMSENSORS*HISTORICINDEX)[1])));
            g.fillRect((int)(this.getWidth()*Double.parseDouble(sensorData.get(sensorData.size()-i-1-NUMSENSORS*HISTORICINDEX)[3])/100)-this.getWidth()/(ZenSense.NUMHORIZONTAL*2), (int)((windowHeight*Double.parseDouble(sensorData.get(sensorData.size()-i-1-NUMSENSORS*HISTORICINDEX)[4])/100)-windowHeight/(ZenSense.NUMVERTICAL*2)-windowHeight*0.01), this.getWidth()/ZenSense.NUMHORIZONTAL, (int)(windowHeight/ZenSense.NUMVERTICAL));
            g.setColor(Color.BLACK);
            g.drawString((RIPEDAYS-Math.round(RIPEDAYS*Double.parseDouble(sensorData.get(sensorData.size()-i-1-NUMSENSORS*HISTORICINDEX)[1])/RIPEVOLTAGE)+" Days Left"), (int)(this.getWidth()*Double.parseDouble(sensorData.get(sensorData.size()-i-1-NUMSENSORS*HISTORICINDEX)[3])/100)-this.getWidth()/(ZenSense.NUMHORIZONTAL*4), (int)(this.getHeight()*Double.parseDouble(sensorData.get(sensorData.size()-i-1-NUMSENSORS*HISTORICINDEX)[4])/100));
        }
        
        for(int i = 0;i<ZenSense.NUMSENSORS;i++){ 
            if(selectedSensor!=null&&Double.parseDouble(sensorData.get(sensorData.size()-i-1-NUMSENSORS*HISTORICINDEX)[5])==Double.parseDouble(selectedSensor[5])){
                g.setColor(Color.BLUE);
                java.awt.Graphics2D g2 = (java.awt.Graphics2D)g.create();
                g2.setStroke(new java.awt.BasicStroke(3)); // thickness of 3.0f
                g2.setColor(Color.blue);
                g2.drawRect((int)(this.getWidth()*Double.parseDouble(sensorData.get(sensorData.size()-i-1-NUMSENSORS*HISTORICINDEX)[3])/100)-this.getWidth()/(ZenSense.NUMHORIZONTAL*2), (int)((windowHeight*Double.parseDouble(sensorData.get(sensorData.size()-i-1-NUMSENSORS*HISTORICINDEX)[4])/100)-windowHeight/(ZenSense.NUMVERTICAL*2)-windowHeight*0.01), this.getWidth()/ZenSense.NUMHORIZONTAL, (int)(windowHeight/ZenSense.NUMVERTICAL));
            }
        }
    }
    public Color getColor(double minimum, double maximum, double value){
        
        int b = 0;
        int r = 0;
        int g = 0;
        if(value/ZenSense.RIPEVOLTAGE>=0.5){
            g = 255;
            r = (int)(255*(1-value/ZenSense.RIPEVOLTAGE));
        }else{
            r = 255;
            g = (int)(255*(value/ZenSense.RIPEVOLTAGE));
        }
       
        return (new Color(r,g,b,127));
    }
}
