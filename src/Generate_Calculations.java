/*
* 本类的功能是按照给定的条件，如运算数范围，是否带小数等，产生指定数量的随机算式
* 并将算式作为方法getCalculation的返回值返回
*
* */

import java.math.BigDecimal;
import java.util.Random;
public class Generate_Calculations {
    private final int  range;                 //运算数的范围，如10以下
    private final boolean isFractionAllowed;  //是否允许小数
    private final int OperandsNum;            //运算数个数
    private final int Num;
    private final boolean isParenthesesAllowed;//是否允许括号;
    private static final char[] Operator = {'+','-','*','/','(',')'};


    public Generate_Calculations(int range ,boolean isFractionAllowed, int OperandNum,  boolean
                                 isParenthesesAllowed,int Num){
        this.range = range;
        this.isFractionAllowed = isFractionAllowed;
        this.OperandsNum = OperandNum;
       // this.type = type;
        this.isParenthesesAllowed = isParenthesesAllowed;
        this.Num = Num;
    }

    //本方法作为返回String[]类型的算式数组，其他类只需调用它即可
    public String[] getCalculations(){
        if(Num == 0 )  return new String[]{""};             //若题目数量为0，则返回空
        String[] res = new String[Num];
        if(!isParenthesesAllowed)                           //如果需要产生的是不带括号的，则循环调用generateCalculationsWithoutParentheses即可
            for(int i=0;i<Num;i++)
                res[i] = generateCalculationsWithoutParentheses();
            //return generateCalculationsWithoutParentheses();
        else                                                //否则调用addParentheses，将generateCalculationsWithoutParentheses方法产生的算式套上括号
            for(int i=0;i<Num;i++)
                res[i] = addParentheses(generateCalculationsWithoutParentheses());
            return res;
    }


    //此方法用来随机产生一个给定条件的不带括号的算式
    private String generateCalculationsWithoutParentheses(){
        Random r = new Random();                        //产生随机数需要的Random类对象
        /*
        double[] random = new double[OperandsNum];       //随机生成若干个double数
        for(int i=0;i<random.length;i++){                //这些随机数的范围为[0,range);若题目不要求小数，我们对这些数取整，否则保留
            random[i] = 10 * range * r.nextDouble();     //1位小数
        }*/


        Object[] Operands  = new Object[OperandsNum];     //随机产生给定数目的运算数，如果题目要求是不带小数，则使用nextInt方法随机生成整数,否则使用nextDoulbe方法随机生成小数
        for(int i=0;i<OperandsNum;i++){
            if(!isFractionAllowed){
                Operands[i] = r.nextInt(range) + 1;
            }

            else {
                double tmp = r.nextDouble() * range;   //保留一位小数，使运算时难度更加合理
                Operands[i] = new BigDecimal(tmp).setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
            }

        }
        //下面实现在生成的运算数之间生成运算符，并返回最后的算式
        String calculation = "";
        for(int i=0;i<OperandsNum;i++)
        {
            calculation +=Operands[i].toString();
            if(i!=OperandsNum-1) {
                int idx = r.nextInt(4);
                calculation += Operator[idx];
            }
        }
        return calculation;


    }

    //该方法实现在给定的算式calculation上随机套上一个合法的括号，并返回套上括号的算式
    private String addParentheses(String calculation){

        //本方法的思路为:
        //先用IndexOfOperand数组记录下算式中若干个运算符的下标
        //那么左括号(可随机添加在 原算式的开头、每个运算符的右边；
        //右括号）可随机添加在 原算式的末尾、每个运算符的左边；
        Random r = new Random();
        int[] IndexOfOperand = new int[OperandsNum+1];
        IndexOfOperand[0] = -1;
        IndexOfOperand[OperandsNum] = calculation.length();
        int idx=1;
        for(int i=0;i<calculation.length();i++){
            if(calculation.charAt(i) == '+' || calculation.charAt(i) == '-' ||
                    calculation.charAt(i) == '*'||calculation.charAt(i) == '/'){
                IndexOfOperand[idx++] =  i;
            }
        }                                   //以上完成IndexOfOperand的建立.

        int leftP ,rightP ;
        do{
            leftP = r.nextInt(OperandsNum-1);
            rightP = leftP +2+ r.nextInt(OperandsNum+1-leftP-2);
        }while(leftP == 0 && rightP==OperandsNum);


        int IdxOfLp = IndexOfOperand[leftP], IdxOfRp = IndexOfOperand[rightP];      //IdxOfLp表示是左括号的下标，IdxOfRp表示右括号的下标
        String str_l,str_r;
        str_r = calculation.substring(IdxOfRp);                                     //添加右括号）：现将原算式拆分成两部分，然后将右括号）放在两部分之间，最后拼接。
        String res = calculation.substring(0,IdxOfRp) + ")"+str_r ;
        if(IdxOfLp==-1) str_l="";
        else
         str_l=res.substring(0,IdxOfLp+1);                                          //添加左括号，方法同上
        str_r = res.substring(IdxOfLp+1);
        res = str_l + "(" + str_r;


        return res;


    }



}
