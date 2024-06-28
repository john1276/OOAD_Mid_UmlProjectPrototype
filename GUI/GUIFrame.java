package GUI;

import Enums.Mode;
import Lines.AssociationLine;
import Lines.BaseLine;
import Lines.CompositionLine;
import Lines.GeneralizationLine;
import Objects.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class GUIFrame extends JFrame{
    private JPanel LeftPanel;
    private JLayeredPane DrawingCanvas;
    private JMenuBar MenuBar;
    private ArrayList<JButton> Buttons = new ArrayList();
    Mode mode;
    boolean drag=false;
    private int SelectObjID=-1;
    private ArrayList<BaseObject> SelectedList=new ArrayList<>();
    private Point LastPoint=new Point();
    private Point StartPoint=new Point();
    private Point StartGroupPoint=new Point();
    private Point EndGroupPoint=new Point();
    public GUIFrame() {
        this.setTitle("UML Editor");
        this.setSize(800, 600);
        this.setDefaultCloseOperation(3);
        this.InitUI();
    }
    private void InitUI() {

        this.MenuBar = new JMenuBar();
        this.setLayout(new BorderLayout());
        this.setJMenuBar(this.MenuBar);
        this.MenuBar.add(this.createFileMenu());
        this.MenuBar.add(this.createEditMenu());
        SetLeftPanel();
        this.DrawingCanvas = new JLayeredPane();
        this.DrawingCanvas.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                MouseClickEvent(e);
            }
        });
        this.DrawingCanvas.addMouseMotionListener(new MouseAdapter() {
            public void mouseDragged(MouseEvent e) {
                super.mouseDragged(e);
                MouseDragEvent(e);
            }
        });
        this.DrawingCanvas.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                MousePressedEvent(e);
            }
        });
        this.DrawingCanvas.addMouseListener(new MouseAdapter() {
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                MouseReleasedEvent(e);
            }
        });
        this.add(this.LeftPanel, "West");
        this.add(this.DrawingCanvas, "Center");
    }
    private void SetLeftPanel(){
        this.LeftPanel = new JPanel();
        this.LeftPanel.setLayout(new GridLayout(6, 1, 5, 5));
        this.addButtonToToolList("select", "Select");
        this.addButtonToToolList("association", "Association Line");
        this.addButtonToToolList("generalization", "Generalization Line");
        this.addButtonToToolList("composition", "Composition Line");
        this.addButtonToToolList("class", "Class");
        this.addButtonToToolList("use case", "Use Case");
    }
    private void handleModeChange(String Type) {
        System.out.println("Mode changed to: " + Type);
        switch(Type)
        {
            case "select":
                mode=Mode.Select;
                break;
            case "association":
                mode=Mode.Association;
                break;
            case "generalization":
                mode=Mode.Generalization;
                break;
            case "composition":
                mode=Mode.Composition;
                break;
            case "class":
                mode=Mode.Class;
                break;
            case "use case":
                mode=Mode.UseCase;
        }
    }
    private void addButtonToToolList(String actionCommand, String buttonText) {
        JButton button = new JButton(buttonText);
        button.setActionCommand(actionCommand);
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Tool selected: " + e.getActionCommand());
                for (JButton otherButton : Buttons) {
                    otherButton.setBackground(Color.WHITE);
                    otherButton.setForeground(Color.BLACK);
                }
                handleModeChange(e.getActionCommand());
                if (mode==Mode.Class||mode==Mode.UseCase)
                {
                    button.setBackground(Color.BLACK);
                    button.setForeground(Color.WHITE);
                }
            }
        });
        this.Buttons.add(button);
        this.LeftPanel.add(button);
    }
    private JMenu createEditMenu() {
        JMenu editMenu = new JMenu("Edit");
        JMenuItem Group = new JMenuItem("Group");
        Group.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                if (SelectedList.size()>1)
                {
                    int width=EndGroupPoint.x-StartPoint.x;
                    int height=EndGroupPoint.y-StartPoint.y;
                    BaseObject NewGroupObj=new GropObject(StartGroupPoint.x,StartPoint.y,width,height);
                    for(BaseObject TempObj:SelectedList)
                    {
                        ((GropObject)NewGroupObj).AddObj(TempObj);
                    }
                    DrawingCanvas.add(NewGroupObj,DrawingCanvas.getComponentCount());
                    SelectedList.clear();
                    SelectedList.add(NewGroupObj);
                }
            }
        });
        editMenu.add(Group);
        JMenuItem UnGroup = new JMenuItem("UnGroup");
        UnGroup.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (SelectedList.size()==1&&SelectedList.get(0) instanceof GropObject) {
                    GropObject composite = (GropObject) SelectedList.get(0);
                    DrawingCanvas.remove(composite);
                    DrawingCanvas.invalidate();
                    DrawingCanvas.revalidate();
                    DrawingCanvas.repaint();
                    SelectedList.clear();
                }
            }
        });
        editMenu.add(UnGroup);
        JMenuItem ChangeName = new JMenuItem("change object name");
        ChangeName.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (SelectedList.size() == 1)
                {
                    JFrame jFrame = new JFrame();
                    String getMessage = JOptionPane.showInputDialog(jFrame, "Enter your message");
                    if (getMessage != null){
                        SelectedList.get(0).ChangeName(getMessage);
                    }

                }

            }
        });
        editMenu.add(ChangeName);
        return editMenu;
    }

    private JMenu createFileMenu() {
        JMenu fileMenu = new JMenu("File");
        return fileMenu;
    }
    public void MouseClickEvent(MouseEvent e){
        //DeActiveAllObjects();
        if(mode==Mode.UseCase||mode==Mode.Class)
        {

            BaseObject Obj;
            if(mode==Mode.UseCase)
            {
                Obj=new UseCase(e.getX(),e.getY());
            }
            else {
                Obj=new BaseClass(e.getX(),e.getY());
            }
            Obj.setVisible(true);
            DrawingCanvas.add(Obj,DrawingCanvas.getComponentCount());
            DrawingCanvas.revalidate();
            DrawingCanvas.repaint();
        }
        else if(mode==Mode.Select&&!drag)
        {
            SelectedList.clear();
            SelectObjID=FindComponent(e.getX(),e.getY());
            DeActiveAllObjects();
            if(SelectObjID!=-1)
            {
                Component Local = DrawingCanvas.getComponent(SelectObjID);
                if( Local instanceof BaseObject)
                {
                    ((BaseObject) Local).setDecorationVisible(true);
                    SelectedList.add((BaseObject) Local);
                }
            }
        }
    }
    public void MouseDragEvent(MouseEvent e){

        if (drag && SelectObjID!=-1&&mode==Mode.Select) {
            int deltaX = e.getX() - LastPoint.x;
            int deltaY = e.getY() - LastPoint.y;
            LastPoint = e.getPoint();
            Component Local = DrawingCanvas.getComponent(SelectObjID);
            if( Local instanceof BaseObject)
            {
                ((BaseObject) Local).MoveToNewPosition(deltaX,deltaY);
            }
        }
    }
    public void MousePressedEvent(MouseEvent e){
        if (mode==Mode.Composition||mode==Mode.Generalization||mode==Mode.Association)
        {
            SelectObjID=FindComponent(e.getX(),e.getY());
            Component LocalStart = DrawingCanvas.getComponent(SelectObjID);
            StartPoint=((BaseObject) LocalStart).FindClosestRectanglePosition(e.getX(),e.getY());
        }
        else if(mode==Mode.Select)
        {
            drag=true;
            SelectObjID=FindComponent(e.getX(),e.getY());
            DeActiveAllObjects();
            if(SelectObjID==-1)
            {
                StartPoint=e.getPoint();
            }
            else{
                StartPoint=new Point();
                Component Local = DrawingCanvas.getComponent(SelectObjID);
                if(Local instanceof BaseObject CProObj)
                {
                    LastPoint = e.getPoint();
                    CProObj.setDecorationVisible(true);
                }
            }
        }
    }
    public void MouseReleasedEvent(MouseEvent e){
        if (mode==Mode.Composition||mode==Mode.Generalization||mode==Mode.Association)
        {
            int tmp=FindComponent(e.getX(),e.getY());
            if(SelectObjID!=-1&&tmp!=-1)
            {
                Component LocalStart = DrawingCanvas.getComponent(SelectObjID);
                Component LocalEnd = DrawingCanvas.getComponent(tmp);
                if(LocalStart instanceof BaseObject&& LocalEnd instanceof BaseObject)
                {
                    Point EndPoint=((BaseObject) LocalEnd).FindClosestRectanglePosition(e.getX(),e.getY());
                    BaseLine NewArrow=new BaseLine(StartPoint.x,StartPoint.y, EndPoint.x,EndPoint.y);
                    if(mode==Mode.Composition)
                    {
                        NewArrow=new CompositionLine(StartPoint.x,StartPoint.y, EndPoint.x,EndPoint.y);
                    }
                    if(mode==Mode.Generalization){
                        NewArrow=new GeneralizationLine(StartPoint.x,StartPoint.y, EndPoint.x,EndPoint.y);
                    }
                    if(mode==Mode.Association){
                        NewArrow=new AssociationLine(StartPoint.x,StartPoint.y, EndPoint.x,EndPoint.y);
                    }
                    ((BaseObject) LocalStart).AddArrowStartPointToPort(StartPoint.x,StartPoint.y,NewArrow);
                    ((BaseObject) LocalEnd).AddArrowEndPointToPort(EndPoint.x,EndPoint.y,NewArrow);
                    NewArrow.setVisible(true);
                    DrawingCanvas.add(NewArrow,DrawingCanvas.getComponentCount());
                }
            }
            else
            {
                StartPoint=new Point();
            }
        }
        else if(mode==Mode.Select&&drag)
        {
            drag=false;
            if(SelectObjID==-1)
                SelectAllComponentInRange(StartPoint,e.getX(),e.getY());
        }
    }
    private void SelectAllComponentInRange(Point Start, int x, int y) {
        int i;
        if (x < Start.x) {
            i = x;
            x = Start.x;
            Start.x = i;
        }

        if (y < Start.y) {
            i = y;
            y = Start.y;
            Start.y = i;
        }
        SelectedList.clear();
        StartGroupPoint.x=10000;
        StartGroupPoint.y=10000;
        EndGroupPoint.x=0;
        EndGroupPoint.y=0;
        for(int j=0;j<DrawingCanvas.getComponentCount();j++)
        {
            Component Local=DrawingCanvas.getComponent(j);
            if( Local instanceof BaseObject)
            {
                BaseObject CProtoObj= (BaseObject) Local;
                if (Start.x <= CProtoObj.getPosX() && Start.y <= CProtoObj.getPosY() && x > CProtoObj.getPosX() + CProtoObj.getW()&&y>CProtoObj.getPosY()+CProtoObj.getH()) {
                    CProtoObj.setDecorationVisible(true);
                    System.out.println("temp");
                    SelectedList.add(CProtoObj);
                    if (CProtoObj.getPosX()<StartGroupPoint.x){
                        StartGroupPoint.x=CProtoObj.getPosX();
                    }
                    if (CProtoObj.getPosY()<StartGroupPoint.y) {
                        StartGroupPoint.y=CProtoObj.getPosY();
                    }
                    if (CProtoObj.getPosX()+CProtoObj.getW()>EndGroupPoint.x){
                        EndGroupPoint.x=CProtoObj.getPosX()+CProtoObj.getW();
                    }
                    if (CProtoObj.getPosY()+CProtoObj.getH()>EndGroupPoint.y){
                        EndGroupPoint.y=CProtoObj.getPosY()+CProtoObj.getH();
                    }
                }
            }
        }
    }
    public void DeActiveAllObjects(){
        for (Component temp:DrawingCanvas.getComponents())
        {
            if (temp instanceof BaseObject)
            {
                ((BaseObject) temp).setDecorationVisible(false);
            }
        }
    }
    private int FindComponent(int x,int y)
    {
        int tmp=-1;
        for(int i=DrawingCanvas.getComponentCount()-1;i>=0;i--)
        {
            Component Local=DrawingCanvas.getComponent(i);
            if( Local instanceof BaseObject)
            {
                BaseObject CProtoObj= (BaseObject) Local;
                if(CProtoObj.IsInside(x,y))
                {
                    tmp=i;
                    break;
                }
            }
        }
        return tmp;
    }
}