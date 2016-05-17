package com.dreamleilei.compiling;

import java.util.*;

/**
 * Created by lei on 16-5-17.
 */


public class Compiling {
    private  Set<String> terminalSymbol ;               //终结符的集合
    private Set<String> nonTerminalSymbol;              //非终结符的集合
    private Map grammers[];                             //文法推导
    private Map[][] pridectionTable;                    //预测表
    private Map<String, Integer> position;              //获得字符在二维数组中下标的位置


    public Compiling() {
    }

    private void initTermianlSymbol(){   /*初始化终结符方法
         /*初始化终结符*/
        terminalSymbol = new HashSet<String>();
        terminalSymbol.add("i");
        terminalSymbol.add("+");
        terminalSymbol.add("*");
        terminalSymbol.add("(");
        terminalSymbol.add(")");
        terminalSymbol.add("#");
        terminalSymbol.add("null");
    }

    private void initNonTerminalSymbol() {/*初始化非终结符
           /*初始化非终结符*/
        nonTerminalSymbol = new HashSet<String>();
        nonTerminalSymbol.add("E");
        nonTerminalSymbol.add("E'");
        nonTerminalSymbol.add("T");
        nonTerminalSymbol.add("T'");
        nonTerminalSymbol.add("F");

    }
    private void initGrammmers(){
          /*c初始化文法*/
        grammers = new HashMap[8];
        for (int i = 0; i < grammers.length; i++) {
            grammers[i] = new HashMap<String, LinkedList<String>>();

        }
        LinkedList<String> list0 = new LinkedList<String>();
        list0.add("T");
        list0.add("E'");
        grammers[0].put("E", list0);

        LinkedList<String> list1 = new LinkedList<String>();
        list1.add("+");
        list1.add("T");
        list1.add("E'");
        grammers[1].put("E'", list1);

        LinkedList<String> list2 = new LinkedList<String>();
        list2.add("null");
        grammers[2].put("E'", list2);

        LinkedList<String> list3 = new LinkedList<String>();
        list3.add("F");
        list3.add("T'");
        grammers[3].put("T", list3);

        LinkedList<String> list4 = new LinkedList<String>();
        list4.add("*");
        list4.add("F");
        list4.add("T'");
        HashMap<String, LinkedList<String>> grammer4 = new HashMap<String, LinkedList<String>>();
        grammers[4].put("T'",list4);

        LinkedList<String> list5 = new LinkedList<String>();
        list5.add("null");
        grammers[5].put("T'", list5);

        LinkedList<String> list6 = new LinkedList<String>();
        list6.add("(");
        list6.add("E");
        list6.add(")");
        grammers[6].put("F", list6);

        LinkedList<String> list7 = new LinkedList<String>();
        list7.add("i");
        grammers[7].put("F", list7);

    }

    private void initPredictionTable() {
         /*初始化预测表*/
        pridectionTable = new HashMap[6][7];
        for (int i = 0; i < pridectionTable.length; i++) {
            for (int j = 0; j < pridectionTable[i].length; j++) {
                pridectionTable[i][j] = new HashMap<String, LinkedList<String>>();

            }

        }

        pridectionTable[0][0] = grammers[0];
        pridectionTable[0][3] = grammers[0];
        pridectionTable[1][1] = grammers[1];
        pridectionTable[1][4] = grammers[2];
        pridectionTable[1][5] = grammers[2];
        pridectionTable[2][0] = grammers[3];
        pridectionTable[2][3] = grammers[3];
        pridectionTable[3][1] = grammers[5];
        pridectionTable[3][2] = grammers[4];
        pridectionTable[3][4] = grammers[5];
        pridectionTable[3][5] = grammers[5];
        pridectionTable[4][0] = grammers[7];
        pridectionTable[4][3] = grammers[6];

    }

    public void initOther() {  /*初始化其它参数如为了方便编码，把预测表中的符号对应的数组下标值建立映射关系 */
        /*得到每个符号的所对应的下标*/
        position = new HashMap<String, Integer>();
        position.put("E", 0);
        position.put("E'", 1);
        position.put("T", 2);
        position.put("T'", 3);
        position.put("F", 4);
        position.put("i", 0);
        position.put("+", 1);
        position.put("*", 2);
        position.put("(", 3);
        position.put(")", 4);
        position.put("#", 5);
    }

    public void init(){


        initTermianlSymbol();
        initNonTerminalSymbol();
        initGrammmers();
        initPredictionTable();
        initOther();
    }

    public boolean compiling(String inputScentences) {
        LinkedList<String> stack = new LinkedList<String>();
        char[] chars = inputScentences.toCharArray();
        LinkedList<String> scentences = new LinkedList<String>();
        for (char aChar : chars) {
            scentences.add(String.valueOf(aChar));
        }
        stack.add("#");
        stack.add("E");
        while(!stack.isEmpty()  && !scentences.isEmpty()) {
            System.out.println("\n\nstack = " + stack);
            System.out.println("scentenses = " + scentences);
            String currentSymbol = scentences.getFirst();   //获取输入串的第一个字符
            String currentStackSymbol = stack.getLast();     //获取栈顶的第一个符号
            System.out.println("current Character = " + currentSymbol);
            if( nonTerminalSymbol.contains(currentStackSymbol) ) {  //如果栈顶的符号是非终结符
                Map<String, LinkedList<String>> temp =
                        pridectionTable[position.get(currentStackSymbol)][position.get(currentSymbol)]; //获取预测的文法
                System.out.println("production " + temp);
                if(temp.equals(new HashMap<String, LinkedList<String>>())){
                    return false;
                }
                String removeStackSymbol = stack.removeLast();  //出栈
                LinkedList<String> newSymbols = (LinkedList<String>) temp.get(removeStackSymbol).clone();
                while(!newSymbols.isEmpty()){   //当栈不是空的时候
                    stack.add(newSymbols.removeLast());  //产生式右部倒序入栈
                }
            } else if(terminalSymbol.contains(currentStackSymbol)) {         //如果栈顶的符号是终结符
                if(currentSymbol.equals(currentStackSymbol)) {               //如果输入串的第一个符号和栈顶符号相同，则栈顶出栈，符号串右移
                    stack.removeLast();
                    scentences.removeFirst();
                } else if(currentStackSymbol.equals("null")) {               //如果产生式右部是空产生式，栈顶出栈
                    stack.removeLast();
                } else{
                    return false;
                }
            } else {
                return false;
            }
            System.out.println("Stack .." + stack);
        }
        if(!stack.isEmpty()){
            return false;
        }
        return true;
    }

    public static void main(String[] args) {

        Compiling complier = new Compiling();
        complier.init();
        System.out.println("请输入您要分析的串: ");
        Scanner scanner = new Scanner(System.in);
        String scentense = scanner.next();
        scentense += "#";
        boolean result = complier.compiling(scentense);

        if(result) {
            System.out.println("\n\n您输入的串是合法的串");
        } else {
            System.out.println("\n\n您输入的串不合法");
        }

    }
}
