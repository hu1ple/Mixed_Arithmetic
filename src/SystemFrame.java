import javax.swing.*;

public class SystemFrame extends JFrame {                       //自定义窗口类，继承自JFrame，主要有两个成员变量：
    private MainPanel mainPanel;                                //mainPanel为主面板，用户在此进行训练设置
    private AnswerPanel answerPanel;                            //answerPanel为作答面板，用户在此进行作答
    //通过设置这两个paanel的属性，来实现界面切换，具体见MainPanel、AnswerPanel类中的对actionPerformed方法的重写

    public SystemFrame(){                                       //初始化窗口和用户主面板mainPanel初始化
        mainPanel = new MainPanel(this);
        this.add(mainPanel);
        mainPanel.setBounds(0,0,800,800);
        this.setTitle("混合计算自测系统");
        this.setSize(800,800);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setVisible(true);
    }
    public MainPanel getMainPanel() {
        return mainPanel;
    }

    public AnswerPanel getAnswerPanel() {
        return answerPanel;
    }
    public void setAnswerPanel(int range ,int OperandsNum, boolean isFractionAllowed,       //当用户在mainPanel中点击确认按钮时，调用此方法，将
                              boolean isParenthesesAllowed,int type[]  ){                   //用户的训练设置参数传进来，并初始化answerPanel。
        answerPanel = new AnswerPanel(range ,OperandsNum,isFractionAllowed,
                                        isParenthesesAllowed,type,this);
        this.add(answerPanel);
        mainPanel.setBounds(0,0,800,800);

    }
    public static void main(String[] args){                             //主方法
            new SystemFrame();

    }
}
