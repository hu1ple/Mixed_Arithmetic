import javax.swing.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.Random;

public class Choice_Questions extends FillBlank_Questions implements ItemListener {
    private String[] str_options;
    private JRadioButton[] radioBt_options;
    public Choice_Questions(String question) {
        super(question);
        generate_Options();
        radioBt_options = new JRadioButton[]{
                new JRadioButton("A."+str_options[0]),
                new JRadioButton("B."+str_options[1]),
                new JRadioButton("C."+str_options[2]),
                new JRadioButton("D." + str_options[3])
        };
        ButtonGroup buttonGroup = new ButtonGroup();
        for(int i=0;i<radioBt_options.length;i++) {
            buttonGroup.add(radioBt_options[i]);
            this.hBox.add(radioBt_options[i]);
            radioBt_options[i].addItemListener(this);
        }

    }
    public void setText(){
        tf_question.setText("选择题: "+question+" =");
    }

    private void generate_Options(){
        Random r = new Random();
        str_options = new String[4];
        String Sres = Compute.ToOneDecimal(Compute.compute(question));
        Double Dres = Double.parseDouble(Sres);
        str_options[0] = Compute.DoubleToInteger(String.valueOf(Dres-1));
        str_options[1] = Compute.DoubleToInteger(String.valueOf(Dres+1));
        str_options[2] = Compute.DoubleToInteger(String.valueOf(Dres+5));
        str_options[3] = Compute.DoubleToInteger(String.valueOf(Dres+10));
        int a = r.nextInt(4);
        str_options[a] = Compute.DoubleToInteger(Sres);
        answer = String.valueOf((char) (a + 'A'));
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        if(e.getSource()==radioBt_options[0])
            tf_response.setText("A");
        else if(e.getSource() ==radioBt_options[1])
            tf_response.setText("B");
        else if(e.getSource() ==radioBt_options[2])
            tf_response.setText("C");
        else if(e.getSource() ==radioBt_options[3])
            tf_response.setText("D");
    }
    public void OutputToFile(String filename) throws MyException{
        try{
            FileWriter fileWriter = new FileWriter(filename,true);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.append("选择题:"+question+"\n");
            bufferedWriter.append("A "+str_options[0]+"   B  "+str_options[1]+"   C  "+str_options[2]+"   D  "+str_options[3]+"\n");
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
