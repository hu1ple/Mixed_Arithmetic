import javax.swing.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.Random;

public class TorF_Questions extends FillBlank_Questions implements ItemListener {
    private String judgingObject;
    private JRadioButton[] radioBt_TorF;

    public TorF_Questions(String question) {

        super(question);
        generateJudgingObject();        //生成判断的对象，使用随机函数，在正确答案和错误答案中选一个
        setText();

        radioBt_TorF = new JRadioButton[]{
                new JRadioButton("T"),
                new JRadioButton("F")
        };
        tf_question.setColumns(35);
        tf_response.setColumns(5);
        ButtonGroup buttonGroup = new ButtonGroup();
        for(int i=0;i<radioBt_TorF.length;i++) {
            buttonGroup.add(radioBt_TorF[i]);
            this.hBox.add(radioBt_TorF[i]);
            radioBt_TorF[i].addItemListener(this);
        }

    }
    public void setText(){
        tf_question.setText("判断题：" + question + "=" + judgingObject);
    }
    private void generateJudgingObject(){
        String Sres = Compute.final_compute(question);
        double Dres = Double.parseDouble(Sres);
        String fake_answer = Compute.DoubleToInteger(String.valueOf(Dres+10)); //产生一个比正确答案大10 的结果；
        Random r = new Random();
        int rand = r.nextInt(2);
        if(rand == 0)
        {
            judgingObject = Compute.DoubleToInteger(Sres);
            answer = "T";
        }
        else {
            judgingObject = fake_answer;
            answer = "F";
        }
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        if(e.getSource() == radioBt_TorF[0]){
           // this.response = "T";
            this.tf_response.setText("T");
        }
        else if(e.getSource() == radioBt_TorF[1]){
            //this.response ="F";
            this.tf_response.setText("F");
        }
    }
    public void OutputToFile(String filename) throws MyException{
        try{
            FileWriter fileWriter = new FileWriter(filename,true);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.append("判断题:"+question+"="+judgingObject+"\n");
            bufferedWriter.append("你的答案:"+response+"   "+"正确答案:"+answer+"\n");
            if(isCorrect)
                bufferedWriter.append("回答正确\n");
            else
                bufferedWriter.append("回答错误\n");
            bufferedWriter.append("\n\n");
            bufferedWriter.close();
            fileWriter.close();
        } catch (Exception e) {
            throw new MyException("文件输出异常！");
            //e.printStackTrace();
        }
    }
}
