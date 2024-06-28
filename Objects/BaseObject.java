package Objects;
import Lines.BaseLine;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.OverlayLayout;
public class BaseObject extends JComponent{
    //BaseShape Shape;
    String name="";
    DecorateObject DecObj;
    int width=40;
    int height=40;
    int Posx;
    int Posy;
    boolean select=false;
    public BaseObject(int x,int y)
    {
        this.Posx=x;
        this.Posy=y;
        init();
    }
    public BaseObject(int x,int y,int width,int height)
    {
        this.Posx=x;
        this.Posy=y;
        this.width=width;
        this.height=height;
        init();
    }
    void init(){
        this.setLayout(new OverlayLayout(this));
        this.setOpaque(false);
        this.DecObj=new DecorateObject(Posx,Posy,width,height);
        this.add(this.DecObj);
        this.setSize(1000, 1000);
        this.DecObj.setVisible(false);
        //this.setBounds(0, 0, 1000,1000);
    }
    public void SetName(String name){
        this.name=name;
        repaint();
    }
    public void setDecorationVisible(boolean visible) {
        this.select = visible;
        this.DecObj.setVisible(visible);
        this.DecObj.setOpaque(!visible);
        this.DecObj.repaint();
        this.repaint();
    }
    public boolean IsSelected() {
        return this.select;
    }
    public boolean IsInside(int x,int y) {
        return (x>=this.Posx&&y>=this.Posy&&x<=this.Posx+width&&y<=this.Posy+height);
    }
    public void MoveToNewPosition(int deltaX, int deltaY){
        this.Posx+=deltaX;
        this.Posy+=deltaY;
        this.DecObj.MoveToNewPosition(deltaX,deltaY);
        repaint();
        /*
        MoveSubObjects(deltaX,deltaY);
        MoveArrowObjects(deltaX,deltaY);
        */
    }
    public int getPosX(){
        return this.Posx;
    }
    public int getPosY(){
        return this.Posy;
    }
    public int getH(){
        return this.height;
    }
    public int getW(){
        return this.width;
    }
    public void ChangeName(String newName)
    {
        this.name=newName;
        repaint();
    }
    public Point FindClosestRectanglePosition(int x,int y)
    {
        double distance=100000000.0;
        Point result=new Point();
        for(int i=0;i<this.DecObj.Ports.size();i++)
        {
            BasePort temp=this.DecObj.Ports.get(i);
            if(distance>temp.distance(x,y))
            {
                distance=temp.distance(x,y);
                result.x=temp.PosX;
                result.y=temp.PosY;
            }
        }
        return result;
    }
    public int FindClosetPort(int x, int y)
    {
        double distance=100000000.0;
        int tmp=0;
        for(int i=0;i<this.DecObj.Ports.size();i++)
        {
            BasePort temp=this.DecObj.Ports.get(i);
            if(distance>temp.distance(x,y))
            {
                distance=temp.distance(x,y);
                tmp=i;
            }
        }
        return tmp;
    }
    public void AddArrowStartPointToPort(int x,int y,BaseLine Arrow){
        int ID=FindClosetPort(x, y);
        this.DecObj.Ports.get(ID).AddArrowStart(Arrow);
    }
    public void AddArrowEndPointToPort(int x,int y,BaseLine Arrow){
        int ID=FindClosetPort(x, y);
        this.DecObj.Ports.get(ID).AddArrowEnd(Arrow);
    }
    public void PrintPort(){
        for(int i=0;i<this.DecObj.Ports.size();i++)
        {
            System.out.println("Port X:"+this.DecObj.Ports.get(i).PosX+", Y:"+this.DecObj.Ports.get(i).PosY);
        }
    }

}
