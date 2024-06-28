package Objects;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class DecorateObject extends JPanel {
    private int edgeRectSize = 10;
    int Posx;
    int Posy;
    int width;
    int height;
    ArrayList<BasePort> Ports=new ArrayList<>();
    public DecorateObject(int x,int y,int width,int height) {
        this.setOpaque(false);
        this.Posx=x;
        this.Posy=y;
        this.width=width;
        this.height=height;
        initial();
        //this.setSize(CompositePrototypeObject.this.width, CompositePrototypeObject.this.height);
    }
    void initial(){
        int centerX = this.Posx + (width - this.edgeRectSize) / 2;
        int centerY = this.Posy + (height - this.edgeRectSize) / 2;
        BasePort p1 = new BasePort(centerX, Posy);
        Ports.add(p1);
        BasePort p2 = new BasePort(centerX, Posy+height);
        Ports.add(p2);
        BasePort p3 = new BasePort(Posx,centerY);
        Ports.add(p3);
        BasePort p4 = new BasePort(Posx+width,centerY);
        Ports.add(p4);
    }

    protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            int w = this.Posx + this.width;
            int h = this.Posy + this.height;
            g.setColor(Color.BLACK);
            int centerX;
            int centerY;
            /*if (CompositePrototypeObject.this.shape == Shape.Rectangle) {
                g.drawRect(CompositePrototypeObject.this.positionX, CompositePrototypeObject.this.positionY, CompositePrototypeObject.this.width, CompositePrototypeObject.this.height);
                g.drawLine(CompositePrototypeObject.this.positionX, CompositePrototypeObject.this.positionY, w, h);
                g.drawLine(w, CompositePrototypeObject.this.positionY, CompositePrototypeObject.this.positionX, h);
            } else if (CompositePrototypeObject.this.shape == Shape.Oval) {
                g.drawOval(CompositePrototypeObject.this.positionX, CompositePrototypeObject.this.positionY, CompositePrototypeObject.this.width, CompositePrototypeObject.this.height);
                centerX = w - CompositePrototypeObject.this.width / 2;
                centerY = h - CompositePrototypeObject.this.height / 2;
                int deltaX = (int)((double)CompositePrototypeObject.this.width / (2.0 * Math.sqrt(2.0)));
                int deltaY = (int)((double)CompositePrototypeObject.this.height / (2.0 * Math.sqrt(2.0)));
                g.drawLine(centerX + deltaX, centerY + deltaY, centerX - deltaX, centerY - deltaY);
                g.drawLine(centerX - deltaX, centerY + deltaY, centerX + deltaX, centerY - deltaY);
            }
             */
            centerX = (this.Posx + w - this.edgeRectSize) / 2;
            centerY = (this.Posy + h - this.edgeRectSize) / 2;
            g.fillRect(centerX, this.Posy - this.edgeRectSize / 2, this.edgeRectSize, this.edgeRectSize);
            //Point RectangleCenterUp = new Point(centerX + this.edgeRectSize / 2, this.Posy);
            //CompositePrototypeObject.this.Rectangles.add(RectangleCenterUp);
            g.fillRect(centerX, h - this.edgeRectSize / 2, this.edgeRectSize, this.edgeRectSize);
            //Point RectangleCenterDown = new Point(centerX + this.edgeRectSize / 2, h);
            //CompositePrototypeObject.this.Rectangles.add(RectangleCenterDown);
            g.fillRect(this.Posx - this.edgeRectSize / 2, centerY, this.edgeRectSize, this.edgeRectSize);
            Point RectangleCenterLeft = new Point(this.Posx, centerY + this.edgeRectSize / 2);
            //CompositePrototypeObject.this.Rectangles.add(RectangleCenterLeft);
            g.fillRect(w - this.edgeRectSize / 2, centerY, this.edgeRectSize, this.edgeRectSize);
            Point RectangleCenterRight = new Point(w, centerY + this.edgeRectSize / 2);
            //CompositePrototypeObject.this.Rectangles.add(RectangleCenterRight);
    }
    public void MoveToNewPosition(int deltaX, int deltaY){
        this.Posx+=deltaX;
        this.Posy+=deltaY;
        for(int i=0;i<Ports.size();i++)
        {
            Ports.get(i).MoveToNewPosition(deltaX,deltaY);
        }
        repaint();

    }
}
