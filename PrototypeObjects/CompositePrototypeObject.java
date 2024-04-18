package PrototypeObjects;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashSet;

public class CompositePrototypeObject extends JComponent {
    private ShapePanel shapePanel;        // 用于绘制形状的面板
    private DecorationPanel decorationPanel;  // 用于绘制装饰（如对角线）的面板
    private int positionX;
    private int positionY;
    private int width;
    private int height;
    private Shape shape;
    private boolean select;
    private ArrayList<Point> Rectangles=new ArrayList<>();
    private ArrayList<JComponent> Group=new ArrayList<>();
    private ArrayList<JComponent> ArrowStartPoint=new ArrayList<>();
    private ArrayList<JComponent> ArrowEndPoint=new ArrayList<>();
    private String name=new String();
    public CompositePrototypeObject(Shape shape, int positionX, int positionY, int width, int height) {
        // 设置布局以覆盖子组件
        setLayout(new OverlayLayout(this));
        this.positionX=positionX;
        this.positionY=positionY;
        this.width=width;
        this.height=height;
        this.shape=shape;
        // 创建子面板并加入到容器中
        shapePanel = new ShapePanel();
        decorationPanel = new DecorationPanel();

        add(decorationPanel);
        add(shapePanel);

        // 设置容器的位置和大小
        setBounds(0,0, 800, 600);
    }
    public CompositePrototypeObject(Point Start, Point End) {
        // 设置布局以覆盖子组件
        setLayout(new OverlayLayout(this));
        this.positionX=Start.x;
        this.positionY=Start.y;
        this.width=End.x-Start.x;
        this.height=End.y-Start.y;
        // 创建子面板并加入到容器中
        shapePanel = new ShapePanel();
        decorationPanel = new DecorationPanel();
        shape=Shape.Composite;
        add(shapePanel);
        add(decorationPanel);

        // 设置容器的位置和大小
        setBounds(0,0, 800, 600);
    }
    public Shape GetShape() {
        return this.shape;
    }
    public void ChangeName(String newName)
    {
        this.name=newName;
        shapePanel.repaint();
    }
    public void MoveToNewPosition(int deltaX,int deltaY){
        this.positionX+=deltaX;
        this.positionY+=deltaY;
        shapePanel.repaint();
        decorationPanel.repaint();
        MoveSubObjects(deltaX,deltaY);
        MoveArrowObjects(deltaX,deltaY);
    }
    private void MoveSubObjects(int deltaX,int deltaY) {
        for(JComponent TempObj:this.Group) {
            if (TempObj instanceof CompositePrototypeObject)
                ((CompositePrototypeObject) TempObj).MoveToNewPosition(deltaX,deltaY);
        }
    }
    /*
    private void MoveArrowObjects(int deltaX, int deltaY) {
        // Track which arrows have been moved to avoid moving them multiple times.
        HashSet<LineWithArrow> movedArrows = new HashSet<>();

        for (JComponent TempObj : this.ArrowStartPoint) {
            if (TempObj instanceof LineWithArrow) {
                LineWithArrow arrow = (LineWithArrow) TempObj;
                if (!movedArrows.contains(arrow)) {
                    arrow.MoveStartPosition(deltaX, deltaY);
                    movedArrows.add(arrow); // Mark this arrow as moved.
                }
            }
        }

        for (JComponent TempObj : this.ArrowEndPoint) {
            if (TempObj instanceof LineWithArrow) {
                LineWithArrow arrow = (LineWithArrow) TempObj;
                if (!movedArrows.contains(arrow)) {
                    arrow.MoveEndPosition(deltaX, deltaY);
                    movedArrows.add(arrow); // Mark this arrow as moved.
                }
            }
        }
    }
    */
    private void MoveArrowObjects(int deltaX,int deltaY) {
        int count=1;
        for(JComponent TempObj:this.ArrowStartPoint)
        {
            if (TempObj instanceof LineWithArrow)
            {
                ((LineWithArrow) TempObj).MoveStartPosition(deltaX,deltaY);
            }
        }
        for(JComponent TempObj:this.ArrowEndPoint)
        {
            if (TempObj instanceof LineWithArrow)
            {
                ((LineWithArrow) TempObj).MoveEndPosition(deltaX,deltaY);
            }
        }
    }
    public void AddGroup(JComponent Component){
        Group.add(Component);
    }
    public void AddStartArrow(JComponent Component) {
        ArrowStartPoint.add(Component);
    }
    public void AddEndArrow(JComponent Component) {
        ArrowEndPoint.add(Component);
    }
    //maybe I should write a method to delete component
    public boolean IsInside(int x,int y) {
        return (x>=positionX&&y>=positionY&&x<=positionX+width&&y<=positionY+height);
    }
    public void SetNewPosition(int x,int y)
    {
        this.positionX=x;
        this.positionY=y;
        shapePanel.repaint();
        decorationPanel.repaint();
    }
    public int GetPositionX()
    {
        return this.positionX;
    }
    public int GetPositionY()
    {
        return this.positionY;
    }
    public int GetWidth()
    {
        return this.width;
    }
    public int GetHeight()
    {
        return this.height;
    }
    // 显示或隐藏装饰
    public void setDecorationVisible(boolean visible) {
        this.select=visible;
        decorationPanel.setVisible(visible);
    }
    public boolean IsSelected(){
        return this.select;
    }
    public Point FindClosestRectanglePosition(int x,int y)
    {
        double distance=10000.0;
        Point temp=new Point(x,y);
        Point result=new Point();
        for(Point points:Rectangles)
        {
            if(distance>temp.distance(points))
            {
                distance=temp.distance(points);
                result=points;
            }
        }
        return result;
    }
    // ShapePanel 内部类用于绘制形状
    private class ShapePanel extends JPanel {
        public ShapePanel() {
            setOpaque(false);
            setSize(800, 600);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.setColor(Color.GRAY);
            if (shape == Shape.Oval) {
                g.fillOval(positionX, positionY, width, height);
            } else if (shape == Shape.Rectangle) {
                g.fillRect(positionX, positionY, width, height);
            }
            g.setColor(Color.BLACK);
            g.drawString(name,positionX+width/4,positionY+height/4);
        }
    }

    // DecorationPanel 内部类用于绘制装饰
    private class DecorationPanel extends JPanel {
        private int edgeRectSize = 10; // 边缘矩形的尺寸
        public DecorationPanel() {
            setOpaque(false);
            setSize(width, height);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            int w = positionX+ width;
            int h = positionY+ height;

            g.setColor(Color.BLACK);
            if(shape==Shape.Rectangle)
            {
                g.drawRect(positionX,positionY, width, height);
                // 画对角线
                g.drawLine(positionX, positionY, w, h);
                g.drawLine(w, positionY, positionX, h);
            }
            else if(shape==Shape.Oval)
            {
                g.drawOval(positionX,positionY, width, height);
                int centerX= w-width/2;
                int centerY= h-height/2;
                int deltaX=(int)(width/(2*Math.sqrt(2)));
                int deltaY=(int)(height/(2*Math.sqrt(2)));
                g.drawLine(centerX+deltaX,centerY+deltaY,centerX-deltaX,centerY-deltaY);
                g.drawLine(centerX-deltaX,centerY+deltaY,centerX+deltaX,centerY-deltaY);
            }
            // 画边缘矩形
            int rectX = (positionX+w - edgeRectSize) / 2;
            int rectY = (positionY+h - edgeRectSize) / 2;
            g.fillRect(rectX, positionY - edgeRectSize/2, edgeRectSize, edgeRectSize); // 上
            Point RectangleCenterUp=new Point(rectX+edgeRectSize/2, positionY);
            Rectangles.add(RectangleCenterUp);
            g.fillRect(rectX, h-edgeRectSize/2, edgeRectSize, edgeRectSize); // 下
            Point RectangleCenterDown=new Point(rectX+edgeRectSize/2, h);
            Rectangles.add(RectangleCenterDown);
            g.fillRect(positionX - edgeRectSize/2, rectY, edgeRectSize, edgeRectSize); // 左
            Point RectangleCenterLeft=new Point(positionX, rectY+edgeRectSize/2);
            Rectangles.add(RectangleCenterLeft);
            g.fillRect(w-edgeRectSize/2, rectY, edgeRectSize, edgeRectSize); // 右
            Point RectangleCenterRight=new Point(w, rectY+edgeRectSize/2);
            Rectangles.add(RectangleCenterRight);
        }
    }
}
