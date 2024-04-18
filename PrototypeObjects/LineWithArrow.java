package PrototypeObjects;

import javax.swing.*;
import java.awt.*;

public class LineWithArrow extends JPanel {
    private Point Start;
    private Point End;
    private Mode Action;
    private int ArrowLength=5;
    //+cos45+sin45,-,-
    public LineWithArrow(Point StartPoint, Point EndPoint,Mode Command){
        Action=Command;
        this.Start = new Point(StartPoint);
        this.End = new Point(EndPoint);
        setBounds(0, 0, 800, 600);
        setOpaque(false);
    }
    public void MoveStartPosition(int deltaX,int deltaY) {
        this.Start.x+=deltaX;
        this.Start.y+=deltaY;
        repaint();
    }
    public void MoveEndPosition(int deltaX, int deltaY) {
        this.End.x+=deltaX;
        this.End.y+=deltaY;
        repaint();
    }
    private void DrawGeneralizationArrows(Graphics g, Point Start, Point End){
        g.drawLine(Start.x,Start.y,End.x,End.y);
        int x1=Start.x, y1=Start.y, x2=End.x, y2=End.y;
        int dx = x2 - x1, dy = y2 - y1;
        int d=ArrowLength,h=ArrowLength;
        double D = Math.sqrt(dx*dx + dy*dy);
        double xm = D - d, xn = xm, ym = h, yn = -h, x;
        double sin = dy / D, cos = dx / D;

        x = xm*cos - ym*sin + x1;
        ym = xm*sin + ym*cos + y1;
        xm = x;

        x = xn*cos - yn*sin + x1;
        yn = xn*sin + yn*cos + y1;
        xn = x;

        int[] xpoints = {x2, (int) xm, (int) xn};
        int[] ypoints = {y2, (int) ym, (int) yn};

        g.drawLine(x1, y1, x2, y2);
        g.drawPolygon(xpoints, ypoints, 3);
    }
    private void DrawCompositionArrows(Graphics g, Point Start, Point End){
        g.drawLine(Start.x,Start.y,End.x,End.y);
        int x1=Start.x, y1=Start.y, x2=End.x, y2=End.y;
        int dx = x2 - x1, dy = y2 - y1;
        int d=ArrowLength,h=ArrowLength;
        double D = Math.sqrt(dx*dx + dy*dy);
        double xm = D - d, xn = xm, ym = h, yn = -h, x;
        double sin = dy / D, cos = dx / D;

        x = xm*cos - ym*sin + x1;
        ym = xm*sin + ym*cos + y1;
        xm = x;

        x = xn*cos - yn*sin + x1;
        yn = xn*sin + yn*cos + y1;
        xn = x;

        int[] xpoints = {x2, (int) xm, (int) xn};
        int[] ypoints = {y2, (int) ym, (int) yn};

        g.drawLine(x1, y1, x2, y2);
        g.fillPolygon(xpoints, ypoints, 3);
    }
    private void DrawAssociationArrows(Graphics g, Point Start, Point End){
        g.drawLine(Start.x,Start.y,End.x,End.y);
        int x1=Start.x, y1=Start.y, x2=End.x, y2=End.y;
        int dx = x2 - x1, dy = y2 - y1;
        int d=ArrowLength,h=ArrowLength;
        double D = Math.sqrt(dx*dx + dy*dy);
        double xm = D - d, xn = xm, ym = h, yn = -h, x;
        double sin = dy / D, cos = dx / D;

        x = xm*cos - ym*sin + x1;
        ym = xm*sin + ym*cos + y1;
        xm = x;

        x = xn*cos - yn*sin + x1;
        yn = xn*sin + yn*cos + y1;
        xn = x;

        g.drawLine(x1, y1, x2, y2);
        g.drawLine(x2,y2,(int)xm,(int)ym);
        g.drawLine(x2,y2,(int)xn,(int)yn);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.BLACK);
        if(Action==Mode.Composition)
        {
            DrawCompositionArrows(g,Start,End);
        }
        else if(Action==Mode.Association)
        {
            DrawAssociationArrows(g,Start,End);
        }
        else if(Action==Mode.Generalization)
        {
            DrawGeneralizationArrows(g,Start,End);
        }
    }

}
