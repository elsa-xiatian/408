import lombok.Data;

import java.util.*;

public class Tree {
    @Data
    static
    class TreeNode{
        //节点类
        int val;
        TreeNode left;
        TreeNode right;
        public TreeNode(int val){
            this.val = val;
            this.left = null;
            this.right = null;
        }
    }

    static class BinaryTree{
        //二叉树工具类

        //先序遍历
        public void preOrder(TreeNode root, List<Integer> res){
            if(root == null){return;}
            res.add(root.val); //处理当前节点
            preOrder(root.left,res);
            preOrder(root.right,res);
        }

        //中序遍历
        public void inOrder(TreeNode root,List<Integer> res){
            if(root == null){return;}
            inOrder(root.left,res);
            res.add(root.val);
            inOrder(root.right,res);
        }

        //后序遍历
        public void postOrder(TreeNode root,List<Integer> res){
            if(root == null){return;}
            postOrder(root.left,res);
            postOrder(root.right,res);
            res.add(root.val);
        }

        //层序遍历
        public List<Integer> levelOrder(TreeNode root){
            List<Integer> res = new ArrayList<>();
            Queue<TreeNode> queue = new LinkedList<>();
            //1.根节点入队
            queue.offer(root);

            while(!queue.isEmpty()){
                //1.当前节点弹出队列，并加入结果
                TreeNode node = queue.poll();
                res.add(node.val);

                //处理当前节点的字数
                if(node.left != null){
                    queue.offer(node.left);
                }

                if(node.right != null){
                    queue.offer(node.right);
                }
            }
            return res;
        }

        public TreeNode buildTree(int[] preorder,int[] inorder){
            //边界条件判断
            if(inorder == null || preorder == null || inorder.length != preorder.length){
                return null;
            }
            HashMap<Integer,Integer> map = new HashMap<>();
            for (int i = 0; i < inorder.length; i++) {
                map.put(inorder[i],i); //使用map快速存储元素在中序遍历中的位置
            }
            //递归建树
            return build(preorder,0,preorder.length-1,inorder,0,inorder.length-1,
                    map);
        }

        private TreeNode build(int[] preorder, int preStart, int preEnd,
                               int[] inorder, int inStart, int inEnd, HashMap<Integer, Integer> map)
        {
            //边界条件处理
            if(preStart > preEnd || inStart > inEnd){
                return null;
            }

            int rootval = preorder[preStart]; //先序遍历中第一位是根节点的值
            TreeNode root = new TreeNode(rootval);

            //找到根节点在中序遍历中的位置，方便划分左右子树
            int index = map.get(rootval);
            int leftsize = index - inStart;//左子树规模

            root.left = build(preorder,preStart+1,preStart + leftsize,
                    inorder,inStart,index-1,map);

            root.right = build(preorder,preStart + leftsize + 1,preEnd,
                    inorder,index+1,inEnd,map);

            return root;
        }

    }

    class ThreadTreeNode{
        int val;
        ThreadTreeNode left;
        ThreadTreeNode right;
        ThreadTreeNode parent; // 新增：父节点指针（用于回退）
        boolean ltag; //(true表示指向前驱，false表示指向左孩子)
        boolean rtag;

        public ThreadTreeNode(int val){
            this.val = val;
            this.left = null;
            this.right = null;
            this.parent = null;
            ltag = false;
            rtag = false;
        }
    }

    class ThreadBinaryTree{
        private ThreadTreeNode pre; //记录前驱节点

        public void resetPre() {
            this.pre = null;
        }

        //中序线索化
        public void inOrderThread(ThreadTreeNode root){
            if(root == null) return;
            inOrderThread(root.left); //处理左子树

            //处理当前节点
            if(root.left == null){
                root.left = pre;
                root.ltag = true;
            }

            if(pre != null && pre.right == null){
                pre.right = root;
                pre.rtag = true;
            }
            pre = root;
            //处理右子树
            inOrderThread(root.right);
        }

        public List<Integer> traverseInorderThreadTree(ThreadTreeNode root){
            List<Integer> res = new ArrayList<>();
            if(root == null){return res;}

            //1.先处理左子树
            ThreadTreeNode curr = root;
            while(curr != null && !curr.ltag){
                curr = curr.left;  //左孩子不为线索就为节点，一直往左走
            }

            while(curr != null){
                //处理当前节点
                res.add(curr.val);

                if(curr.rtag){
                    curr = curr.right; //是右线索则直接往下走
                }else{
                    curr = curr.right;
                    while(curr != null && !curr.ltag){
                        curr = curr.left; //在右子树中找到最左下的节点
                    }
                }

            }
            return res;
        }

        public void PreorderThread(ThreadTreeNode root){
            if (root == null) return;

            // 1. 处理当前节点的【前驱线索】（左指针为空时，指向前驱pre）
            if (root.left == null) {
                root.left = pre;    // 左指针指向前驱
                root.ltag = true;   // 标记为前驱线索
            }

            // 2. 处理【前驱节点的后继线索】（
            if (pre != null && pre.right == null) {
                pre.right = root;   // pre的右指针指向当前节点（pre的后继）
                pre.rtag = true;    // 标记为后继线索
            }

            // 3. 更新前驱节点：当前节点成为下一个节点的前驱
            pre = root;

            // 4. 递归线索化左子树（只有左指针是孩子节点，才递归，避免死循环）
            if (!root.ltag) {
                PreorderThread(root.left);
            }

            // 5. 递归线索化右子树（只有右指针是孩子节点，才递归，避免死循环）
            if (!root.rtag) {
                PreorderThread(root.right);
            }
        }

        public List<Integer> traversePreorderThreadtree(ThreadTreeNode root){
            List<Integer> res = new ArrayList<>();
            if(root == null){return res;}

            ThreadTreeNode curr = root;

            while(curr != null){
                res.add(curr.val);
                if(!curr.ltag){
                    curr = curr.left;
                }else{
                    curr = curr.right;
                }
            }

            return res;
        }


        public void postorderThread(ThreadTreeNode root){
            if (root == null) return;

            // 1. 线索化左子树（递归时传递父节点）
            if(!root.ltag && root.left != null){
                root.left.parent = root;
                postorderThread(root.left);
            }

            // 2. 线索化右子树（递归时传递父节点）
            if (!root.rtag && root.right != null) {
                root.right.parent = root; // 右孩子的父节点是当前节点
                postorderThread(root.right);
            }

            // 3. 处理当前节点的线索（原逻辑不变）
            if (root.left == null) {
                root.left = pre;
                root.ltag = true;
            }
            if (pre != null && pre.right == null) {
                pre.right = root;
                pre.rtag = true;
            }

            // 4. 更新前驱节点
            pre = root;
        }

        public List<Integer> traversePostorderThreadTree(ThreadTreeNode root){
            List<Integer> res = new ArrayList<>();
            if(root == null) return res;

            ThreadTreeNode curr = root;
            ThreadTreeNode preNode = null;

            //1.找到需要遍历的第一个节点
            while(curr != null && !curr.ltag){
                curr = curr.left;
            }

            //开始循环处理节点
            while(curr != null){
                //1.当前遍历的节点的右子树为线索
                if(curr.rtag){
                    res.add(curr.val);
                    preNode = curr;
                    curr = curr.right;
                }else{
                    //2.当前遍历的节点的右子树存在且已经访问完
                    if(preNode == curr.right){
                        res.add(curr.val);
                        if(curr == root){
                            break;
                        }
                        preNode = curr;
                        curr = curr.parent;
                    }
                    //3.当前遍历的节点的右子树还没有访问
                    else{
                        curr = curr.right;
                        while(curr != null && !curr.ltag){
                            curr = curr.left;
                        }
                    }

                }
            }
            return res;
        }

            public ThreadTreeNode convertToThreadNode(TreeNode node) {
                if (node == null) {return null;}
                ThreadTreeNode threadNode = new ThreadTreeNode(node.val);
                threadNode.left = convertToThreadNode(node.left);
                threadNode.right = convertToThreadNode(node.right);
                // 初始ltag/rtag都是false（默认指向孩子）
                return threadNode;
            }

    }

    static class HuffmanNode implements Comparable<HuffmanNode> {
        int weight; // 节点权重（关键：用于排序选择最小节点）
        HuffmanNode left;  // 左子树
        HuffmanNode right; // 右子树

        // 构造器：初始化单节点树（无左右子树）
        public HuffmanNode(int weight) {
            this.weight = weight;
            this.left = null;
            this.right = null;
        }

        // 实现Comparable接口：按权重升序排序（方便选择最小节点）
        @Override
        public int compareTo(HuffmanNode o) {
            return this.weight - o.weight;
        }
    }

    public class HuffmanTree{
        public static HuffmanNode buildHuffmanTree(int[] weights){
            //1.将给出的权重值转为单节点
            List<HuffmanNode> list = new ArrayList<>();
            for (int weight : weights) {
                if(weight < 0){
                    throw new RuntimeException("权重为负");
                }
                list.add(new HuffmanNode(weight));
            }
            //2.处理边界条件
            if(list.isEmpty()){
                return null;
            }
            if(list.size() == 1){
                return list.get(0);
            }

            //3.循环处理节点
           while(list.size() > 1){
               Collections.sort(list);
               HuffmanNode left = list.remove(0);
               HuffmanNode right = list.remove(0);

               HuffmanNode parent = new HuffmanNode(left.weight + right.weight);

               parent.left = left;
               parent.right = right;

               list.add(parent);
           }


            //4.剩下的节点即为根节点
            return list.get(0);
        }

    }

    public class DSU{
        private int[] pre;//父节点数组
        private int[] rnk; //rnk[i]表示i所在树的高度，用于后续按秩合并

        public DSU(int size) {
            pre = new int[size];
            rnk = new int[size];

            for (int i = 0; i < size; i++) {
                pre[i] = i; //初始时每个节点指向自身
                rnk[i] = 1; //初始时每个节点高度为1
            }
        }

        public int root(int x){
            //找节点x的根的操作(递归)
            return x == pre[x] ? x : root(pre[x]);
        }

        public void merge(int x,int y){
            if(root(x) == root(y)) return;

            //实现按秩合并，总让秩小的指向秩大的
            if(rnk[x] > rnk[y]){
                swap(x,y);
            }

            pre[x] = y;

            if(rnk[x] == rnk[y]) rnk[y]++;

        }

        public boolean isConnected(int x,int y){
            return root(x) == root(y);
        }

        private void swap(int x, int y) {
         int temp = y;
         y = x;
         x = temp;
        }
    }



    public static void main(String[] args) {
        // 1. 构建普通二叉树（结构：1→2→4，1→2→5，1→3→6）
        TreeNode root = new TreeNode(1);
        root.left = new TreeNode(2);
        root.right = new TreeNode(3);
        root.left.left = new TreeNode(4);
        root.left.right = new TreeNode(5);
        root.right.right = new TreeNode(6);

        // 2. 测试普通二叉树遍历
        BinaryTree binaryTree = new BinaryTree();
        List<Integer> preRes = new ArrayList<>();
        binaryTree.preOrder(root, preRes);
        System.out.println("普通先序遍历：" + preRes); // 预期：[1,2,4,5,3,6]

        // 3. 测试线索二叉树
        Tree tree = new Tree();
        ThreadBinaryTree threadedTree = tree.new ThreadBinaryTree();
        ThreadTreeNode threadRoot = threadedTree.convertToThreadNode(root);

         //3.2 测试先序线索化及遍历（预期：[1,2,4,5,3,6]）
        threadedTree.resetPre();
        threadRoot = threadedTree.convertToThreadNode(root);
        threadedTree.PreorderThread(threadRoot);
        List<Integer> preThreadRes = threadedTree.traversePreorderThreadtree(threadRoot);
        System.out.println("先序线索遍历：" + preThreadRes);

         //3.1 测试中序线索化及遍历（预期：[4,2,5,1,3,6]）
        threadedTree.resetPre();
        threadRoot = threadedTree.convertToThreadNode(root);
        threadedTree.inOrderThread(threadRoot);
        List<Integer> inThreadRes = threadedTree.traverseInorderThreadTree(threadRoot);
        System.out.println("中序线索遍历：" + inThreadRes);


        // 3.3 测试后序线索化及遍历（预期：[4,5,2,6,3,1]）
        threadedTree.resetPre();
        threadRoot = threadedTree.convertToThreadNode(root);
        threadedTree.postorderThread(threadRoot);
        List<Integer> postThreadRes = threadedTree.traversePostorderThreadTree(threadRoot);
        System.out.println("后序线索遍历：" + postThreadRes);

        // 4. 测试先序+中序构造二叉树
        int[] preorder = {1,2,4,5,3,6};
        int[] inorder = {4,2,5,1,3,6};
        TreeNode builtRoot = binaryTree.buildTree(preorder, inorder);
        List<Integer> builtInRes = new ArrayList<>();
        binaryTree.inOrder(builtRoot, builtInRes);
        System.out.println("构造树的中序遍历：" + builtInRes); // 预期：[4,2,5,1,3,6]



    }





}


