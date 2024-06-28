package Lines;

import javax.swing.*;
import java.awt.*;

public class BaseLine extends JPanel {
    int StartX;
    int StartY;
    int EndX;
    int EndY;
    int ArrowLength=5;
    public BaseLine(int Sx,int Sy,int Ex,int Ey){
        StartX=Sx;
        StartY=Sy;
        EndX=Ex;
        EndY=Ey;
        setBounds(0, 0, 800, 600);
        setOpaque(false);
    }
    @Override
    public void paintComponent(Graphics g){

    }
    public void MoveStartPoint(int deltaX,int deltaY){
        this.StartX+=deltaX;
        this.StartY+=deltaY;
        repaint();
    }
    public void MoveEndPoint(int deltaX,int deltaY){
        this.EndX+=deltaX;
        this.EndY+=deltaY;
        repaint();
    }
}
