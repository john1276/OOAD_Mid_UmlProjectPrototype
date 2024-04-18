package GUI;
import PrototypeObjects.CompositePrototypeObject;
import PrototypeObjects.LineWithArrow;
import PrototypeObjects.Mode;
import PrototypeObjects.Shape;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import javax.swing.*;


public class MainGUI extends JFrame{
    private Mode mode;
    private JPanel LeftPanel;
    private JLayeredPane DrawingCanvas;
    private JMenuBar MenuBar;
    private ArrayList<JButton> Buttons=new ArrayList<>();
    private ArrayList<JComponent> SelectedList=new ArrayList<>();
    private Point StartPoint=new Point();
    private boolean drag=false;
    private Point StartComponentPoint=new Point(1000,1000);
    private Point EndComponentPoint=new Point(0,0);
    private  Point LastPoint=new Point();
    private int SelectObjID=-1;
    public MainGUI()
    {
        setTitle("UML Editor");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        InitUI();
    }
    private void InitUI(){
        MenuBar = new JMenuBar();
        setLayout(new BorderLayout());    //为Frame窗口设置布局为BorderLayout
        setJMenuBar(MenuBar);

        MenuBar.add(createFileMenu());
        MenuBar.add(createEditMenu());
        LeftPanel=new JPanel();    //創建GridLayout
        //指定面板的布局为GridLayout，1行6列，间隙为5
        LeftPanel.setLayout(new GridLayout(6,1,5,5));
        addButtonToToolList("select", "Select");
        addButtonToToolList("association", "Association Line");
        addButtonToToolList("generalization", "Generalization Line");
        addButtonToToolList("composition", "Composition Line");
        addButtonToToolList("class", "Class");
        addButtonToToolList("use case", "Use Case");
        DrawingCanvas = new JLayeredPane();//當需要新增圖的時候，直接呼叫並add即可
        DrawingCanvas.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if(mode==Mode.UseCase||mode==Mode.Class)
                {
                    PrototypeObjects.Shape tmp=PrototypeObjects.Shape.Rectangle;
                    if(mode==Mode.UseCase)
                    {
                        tmp= PrototypeObjects.Shape.Oval;
                    }
                    CompositePrototypeObject Objt=new CompositePrototypeObject(tmp,e.getX(),e.getY(),40,40);
                    Objt.setVisible(true);
                    
                    DrawingCanvas.add(Objt,DrawingCanvas.getComponentCount());

                    DrawingCanvas.revalidate();
                    DrawingCanvas.repaint();
                }
                else if(mode==Mode.Select&&!drag)
                {
                    //做法1:看存不存在快速處理的方法
                    //做法2:透過ArrayList去抓
                    //做法2缺點:很燒效能
                    //目前做好了做法2

                    SelectedList.clear();
                    SelectObjID=FindComponent(e.getX(),e.getY());
                    DeActiveAllObjects();
                    if(SelectObjID!=-1)
                    {
                        Component Local = DrawingCanvas.getComponent(SelectObjID);
                        if( Local instanceof CompositePrototypeObject)
                        {
                            ((CompositePrototypeObject) Local).setDecorationVisible(true);
                            SelectedList.add((CompositePrototypeObject) Local);
                        }
                    }
                }

            }
        });
        DrawingCanvas.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                super.mouseDragged(e);
                if (drag && SelectObjID!=-1&&mode==Mode.Select) {
                    // 计算鼠标移动的距离
                    int deltaX = e.getX() - LastPoint.x;
                    int deltaY = e.getY() - LastPoint.y;
                    // 更新最后一次鼠标位置
                    LastPoint = e.getPoint();
                    Component Local = DrawingCanvas.getComponent(SelectObjID);
                    if( Local instanceof CompositePrototypeObject)
                    {
                        ((CompositePrototypeObject) Local).MoveToNewPosition(deltaX,deltaY);
                    }
                }
            }
        });
        DrawingCanvas.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                //處理lines
                //形狀還沒處理
                if (mode==Mode.Composition||mode==Mode.Generalization||mode==Mode.Association)
                {
                    SelectObjID=FindComponent(e.getX(),e.getY());
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
                        if( Local instanceof CompositePrototypeObject)
                        {
                            CompositePrototypeObject CProObj= (CompositePrototypeObject) Local;
                            LastPoint.x=CProObj.GetPositionX();
                            LastPoint.y=CProObj.GetPositionY();
                            CProObj.setDecorationVisible(true);
                        }
                    }
                }
            }
        });
        DrawingCanvas.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                if (mode==Mode.Composition||mode==Mode.Generalization||mode==Mode.Association)
                {
                    int tmp=FindComponent(e.getX(),e.getY());
                    if(SelectObjID!=-1&&tmp!=-1)
                    {
                        Component LocalStart = DrawingCanvas.getComponent(SelectObjID);
                        Component LocalEnd = DrawingCanvas.getComponent(tmp);
                        if(LocalStart instanceof CompositePrototypeObject&& LocalEnd instanceof CompositePrototypeObject)
                        {
                            StartPoint=((CompositePrototypeObject) LocalStart).FindClosestRectanglePosition(e.getX(),e.getY());
                            Point EndPoint=((CompositePrototypeObject) LocalEnd).FindClosestRectanglePosition(e.getX(),e.getY());
                            LineWithArrow NewArrow=new LineWithArrow(StartPoint, EndPoint, mode);
                            ((CompositePrototypeObject) LocalStart).AddStartArrow(NewArrow);
                            ((CompositePrototypeObject) LocalEnd).AddEndArrow(NewArrow);
                            NewArrow.setVisible(true);
                            DrawingCanvas.add(NewArrow,DrawingCanvas.getComponentCount());
                            //如果沒切Mode會出問題
                            //因為指到的是同一個地方
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
        });
        add(LeftPanel,BorderLayout.WEST);
        add(DrawingCanvas,BorderLayout.CENTER);
    }
    private JMenu createFileMenu(){
        JMenu fileMenu = new JMenu("File");
        return fileMenu;
    }
    private JMenu createEditMenu(){
        JMenu editMenu = new JMenu("Edit");
        JMenuItem Group =new JMenuItem("Group");
        Group.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (SelectedList.size()>1)
                {
                    CompositePrototypeObject NewGroupObj=new CompositePrototypeObject(StartComponentPoint,EndComponentPoint);
                    for(JComponent TempObj:SelectedList)
                    {
                        NewGroupObj.AddGroup(TempObj);
                    }
                    DrawingCanvas.add(NewGroupObj,DrawingCanvas.getComponentCount());
                    SelectedList.clear();
                    SelectedList.add(NewGroupObj);
                }
                //思考，防呆似乎會衍生其他問題
            }
        });
        editMenu.add(Group);
        JMenuItem UnGroup=new JMenuItem("UnGroup");
        UnGroup.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (SelectedList.size()==1&&((CompositePrototypeObject) SelectedList.get(0)).GetShape()== Shape.Composite) {
                    CompositePrototypeObject composite = (CompositePrototypeObject) SelectedList.get(0);
                    DrawingCanvas.remove(composite);
                    DrawingCanvas.invalidate();
                    DrawingCanvas.revalidate();
                    DrawingCanvas.repaint();
                    SelectedList.clear();

                }
            }
        });
        editMenu.add(UnGroup);
        JMenuItem ChangeName=new JMenuItem("change object name");
        ChangeName.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (SelectedList.size()==1)
                {
                    JFrame jFrame = new JFrame();
                    String getMessage = JOptionPane.showInputDialog(jFrame, "Enter your message");
                    ((CompositePrototypeObject) SelectedList.get(0)).ChangeName(getMessage);
                }
            }
        });
        editMenu.add(ChangeName);
        return editMenu;
    }
    private void handleModeChange(String Type) {
        // Implement mode change logic here.
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
                    otherButton.setBackground(Color.WHITE); // Reset other buttons to white.
                    otherButton.setForeground(Color.BLACK); // Reset text color to black.
                }
                handleModeChange(e.getActionCommand());
                if (mode==Mode.Class||mode==Mode.UseCase)
                {
                    button.setBackground(Color.BLACK);
                    button.setForeground(Color.WHITE);
                }
                 // You can handle mode changes here.
            }
        });
        Buttons.add(button); // Add the button to the list.
        LeftPanel.add(button);
    }
    private void SelectAllComponentInRange(Point Start, int x,int y){
        if(x<Start.x){
            int temp=x;
            x=Start.x;
            Start.x=temp;
        }
        if(y<Start.y){
            int temp=y;
            y= Start.y;
            Start.y=temp;
        }
        SelectedList.clear();
        for(int i=0;i<DrawingCanvas.getComponentCount();i++)
        {
            Component Local=DrawingCanvas.getComponent(i);
            if( Local instanceof CompositePrototypeObject)
            {
                CompositePrototypeObject CProtoObj= (CompositePrototypeObject) Local;
                if (Start.x <= CProtoObj.GetPositionX() && Start.y <= CProtoObj.GetPositionY() && x > CProtoObj.GetPositionX() + CProtoObj.GetWidth()&&y>CProtoObj.GetPositionY()+CProtoObj.GetHeight()) {
                    CProtoObj.setDecorationVisible(true);
                    SelectedList.add(CProtoObj);
                    if (CProtoObj.GetPositionX()<StartComponentPoint.x){
                        StartComponentPoint.x=CProtoObj.GetPositionX();
                    }
                    if (CProtoObj.GetPositionY()<StartComponentPoint.y) {
                        StartComponentPoint.y=CProtoObj.GetPositionY();
                    }
                    if (CProtoObj.GetPositionX()+CProtoObj.GetWidth()>EndComponentPoint.x){
                        EndComponentPoint.x=CProtoObj.GetPositionX()+CProtoObj.GetWidth();
                    }
                    if (CProtoObj.GetPositionY()+CProtoObj.GetHeight()>EndComponentPoint.y){
                        EndComponentPoint.y=CProtoObj.GetPositionY()+CProtoObj.GetHeight();
                    }
                }
            }
        }
    }
    private int FindComponent(int x,int y)
    {
        int tmp=-1;
        for(int i=0;i<DrawingCanvas.getComponentCount();i++)
        {
            Component Local=DrawingCanvas.getComponent(i);
            if( Local instanceof CompositePrototypeObject)
            {
                CompositePrototypeObject CProtoObj= (CompositePrototypeObject) Local;
                if(CProtoObj.IsInside(x,y))
                {
                    tmp=i;
                    break;
                }
            }
        }
        return tmp;
    }
    public void DeActiveAllObjects(){
        for (Component temp:DrawingCanvas.getComponents())
        {
            if (temp instanceof CompositePrototypeObject)
            {
                ((CompositePrototypeObject) temp).setDecorationVisible(false);
            }
        }
    }

    public static void main(String[] args) {
        MainGUI MainGUI = new MainGUI();
        MainGUI.setVisible(true);
    }


}
