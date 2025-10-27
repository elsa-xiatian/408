public class KMP {

    public static void main(String[] args) {
        String main = "ABABABCABAAABABABABABCABAA";
        String pat = "ABABCABAA";
        int index = KMPSearch(main, pat);
        System.out.println(index);
    }

    private static int KMPSearch(String main, String pat) {
        char[] M = main.toCharArray();
        char[] P = pat.toCharArray(); //转成char数组方便用数组方式表示
        int[] next = getnext(pat);

        int i = 0; //指向主串指针
        int j = 0; //指向模式串指针
        int Mlen = main.length();
        int Plen = pat.length();

        while(i < Mlen && j < Plen ){
            if(j == -1 || M[i] == P[j]){
                //如果匹配则继续下一位字符的匹配，j==-1说明已经无前缀可以回退因此往右移继续匹配
                i++;
                j++;
            }
            else{
                j = next[j];
            }
        }
        return j == Plen ? i-j : -1;
    }

    //1.计算next数组
    public static int[] getnext(String pat){
        int Plen = pat.length();
        int[] next = new int[Plen];
        next[0] = -1;
        int j = 0; //用来指向当前字符串的位置
        int k = -1; //
        char[] p = pat.toCharArray();

        while(j < Plen - 1){
            if(k == -1 || p[k] == p[j]){
                k++;
                j++;
                if(p[j] != p[k]){
                    next[j] = k;
                }else{
                    next[j] = next[k];
                }
            }else{
                k = next[k];
            }
        }
        return next;
    }
}
