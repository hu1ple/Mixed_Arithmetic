/*
* 此类为填空题类，同时也是判断题和选择题的父类，负责封装每道题目的题干、正确答案、作答、显示题目的容器、控件等
* 还实现了将每道题目的信息输出到文件的方法
* */
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.io.BufferedWriter;
import java.io.FileWriter;

public class FillBlank_Questions  implements DocumentListener {
    protected String question;          //算式 如“1+1”
    protected String  answer;           //正确答案，如“2”，通过将question传递到Compute中的方法Final_compute得到
    protected String response;          //用户的作答
    protected boolean isCorrect;        //记录用户作答是否正确，通过将answer和response比较得到
    protected Box hBox;                 //一个水平箱容器，里面放置了多个文本框，将此容器添加到AnswerPanel中即可实现一道题目的显示
    protected JTextField tf_question;   //两个文本框，分别用来显示题干和用户作答区域
    protected JTextField tf_response;

    public FillBlank_Questions(String question){            //构造方法，只需从外部传进一个算式，即可计算出算式的所有信息，两个子类的构造方法也会调用这个方法
        this.question = question;
        this.answer = Compute.DoubleToInteger(Compute.compute(question));
        this.answer = Compute.ToOneDecimal(this.answer);
        tf_question = new JTextField();
        setText();
        tf_question.setEditable(false);
        tf_question.setColumns(50);
        //tf_question.setSize(300,30);

        tf_response = new JTextField();
        tf_response.getDocument().addDocumentListener(this);   // 为作答框添加文本改变监听，文本改变后会立刻将resposne更新
        //tf_response.setSize(100,30);
        tf_response.setColumns(5);
        hBox = Box.createHorizontalBox();           //将两个文本框添加到容器中
        hBox.add(tf_question);
        hBox.add(tf_response);
        hBox.add(Box.createHorizontalGlue());
    }
    /*
    public void setResponse(String response){
        this.response = response;
        isCorrect = Math.abs(Double.parseDouble(answer) - Double.parseDouble(response)) < 0.0001;
    }*/
    public void setText(){              //设置题目文本框的内容，介于三种题型的格式不是统一的，所以我利用多态性，在子类中重写该方法。
        tf_question.setText("填空题："+question +"=");
    }

    public Box gethBox(){           //返回该题目的水平箱容器
        return this.hBox;
    }
    public void ShowResult(){       //根据用户作答response，生成结果反馈的部件，并添加到hBox中
        JLabel label_YesOrNo  = new JLabel();//label显示结果正确与否
        if(isCorrect) {
            label_YesOrNo.setText("正确");
            label_YesOrNo.setForeground(Color.GREEN);
        }
        else {
            label_YesOrNo.setText("错误");
            label_YesOrNo.setForeground(Color.RED);
        }
        JLabel label_CorrectAns = new JLabel(answer);   //label显示正确答案，如果作答正确，则该label不需要显示
        hBox.add(label_YesOrNo);
        hBox.add(label_CorrectAns);
        if(isCorrect)
            label_CorrectAns.setVisible(false);
    }
    //将每个题目输出到文件中，介于每个题型的输出格式不同，这个方法也会在子类中重写,该方法将会在用户点击“历史成绩”按钮后调用
    public void OutputToFile(String filename) throws MyException{
        try{
            FileWriter fileWriter = new FileWriter(filename,true);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write("填空题:"+question+"\n");
            bufferedWriter.write("你的答案:"+response+"   "+"正确答案:"+answer+"\n");
            if(isCorrect)
                bufferedWriter.write("回答正确\n");

            else
                bufferedWriter.write("回答错误\n");
            bufferedWriter.append("\n\n");
            bufferedWriter.close();
            fileWriter.close();
        } catch (Exception e) {
            throw new MyException("文件输出异常！");           //如果文件流打开异常，则抛出自定义异常，这个异常将会在点击
            //e.printStackTrace();
        }
    }

    @Override
    public void insertUpdate(DocumentEvent e) {    //重写监听器，捕捉作答文本框的改变，同步更新response和isCorrect
        response = tf_response.getText();
        if(Compute.isEqual(response,answer))
            isCorrect = true;
        /*if(text.length() == 1){
            char c =text.charAt(0);
            if(c != 'A' || c!='B'||c !='C' || c!='D' || c!='T' || c!='F'){
                thr
            }
        }*/
    }

    @Override
    public void removeUpdate(DocumentEvent e) {

    }

    @Override
    public void changedUpdate(DocumentEvent e) {

    }
}
