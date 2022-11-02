import java.util.Stack;

public class Compute {
    static int  curpos;     //下标，表示在后缀表达式中从哪个位置取出item，每次取出一个item，curpos应当更新
    static String item;         //每次从后缀表达式中取出数字或运算符保存在item中
    static String postfix;       //后缀表达式
    static Stack<Double> stack =new Stack<>();     //栈，中缀计算时用
    public static int getItem(String fix) {
        item = "";
        int k = curpos, flag;          //flag 标记取出的是操作符还是数字 ，数字标记成0，否则1
        if (fix.charAt(k) == '.')
            flag = -1;
        else if (fix.charAt(k) >= '0' && fix.charAt(k) <= '9') {
            while ((fix.charAt(k) >= '0' && fix.charAt(k) <= '9') || fix.charAt(k) == '.') {
                item += fix.charAt(k++);
                if (k >= fix.length())
                    break;
            }
            flag = 0;
        } else {                  //操作符
            item += fix.charAt(k++);
            flag = 1;
        }
        while (k < fix.length() && fix.charAt(k) == ' ')      //取出空格
            k++;

        curpos = k;        //更新下标
        return flag;
    }

    static int ICP(char c){             //栈外优先级
        if(c == '#')    return 0;
        if(c == '(')    return 16;
        if(c == '*' || c== '/') return 12;
        if(c == '+' || c=='-')  return 10;
        if(c == ')')  return 1;
        else return -1;
    }
    static int ISP(char c){
        if(c == '#')    return 0;
        if(c == '(')    return 1;
        if(c == '*' || c== '/') return 13;
        if(c == '+' || c=='-')  return 11;
        if(c == ')')  return 16;
        else return -1;

    }


    //中缀转后缀，getItem取元素
    static void InfixToPostfix(String infix) {
        curpos = 0;
        postfix = "";
        Stack<Character> S = new Stack<>();
        int flag = -1;
        int k = 0, i;
        char ch,curop = 0;
        S.push('#');
        while(curpos < infix.length()) {
            flag = getItem(infix);

            if (flag == 1)
            {
                curop = item.charAt(0);
                if(curop == ')')
                {
                    do {
                        ch = S.pop();
                        if(ch == '#')
                        {}
                        if(ch!= '(')
                        {
                            postfix+= ch;
                            postfix +=" ";
                        }
                    }while(ch!='(');
                }
                else{
                    ch = S.peek();
                    while(ICP(curop) <= ISP(ch))
                    {
                        S.pop();
                        postfix+=ch;
                        postfix +=" ";
                        ch = S.peek();
                    }
                    S.push(curop);
                }
            }
            else
            {
                postfix+=item;
                postfix+=' ';
            }
        }
        while(!S.empty())
        {
            ch = S.pop();
            if(ch!='#')
            {
                postfix += ch;
                postfix += " ";
            }
        }

    }

    //运算，若后缀表达式取出的是操作符， 则调用这个函数，即先从栈中取出连个
    static void DoOperator(char oper) {
        double oper1 , oper2;

        oper1 = stack.pop();
        int int_oper1 = (int)oper1;

        oper2 = stack.pop();
        int int_oper2 = (int)oper2;
        if(oper == '+') {
            stack.push(oper1 + oper2);
            return;
        }
        else if(oper == '-'){
            stack.push(oper2 - oper1);
            return;
        }
        else if(oper == '*') {
            stack.push(oper1 * oper2);
            return;
        }
        else if(oper =='/') {
                stack.push(oper2 / oper1);
                return;
        }


    }
    //后缀表达式计算，
    static double Caculating(String post)
    {
        while(stack.size()!=0)
            stack.pop();
        double data;
        int flag;
        curpos = 0;
        while(curpos<post.length()&&post.charAt(curpos) == ' ')
            curpos++;
        while(curpos < post.length())
        {

            flag = getItem(post);

           if(flag == 1)
                DoOperator(item.charAt(0));
            else
            {
                data = Double.parseDouble(item);
                stack.push(data);
            }
        }
            //栈中只剩一个数时，即为答案，弹出返回
        return stack.pop();

    }


    public static String compute(String s){


        InfixToPostfix(s);
        double b = Caculating(postfix);      //b 为十进制答案
        return "" + b;

    }
    public static boolean isEqual(String a, String b){                          //判断两个String类型的数是否相等
        if(MainPanel.isNumeric(a) && MainPanel.isNumeric(b)) {
            double da = Double.parseDouble(a), db = Double.parseDouble(b);
            if (Math.abs(da - db) < 0.001)
                return true;
            return false;
        }
        else{
            if(a.equals(b))
                return true;
            return false;
        }
    }
    public static boolean isIntegerForDouble(double obj) {       //判断是否为整数
        double eps = 1e-10;  // 精度范围
        return obj-Math.floor(obj) < eps;
    }

    public static String DoubleToInteger(String s){         //判断s的小数部分是不是0， 如果是0，则将s的小数去掉，变成整数
        double tmp = Double.parseDouble(s);
        if(isIntegerForDouble(tmp))
            return Integer.toString((int)tmp);
        else
            return s;

    }
    public static String ToOneDecimal(String s){        //为了避免结果是无限小数，将结果保留两位小数
        double tmp = Double.parseDouble(s);
        if(s.length()>10)
            return String.format("%.2f",tmp);
        else return s;
    }
}
