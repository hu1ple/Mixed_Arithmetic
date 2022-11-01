import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AnswerPanel  extends JPanel implements ActionListener {
    private SystemFrame systemFrame;
    private FillBlank_Questions[] Questions;
    private int NumOfCorrect;
    private int score;
    private String ID;
    private Box VBox;
    private JButton bt_ok;
    private JButton bt_back;
    private JButton bt_showNext;
    private JButton bt_showLast;
    private JButton bt_outputTofile;
    private int index;


    public AnswerPanel(int range, int OperandsNum, boolean isFractionAllowed,
                       boolean isParenthesesAllowed, int type[], SystemFrame systemFrame,String ID) {
        this.ID = ID;
        this.systemFrame = systemFrame;                         //初始化题目设置
        this.VBox = Box.createVerticalBox();
        index = -1;
        Generate_Calculations[] grt_calctions = new Generate_Calculations[]{
                new Generate_Calculations(range, isFractionAllowed, OperandsNum, isParenthesesAllowed, type[0]),
                new Generate_Calculations(range, isFractionAllowed, OperandsNum, isParenthesesAllowed, type[1]),
                new Generate_Calculations(range, isFractionAllowed, OperandsNum, isParenthesesAllowed, type[2])
        };
        String[][] calculations;
        calculations = new String[][]{
                grt_calctions[0].getCalculations(), grt_calctions[1].getCalculations(), grt_calctions[2].getCalculations()
        };
        Questions = new FillBlank_Questions[type[0] + type[1] + type[2]];
        if (type[0] != 0) {
            for (int i = 0; i < type[0]; i++) {
                Questions[i] = new FillBlank_Questions(calculations[0][i]);
                Questions[i].gethBox().setVisible(false);
                this.VBox.add(Questions[i].gethBox());

            }
        }
        if (type[1] != 0) {
            for (int i = 0; i < type[1]; i++) {
                Questions[type[0] + i] = new TorF_Questions(calculations[1][i]);
                Questions[i].gethBox().setVisible(false);
                this.VBox.add(Questions[i+type[0]].gethBox());

            }
        }
        if (type[2] != 0) {
            for (int i = 0; i < type[2]; i++) {
                Questions[i + type[0] + type[1]] = new Choice_Questions(calculations[2][i]);
                Questions[i].gethBox().setVisible(false);
                this.VBox.add(Questions[i+type[0] + type[1]].gethBox());

            }
        }


        bt_back = new JButton("返回");                    //添加返回按钮
        bt_back.addActionListener(this);
        bt_back.setActionCommand("back");
        bt_ok = new JButton("提交");
        bt_ok.addActionListener(this);
        bt_ok.setActionCommand("ok");
        bt_showLast = new JButton("查看上一组");
        bt_showLast.addActionListener(this);
        bt_showLast.setActionCommand("last");
        bt_showNext = new JButton("查看下一组");
        bt_showNext.addActionListener(this);
        bt_showNext.setActionCommand("next");
        bt_outputTofile = new JButton("保存结果到文件");
        bt_outputTofile.addActionListener(this);
        bt_outputTofile.setActionCommand("file");


        this.VBox.add(bt_ok);
        this.VBox.add(bt_back);
        this.VBox.add(bt_showLast);
        this.VBox.add(bt_showNext);
        this.VBox.add(bt_outputTofile);
        this.add(VBox);

        try {
            showNextGroup();
            refresh();
            int a =0;
        } catch (MyException e) {
            e.printStackTrace();
        }

    }

    private void showNextGroup() throws MyException {
        if (index == Questions.length - 1)
            throw new MyException("已经是最后一组!");
        else {
           // this.removeAll();
            for(int i = index -index %10;i<=index && index>=0;i++)
                Questions[i].gethBox().setVisible(false);

            //this.setVisible(false);
            int i;
            for (i = index + 1; i < Questions.length && i <= index + 10; i++) {
                //this.VBox.add(Questions[i].gethBox());
                Questions[i].gethBox().setVisible(true);
            }
            index = i-1;
            refresh();
            //this.VBox.add(bt_ok);
            //this.VBox.add(bt_back);
            //this.VBox.add(bt_showLast);
            //this.VBox.add(bt_showNext);
            //this.add(VBox);
            //this.setVisible(true);

        }

    }

    private void showLastGroup() throws MyException {
        if (index < 10)
            throw new MyException("已经是第一组！");
        else {
            for(int i = index -index %10;i<=index && index>=0;i++)
                Questions[i].gethBox().setVisible(false);
            //this.removeAll();
           // this.setVisible(false);
            int i;
            for (i = index - index % 10 - 10; i < index - index % 10; i++) {
                Questions[i].gethBox().setVisible(true);
            }
            index = i-1;

        }
    }
    private void refresh(){
        this.setEnabled(false);
        this.setVisible(false);
        this.setEnabled(true);
        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {            //按钮监听，点击返回设置页面
        if (e.getActionCommand().equals("back")) {
            systemFrame.getAnswerPanel().setEnabled(false);       //界面切换，关闭本panel
            systemFrame.getAnswerPanel().setVisible(false);
            systemFrame.getMainPanel().setEnabled(true);
            systemFrame.getMainPanel().setVisible(true);      //打开答题panel
        } else if (e.getActionCommand().equals("ok")) {
            if(NumOfCorrect!=0) {
                showResultDialog("请不要重复提交!");
                return ;
            }
            NumOfCorrect = 0;
            for (int i = 0; i < Questions.length; i++) {
                Questions[i].ShowResult();
                if(Questions[i].isCorrect)  NumOfCorrect++;
            }
            String Analysis ="本次训练共"+Integer.toString(Questions.length)+"道题\n"+
                    "你共做对了"+Integer.toString(NumOfCorrect)+"道题\n"+
                    "你的得分为:"+Integer.toString(100*NumOfCorrect/Questions.length);
            showResultDialog(Analysis);
            refresh();
        } else if (e.getActionCommand().equals("next")) {
            try {
                showNextGroup();
            } catch (MyException ex) {
                showResultDialog(ex.getMessage());
                ex.printStackTrace();
            }
        } else if (e.getActionCommand().equals("last")) {
            try {
                showLastGroup();
            } catch (MyException ex) {
                showResultDialog(ex.getMessage());
                ex.printStackTrace();
            }
        }
        else if(e.getActionCommand().equals("file")){
            if(NumOfCorrect == 0){
                showResultDialog("你还没有进行提交");
                return ;
            }
            try{
                Date date = new Date(System.currentTimeMillis());

                SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
                String time = String.valueOf(formatter.format(date));
                FileWriter fileWriter = new FileWriter(ID+"_Exercises.txt",true);
                BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
                bufferedWriter.write("\n\n*********************新的一轮训练开始:"+time+"*************"+"\n");
                bufferedWriter.close();
                fileWriter.close();
                for(int i=0;i< Questions.length;i++)
                    Questions[i].OutputToFile(ID+"_Exercises.txt");
                fileWriter = new FileWriter(ID+"_Exercises.txt",true);
                bufferedWriter = new BufferedWriter(fileWriter);
                String Analysis = "本次训练共"+Integer.toString(Questions.length) +"道题\n"
                                +"您共答对了"+Integer.toString(NumOfCorrect)+"道题\n"
                                +"您的最终得分为:"+Integer.toString(100*NumOfCorrect/Questions.length)+"\n";

                bufferedWriter.write(Analysis);
                bufferedWriter.write("*********************本轮训练结束*********************\n");
                bufferedWriter.close();
                fileWriter.close();

                fileWriter =new FileWriter(ID+"_record.txt",true);
                bufferedWriter=new BufferedWriter(fileWriter);
                bufferedWriter.write(time+":"+Integer.toString(100*NumOfCorrect/Questions.length)+"\n");
                bufferedWriter.close();
                fileWriter.close();
                showResultDialog("训练记录保存成功！");
            } catch (MyException ex) {
                showResultDialog(ex.getMessage());
                ex.printStackTrace();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }


    private void showResultDialog(String message) {
        final JDialog dialog = new JDialog(systemFrame, "结果", true);
        // 设置对话框的宽高
        dialog.setSize(300, 150);
        // 设置对话框大小不可改变
        dialog.setResizable(false);
        // 设置对话框相对显示的位置
        dialog.setLocationRelativeTo(systemFrame);

        // 创建一个标签显示消息内容
        //JLabel messageLabel = new JLabel(message);
        JTextArea textArea = new JTextArea(message);
        textArea.setEditable(false);
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
        panel.add(textArea);
        panel.add(okBtn);

        // 设置对话框的内容面板
        dialog.setContentPane(panel);
        // 显示对话框
        dialog.setVisible(true);
    }
}
