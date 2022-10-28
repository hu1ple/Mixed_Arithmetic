import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Enumeration;

public class MainPanel extends JPanel  implements ActionListener {
    private JLabel label_welcome;
    private JLabel label_cfg;
    private JLabel[] labels ;
    private JRadioButton[] radioBt_range;//选择运算范围
    private JRadioButton[] radioBt_Fraction;//是否带小数
    private JRadioButton[] radioBt_Parentheses;//是否带括号单选框
    private JRadioButton[] radioBt_OperandsNum;//选择运算数个数
    private ButtonGroup[] buttonGroups; //上述四个单选框组
    private JCheckBox[] checkBox_type;  //复选框，分别确定每个题型是否选择
    private SystemFrame systemFrame;    //窗口
    private JButton bt_ok;              //确定按钮，点击后进入答题界面
    private JButton bt_exit;            //退出按钮，点击后关闭软件
    private JButton bt_viewHistory;
    private JTextField[] editNum;
    private int range;
    private boolean isFractionAllowed;  //是否允许小数
    private int OperandsNum = 0;            //运算数个数
    private int type[];                   //题目类型，1表示填空题，2表示选择题，3表示判断题
    private boolean isParenthesesAllowed;//是否允许括号;

    public MainPanel(SystemFrame systemFrame){
        this.systemFrame = systemFrame;
        InitAllComponents();
        bt_ok.addActionListener(this);
        bt_ok.setActionCommand("OK");
        bt_exit.addActionListener(this);
        bt_exit.setActionCommand("EXIT");
        bt_viewHistory.addActionListener(this);
        bt_viewHistory.setActionCommand("VIEW");

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand().equals("OK")){

            try {
                getConfig();
                systemFrame.setAnswerPanel(range,OperandsNum,isFractionAllowed,isParenthesesAllowed,type); //将用户设置传递到answerPanel中，方法是将参数传给SystemFrame
                System.out.println("确认设置");                                                             //由SystemFrame调用AnswerPanel的构造方法
                systemFrame.getMainPanel().setEnabled(false);       //界面切换，关闭本panel
                systemFrame.getMainPanel().setVisible(false);
                systemFrame.getAnswerPanel().setEnabled(true);
                systemFrame.getAnswerPanel().setVisible(true);      //打开答题panel
            } catch (MyException ex) {
                showWarningDialog(ex.getMessage());
                ex.printStackTrace();
            }
        }
        else if(e.getActionCommand().equals("EXIT")){

        }
        else if(e.getActionCommand().equals("VIEW")){
            try {
                showHistoryDialog();
            } catch (MyException ex) {
                showWarningDialog(ex.getMessage());
            }
        }
    }
    private void getConfig() throws MyException{ //当用户点击确定后，调用此方法，获得用户的设定，并存储在成员变量中


        if(radioBt_range[0].isSelected())                 //根据运算范围单选框设置range
            range = 10;
        else if(radioBt_range[1].isSelected())
            range = 100;
        else
            range = 1000;

        if(radioBt_Fraction[0].isSelected())            //根据选择是否带小数，设置isFractionAllowed的值
            isFractionAllowed = false;
        else
            isFractionAllowed = true;

        if(radioBt_Parentheses[0].isSelected())         //根据选择是否带括号，设置isParenthesesAllowed的值
            isParenthesesAllowed = false;
        else
            isParenthesesAllowed = true;

        boolean flag = false;                           //标记是否三种题型的数量都为0
        type = new int[3];
        for(int i=0;i<checkBox_type.length;i++){        //根据题型复选框和题目数量文本框，设置每个题型的数量，并保存在type中
            if(checkBox_type[i].isSelected()){
                String count = editNum[i].getText();
                if(!isNumeric(count))
                    throw  new MyException("请输入正确的题目数量!");
                else {
                    int a = Integer.parseInt(count);
                    type[i] = a;
                    flag = true;
                }
            }
        }
        for(int i=0;i<radioBt_OperandsNum.length;i++)   //根据运算数单选框确定OperandsNum的值
            if(radioBt_OperandsNum[i].isSelected())
                OperandsNum = i+2;
        if(OperandsNum == 2 && isParenthesesAllowed)
            throw new MyException("运算数个数为2时带括号无意义");
        if(!flag)
            throw new MyException("题目数量不能都为0");





    }
    private void InitAllComponents(){

        Font f1 = new Font("宋体",Font.PLAIN,20);
        InitGlobalFont(f1);
        this.label_welcome = new JLabel("欢迎使用混合运算自测系统");
       // label_welcome.setFont(f1);
        this.label_cfg = new JLabel("训练设置");
        this.labels = new JLabel[]{
                new JLabel("运算范围"), new JLabel("运算数个数"), new JLabel("是否带小数"),
                new JLabel("是否带括号"), new JLabel("题型与题量")
        };
        this.radioBt_range = new JRadioButton[]{                            //设置三个可选运算范围
                new JRadioButton("10以内"),
                new JRadioButton("100以内"),
                new JRadioButton("1000以内")
        };
        radioBt_range[0].setSelected(true);                                 //默认10以内
        this.radioBt_OperandsNum =new JRadioButton[]{                   //设置2-6个运算数个数选择
                new JRadioButton("2"),new JRadioButton("3"),
                new JRadioButton("4"),new JRadioButton("5"),new JRadioButton("6"),
        };
        radioBt_OperandsNum[0].setSelected(true);                           //默认2个运算数

        this.radioBt_Fraction = new JRadioButton[]{                 //设置小数选择单选框
                new JRadioButton("否"),
                new JRadioButton("是")
        };
        radioBt_Fraction[0].setSelected(true);                      //默认不带小数

        this.radioBt_Parentheses = new JRadioButton[]{
                new JRadioButton("否"),
                new JRadioButton("是")
        };                                                          //默认不带括号
        radioBt_Parentheses[0].setSelected(true);
        this.checkBox_type = new JCheckBox[]{
                new JCheckBox("填空题"),
                new JCheckBox("判断题"),
                new JCheckBox("选择题")
        };
        this.editNum = new JTextField[]{
                new JTextField(5),
                new JTextField(5),
                new JTextField(5)
        };
        //for(int i=0;i<editNum.length;i++)
          //  editNum[i].set
        this.buttonGroups = new ButtonGroup[]{
                new ButtonGroup(), new ButtonGroup(),new ButtonGroup(),new ButtonGroup()
        };
        for(int i=0;i<radioBt_range.length;i++)         buttonGroups[0].add(radioBt_range[i]);
        for(int i=0;i<radioBt_OperandsNum.length;i++)   buttonGroups[1].add(radioBt_OperandsNum[i]);
        for(int i=0;i<radioBt_Fraction.length;i++)      buttonGroups[2].add(radioBt_Fraction[i]);
        for(int i=0;i<radioBt_Parentheses.length;i++)   buttonGroups[3].add(radioBt_Parentheses[i]);
        bt_ok = new JButton("确定");bt_exit =new JButton("退出");bt_viewHistory = new JButton("查看历史记录");
        addToBoxLayout();               //将所有部件添加到箱式布局中


    }
    private void addToBoxLayout(){                  //将所有部件添加到箱式布局中
        Box BigBox = Box.createVerticalBox();
        //Box vBox =Box.createVerticalBox();
        Box[] hBox = new Box[]{
                Box.createHorizontalBox(),Box.createHorizontalBox(),Box.createHorizontalBox(),
                Box.createHorizontalBox(),Box.createHorizontalBox(),Box.createHorizontalBox()
        };
        for(int i=0;i<labels.length;i++){
            hBox[i].add(labels[i]);
        }
        for(int i=0;i<radioBt_range.length;i++)
                hBox[0].add(radioBt_range[i]);
        for(int i=0;i<radioBt_OperandsNum.length;i++)
                hBox[1].add(radioBt_OperandsNum[i]);
        for(int i=0;i<radioBt_Fraction.length;i++)
                hBox[2].add(radioBt_Fraction[i]);
        for(int i=0;i<radioBt_Parentheses.length;i++)
                hBox[3].add(radioBt_Parentheses[i]);
        Box[] miniHBox = new Box[]{Box.createHorizontalBox(),Box.createHorizontalBox(),Box.createHorizontalBox()};
        Box miniVBox = Box.createVerticalBox();
        for(int i=0;i<miniHBox.length;i++)
        {
            miniHBox[i].add(checkBox_type[i]);
            miniHBox[i].add(editNum[i]);
            miniVBox.add(miniHBox[i]);
        }

        hBox[4].add(miniVBox);
        hBox[5].add(bt_ok);
        hBox[5].add(bt_exit);
        hBox[5].add(bt_viewHistory);
        BigBox.add(label_welcome);
        BigBox.add(label_cfg);
        for(int i=0;i<hBox.length;i++)
            BigBox.add(hBox[i]);
        this.add(BigBox);
    }
    private void InitGlobalFont(Font font) {
        FontUIResource fontRes = new FontUIResource(font);
        for (Enumeration<Object> keys = UIManager.getDefaults().keys(); keys.hasMoreElements();) {
            Object key = keys.nextElement();
            Object value = UIManager.get(key);
            if (value instanceof FontUIResource) {
                UIManager.put(key, fontRes);
            }
        }
    }
    private void showWarningDialog(String message){
        final JDialog dialog = new JDialog(systemFrame, "警告", true);
        // 设置对话框的宽高
        dialog.setSize(300, 150);
        // 设置对话框大小不可改变
        dialog.setResizable(false);
        // 设置对话框相对显示的位置
        dialog.setLocationRelativeTo(systemFrame);

        // 创建一个标签显示消息内容
        JLabel messageLabel = new JLabel(message);

        // 创建一个按钮用于关闭对话框
        JButton okBtn = new JButton("确定");
        okBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 关闭对话框
                dialog.dispose();
            }
    });

        JPanel panel = new JPanel();

        // 添加组件到面板
        panel.add(messageLabel);
        panel.add(okBtn);

        // 设置对话框的内容面板
        dialog.setContentPane(panel);
        // 显示对话框
        dialog.setVisible(true);

    }
    private void showHistoryDialog() throws MyException{
        final JDialog dialog = new JDialog(systemFrame, "历史成绩", true);
        // 设置对话框的宽高
        dialog.setSize(300, 300);
        // 设置对话框大小不可改变
        dialog.setResizable(false);
        // 设置对话框相对显示的位置
        dialog.setLocationRelativeTo(systemFrame);
        JButton okBtn = new JButton("确定");
        okBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
            }
        });
        JTextArea textArea = new JTextArea();
        JPanel panel = new JPanel();
        try {
            // create a reader instance
            BufferedReader br = new BufferedReader(new FileReader("record.txt"));

            // read until end of file
            String line;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
                textArea.append(line+"\n");
            }
            // close the reader
            br.close();
        } catch (IOException ex) {
            throw new MyException("历史记录文件读取失败");
        }
     // 添加组件到面板
        panel.add(textArea);
        panel.add(okBtn);
        // 设置对话框的内容面板
        dialog.setContentPane(panel);
        // 显示对话框
        dialog.setVisible(true);
    }
    public static boolean isNumeric(String str){

        if(str .equals("") )
            return false;
        for (int i = 0; i < str.length(); i++){

            System.out.println(str.charAt(i));

            if (!Character.isDigit(str.charAt(i))&&str.charAt(i)!='.'){

                return false;

            }

        }

        return true;

    }

}
