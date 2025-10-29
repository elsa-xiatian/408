import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class Expression {
    public static List<String> infixToPostfix(String infixExpression) {
        //1.初始化栈及集合
        Stack<Character> operatorStack = new Stack<>();
        List<String> postfix = new ArrayList<>();

        //开始遍历原中缀表达式
        for (int i = 0; i < infixExpression.length(); i++) {
            char c = infixExpression.charAt(i);

            //情况1： 遇到空格，跳过
            if(c == ' '){
                continue;
            }

            //2.情况2： 遇到操作数
            else if(Character.isDigit(c)){
                StringBuilder num = new StringBuilder(); //用来拼接完整数字

                while(i < infixExpression.length() && Character.isDigit(infixExpression.charAt(i))){
                    num.append(infixExpression.charAt(i++));
                }
                i--;
                postfix.add(String.valueOf(num));
            }
            //3.情况3 ： 遇到左括号
            else if(c == '('){
                operatorStack.push(c);
            }
            //4.情况4 ： 遇到右括号
            else if(c == ')'){
                while(operatorStack.peek() != '('){
                    postfix.add(String.valueOf(operatorStack.pop()));
                }
                operatorStack.pop();
            }
            //5.遇到运算符
            else{
                while(!operatorStack.isEmpty() && operatorStack.peek() != '(' &&
                getPriority(c) <= getPriority(operatorStack.peek())){
                    postfix.add(String.valueOf(operatorStack.pop()));
                }
                operatorStack.push(c);
            }
        }
        while(!operatorStack.isEmpty()){
            postfix.add(String.valueOf(operatorStack.pop()));
        }
        return postfix;

    }

    // 计算运算符优先级（数字越大优先级越高）
    private static int getPriority(char operator) {
        switch (operator) {
            case '+':
            case '-':
                return 1;
            case '*':
            case '/':
                return 2;
            default:
                throw new IllegalArgumentException("不支持的运算符：" + operator);
        }
    }

    // 后缀表达式求值
    public static int evaluatePostfix(List<String> postfix) {
        //准备一个存放数字的栈
        Stack<Integer> numStack = new Stack<>();
        //开始遍历原后缀表达式
        for (String token : postfix) {
            if(token.matches("\\d+")){
                numStack.push(Integer.parseInt(token));
            } //正则表达式判断是否为数字
            else{
                int b = numStack.pop();
                int a = numStack.pop();
                int res = 0;

                switch (token){
                    case "+":
                        res = a+b;
                        break;
                    case "-":
                        res = a-b;
                        break;
                    case "*":
                        res = a*b;
                        break;
                    case "/":
                        if(b == 0){
                            throw new RuntimeException("除数不能为0");
                        }
                        res = a/b;
                        break;
                }
                numStack.push(res);
            }
        }
        return numStack.pop();
    }

    public static void main(String[] args) {
        // 测试用例1：基础运算（包含优先级）
        String test1 = "3 + 4 * 2 / ( 1 - 5 )";
        System.out.println("测试用例1：" + test1);
        List<String> postfix1 = infixToPostfix(test1);
        System.out.println("后缀表达式：" + postfix1); // 预期：[3, 4, 2, *, 1, 5, -, /, +]
        System.out.println("计算结果：" + evaluatePostfix(postfix1) + "（预期：1）\n");

        // 测试用例2：带括号的运算
        String test2 = "( 10 + 20 ) * 3 - 15";
        System.out.println("测试用例2：" + test2);
        List<String> postfix2 = infixToPostfix(test2);
        System.out.println("后缀表达式：" + postfix2); // 预期：[10, 20, +, 3, *, 15, -]
        System.out.println("计算结果：" + evaluatePostfix(postfix2) + "（预期：75）\n");

        // 测试用例3：多位数运算
        String test3 = "100 + 200 * 300";
        System.out.println("测试用例3：" + test3);
        List<String> postfix3 = infixToPostfix(test3);
        System.out.println("后缀表达式：" + postfix3); // 预期：[100, 200, 300, *, +]
        System.out.println("计算结果：" + evaluatePostfix(postfix3) + "（预期：60100）\n");

        // 测试用例4：除法运算（整数除法）
        String test4 = "10 - 6 / 2";
        System.out.println("测试用例4：" + test4);
        List<String> postfix4 = infixToPostfix(test4);
        System.out.println("后缀表达式：" + postfix4); // 预期：[10, 6, 2, /, -]
        System.out.println("计算结果：" + evaluatePostfix(postfix4) + "（预期：7）");
    }


}
