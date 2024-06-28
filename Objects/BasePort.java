package Objects;

import Lines.BaseLine;

import java.util.ArrayList;

public class BasePort{
    int CenterX;
    int CenterY;
    int PosX;
    int PosY;
    int width=10;
    int height=10;
    ArrayList<BaseLine> StartLines=new ArrayList<BaseLine>();
    ArrayList<BaseLine> EndLines=new ArrayList<BaseLine>();
    public BasePort(int x,int y){
        PosX=x;
        PosY=y;
        CenterX=x+5;
        CenterY=y+5;
    }
    public double distance(int x, int y){
        return Math.sqrt((x-PosX)^2+(y-PosY)^2);
    }
    public void MoveToNewPosition(int deltaX, int deltaY){
        this.PosX+=deltaX;
        this.PosY+=deltaY;
        CenterX+=deltaX;
        CenterY+=deltaY;
        for(int i=0;i<StartLines.size();i++)
        {
            StartLines.get(i).MoveStartPoint(deltaX,deltaY);
        }
        for(int i=0;i<EndLines.size();i++)
        {
            EndLines.get(i).MoveEndPoint(deltaX,deltaY);
        }
    }
    public void AddArrowStart(BaseLine Arrow){
        StartLines.add(Arrow);
    }
    public void AddArrowEnd(BaseLine Arrow){
        EndLines.add(Arrow);
    }
}
