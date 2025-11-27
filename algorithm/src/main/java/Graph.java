import lombok.Data;

import java.util.*;

public class Graph {

}
@Data
class Edge implements Comparable<Edge> {
    int from;   // 起点顶点索引
    int to;     // 终点顶点索引
    int weight; // 边的权重

    public Edge(int from, int to, int weight) {
        this.from = from;
        this.to = to;
        this.weight = weight;
    }

    // Kruskal算法需按权重排序，实现Comparable接口
    @Override
    public int compareTo(Edge o) {
        return this.weight - o.weight;
    }

    @Override
    public String toString() {
        return "(" + from + "→" + to + ", 权重:" + weight + ")";
    }

}

enum GraphType {
    DIRECTED,   // 有向图
    UNDIRECTED  // 无向图
}

class AdjacencyMatrixGraph {
    private Object[] vertices;       // 存储顶点数据
    private int[][] adjMatrix;       // 邻接矩阵：adjMatrix[i][j]表示i到j的权重
    private int vertexCount;         // 顶点数
    private int edgeCount;           // 边数
    private GraphType graphType;     // 图类型（有向/无向）
    public static final int INF = Integer.MAX_VALUE; // 表示无直接边（避免与权重0冲突）

    /**
     * 构造器：初始化图
     * @param vertexCount 顶点数量
     * @param graphType 图类型
     */
    public AdjacencyMatrixGraph(int vertexCount, GraphType graphType) {

        this.vertexCount = vertexCount;
        this.graphType = graphType;
        this.vertices = new Object[vertexCount];
        // 初始化邻接矩阵：默认无直接边（设为INF），顶点自身到自身权重为0
        this.adjMatrix = new int[vertexCount][vertexCount];
        for (int i = 0; i < vertexCount; i++) {
            Arrays.fill(adjMatrix[i], INF);
            adjMatrix[i][i] = 0; // 自身环权重为0（可根据需求调整）
        }
    }

    /**
     * 添加顶点
     * @param index 顶点索引（0~vertexCount-1）
     * @param data 顶点数据
     */
    public void addVertex(int index, Object data) {
        if (index < 0 || index >= vertexCount) {
            throw new IllegalArgumentException("顶点索引超出范围");
        }
        vertices[index] = data;
    }

    /**
     * 添加边
     * @param from 起点索引
     * @param to 终点索引
     * @param weight 边权重（无权重图传1）
     */
    public void addEdge(int from, int to, int weight) {
        // 校验顶点索引合法性
        if (from < 0 || from >= vertexCount || to < 0 || to >= vertexCount) {
            throw new IllegalArgumentException("顶点索引超出范围");
        }
        // 有向图：仅添加from→to
        adjMatrix[from][to] = weight;
        // 无向图：同时添加to→from（权重相同）
        if (graphType == GraphType.UNDIRECTED) {
            adjMatrix[to][from] = weight;
        }
        edgeCount++;
    }

    /**
     * 获取邻接矩阵（用于调试查看）
     */
    public int[][] getAdjMatrix() {
        return adjMatrix;
    }

    /**
     * 获取顶点数
     */
    public int getVertexCount() {
        return vertexCount;
    }

    /**
     * 获取顶点数据
     */
    public Object getVertex(int index) {
        return vertices[index];
    }

    /**
     * 打印邻接矩阵（直观查看图结构）
     */
    public void printAdjMatrix() {
        System.out.println("邻接矩阵（INF表示无直接边）：");
        // 打印顶点索引
        System.out.print("   ");
        for (int i = 0; i < vertexCount; i++) {
            System.out.printf("%4d", i);
        }
        System.out.println();
        // 打印每行数据
        for (int i = 0; i < vertexCount; i++) {
            System.out.printf("%3d", i);
            for (int j = 0; j < vertexCount; j++) {
                if (adjMatrix[i][j] == INF) {
                    System.out.printf("%4s", "INF");
                } else {
                    System.out.printf("%4d", adjMatrix[i][j]);
                }
            }
            System.out.println();
        }
    }
}

class AdjacencyListGraph {
    private Object[] vertices;       // 存储顶点数据
    private List<List<Edge>> adjList; // 邻接表：adjList[i]存储顶点i的所有邻接边
    private int vertexCount;         // 顶点数
    private int edgeCount;           // 边数
    private GraphType graphType;     // 图类型（有向/无向）

    /**
     * 构造器：初始化图
     * @param vertexCount 顶点数量
     * @param graphType 图类型
     */
    public AdjacencyListGraph(int vertexCount, GraphType graphType) {
        this.vertexCount = vertexCount;
        this.graphType = graphType;
        this.vertices = new Object[vertexCount];
        // 初始化邻接表：每个顶点对应一个空链表
        this.adjList = new ArrayList<>(vertexCount);
        for (int i = 0; i < vertexCount; i++) {
            adjList.add(new ArrayList<>());
        }
    }

    /**
     * 添加顶点
     * @param index 顶点索引（0~vertexCount-1）
     * @param data 顶点数据
     */
    public void addVertex(int index, Object data) {
        if (index < 0 || index >= vertexCount) {
            throw new IllegalArgumentException("顶点索引超出范围");
        }
        vertices[index] = data;
    }

    /**
     * 添加边
     * @param from 起点索引
     * @param to 终点索引
     * @param weight 边权重（无权重图传1）
     */
    public void addEdge(int from, int to, int weight) {
        if (from < 0 || from >= vertexCount || to < 0 || to >= vertexCount) {
            throw new IllegalArgumentException("顶点索引超出范围");
        }
        // 有向图：仅添加from→to的边
        adjList.get(from).add(new Edge(from, to, weight));
        // 无向图：同时添加to→from的边（权重相同）
        if (graphType == GraphType.UNDIRECTED) {
            adjList.get(to).add(new Edge(to, from, weight));
        }
        edgeCount++;
    }

    /**
     * 获取邻接表（用于后续算法）
     */
    public List<List<Edge>> getAdjList() {
        return adjList;
    }

    /**
     * 获取顶点数
     */
    public int getVertexCount() {
        return vertexCount;
    }

    /**
     * 获取顶点数据
     */
    public Object getVertex(int index) {
        return vertices[index];
    }

    /**
     * 打印邻接表（直观查看图结构）
     */
    public void printAdjList() {
        System.out.println("邻接表：");
        for (int i = 0; i < vertexCount; i++) {
            System.out.print("顶点" + i + "(" + vertices[i] + ")的邻接边：");
            List<Edge> edges = adjList.get(i);
            if (edges.isEmpty()) {
                System.out.print("无");
            } else {
                for (Edge edge : edges) {
                    System.out.print(edge + " ");
                }
            }
            System.out.println();
        }
    }
}

class GraphBFS {
    /**
     * 广度优先遍历（从指定起点开始）
     * @param graph 邻接表图
     * @param startIndex 起始顶点索引
     * @return 遍历顺序的顶点索引列表
     */
    public static List<Integer> bfs(AdjacencyListGraph graph, int startIndex) {
        List<Integer> res = new ArrayList<>();
        int vcount = graph.getVertexCount();
        boolean[] visited = new boolean[vcount];
        Queue<Integer> queue = new LinkedList<>();

        queue.offer(startIndex);
        visited[startIndex] = true;

        while(!queue.isEmpty()){
            int curr = queue.poll();
            res.add(curr);

            for (Edge edge : graph.getAdjList().get(curr)) {
                int next = edge.to;
                if(!visited[next]){
                    queue.offer(next);
                    visited[next] = true;
                }
            }
        }
        return res;
    }

    // 适配邻接矩阵的BFS（逻辑相同，仅查询邻接边方式不同）
    public static List<Integer> bfs(AdjacencyMatrixGraph graph, int startIndex) {
        List<Integer> res = new ArrayList<>();
        Queue<Integer> queue = new LinkedList<>();
        int vcount = graph.getVertexCount();
        boolean[] visited = new boolean[vcount];
        int INF = AdjacencyMatrixGraph.INF;

        queue.offer(startIndex);
        visited[startIndex] = true;
        while(!queue.isEmpty()){
            int curr = queue.poll();
            res.add(curr);

            for (int i = 0; i < vcount; i++) {
                int next = graph.getAdjMatrix()[curr][i];
                if(next != INF && next != 0 && !visited[next] ){
                    queue.offer(next);
                    visited[next] = true;
                }
            }
        }
        return res;
    }
}
class GraphDFS {
    /**
     * 递归版DFS（从指定起点开始）
     *
     * @param graph      邻接表图
     * @param startIndex 起始顶点索引
     * @return 遍历顺序的顶点索引列表
     */
    public static List<Integer> dfsRecursive(AdjacencyListGraph graph, int startIndex) {
        List<Integer> res = new ArrayList<>();
        int vcount = graph.getVertexCount();
        boolean[] visited = new boolean[vcount];
        dfs(graph, startIndex, visited, res);
        return res;

    }

    // 递归辅助方法
    private static void dfs(AdjacencyListGraph graph, int curr, boolean[] visited, List<Integer> result) {
        visited[curr] = true;
        result.add(curr);

        for (Edge edge : graph.getAdjList().get(curr)) {
            int next = edge.to;
            if (!visited[next]) {
                dfs(graph, next, visited, result);
            }
        }
    }


    // 适配邻接矩阵的递归版DFS

    public static List<Integer> dfsRecursive(AdjacencyMatrixGraph graph,int startindex){
        List<Integer> res = new ArrayList<>();
        int vcount = graph.getVertexCount();
        boolean[] visited = new boolean[vcount];
        dfs(graph,startindex,visited,res);

        return res;

    }

    private static void dfs(AdjacencyMatrixGraph graph,int curr, boolean[] visited, List<Integer> result){
        visited[curr] = true;
        result.add(curr);

        for (int i = 0; i < graph.getVertexCount(); i++) {
            int next = graph.getAdjMatrix()[curr][i];
            if(next != Integer.MAX_VALUE && next != 0 && !visited[next]){
                dfs(graph,next,visited,result);
            }
        }

    }

}


class PrimMST {
    /**
     * 求最小生成树
     * @param graph 邻接矩阵图（无向连通图）
     * @param startIndex 起始顶点索引
     * @return 最小生成树的边列表（权重和最小）
     */
    public static List<Edge> prim(AdjacencyMatrixGraph graph, int startIndex) {
        List<Edge> mstEdges = new ArrayList<>(); //存储最小生成树的边集
        int[][] adjMatrix = graph.getAdjMatrix(); //得到邻接矩阵
        int vcount = graph.getVertexCount();  //得到顶点数
        int INF = AdjacencyMatrixGraph.INF; //最大int值，标记顶点间无联通

        boolean[] inMst = new boolean[vcount]; //标记顶点是否已经加入树中（区分已选集与未选集）
        int[] minDist = new int[vcount]; //记录顶点到已选集合的距离，初始为INF
        int[] parent = new int[vcount];
        Arrays.fill(minDist,INF);
        Arrays.fill(parent,-1);
        //初始化操作
        minDist[startIndex] = 0;

        //关键步骤
        for (int i = 0; i < vcount; i++) {
            //1.每次从未选集合中选择一个距离已选集合最近的点加入已选集合中
            int min = INF;
            int u = -1; //本次准备加入的顶点，如果最终值没有更新说明图有问题
            for (int v = 0; v < vcount; v++) {
                if(!inMst[v] && minDist[v] < min){
                    min = minDist[v];
                    u = v;
                }
            }

            if(u == -1){
                throw new RuntimeException("图未连通，无法得到生成树");
            }

            //选好顶点，加入集合
            inMst[u] = true;
            if(parent[u] != -1){
               //除起始顶点外都有父顶点
               mstEdges.add(new Edge(parent[u],u,minDist[u]));
            }

            //2.更新minDist数组
            for (int v = 0; v < vcount; v++) {
                if(!inMst[v] && adjMatrix[u][v] != INF && adjMatrix[u][v] < minDist[v]){
                    minDist[v] = adjMatrix[u][v];
                    parent[v] = u;
                }
            }


        }
        return mstEdges;
    }
    //重载方法
    public static List<Edge> prim(AdjacencyMatrixGraph graph){
        return prim(graph,0);
    }

    //邻接表实现prim算法
    public static List<Edge> prim(AdjacencyListGraph graph, int startIndex){
        List<Edge> mstEdges = new ArrayList<>();
        int vcount = graph.getVertexCount();//得到顶点数
        List<List<Edge>> list = graph.getAdjList(); //得到邻接矩阵

        int[] parent = new int[vcount];
        int[] mindist = new int[vcount];
        boolean[] inMst = new boolean[vcount];

        Arrays.fill(parent,-1);
        Arrays.fill(mindist,Integer.MAX_VALUE);
        mindist[startIndex] = 0;

        for (int i = 0; i < vcount; i++) {
            int min = Integer.MAX_VALUE;
            int u = -1;

            for (int v = 0; v < vcount; v++) {
                if(!inMst[v] && mindist[v] < min){
                    min = mindist[v];
                    u = v;
                }
            }

            if(u == -1){
                throw new RuntimeException("图未连通，无法得到生成树");
            }

            inMst[u] = true;
            if(parent[u] != -1){
                mstEdges.add(new Edge(parent[u],u,mindist[u]));
            }

            for (Edge edge : list.get(u)) {
                int next = edge.to;
                int weight = edge.weight;
                if(!inMst[next] && weight < mindist[next]){
                    mindist[next] = weight;
                    parent[next] = u;
                }
            }
        }
        return mstEdges;

    }





    /**
     * 计算最小生成树的总权重
     */
    public static int calculateTotalWeight(List<Edge> mstEdges) {
        return mstEdges.stream().mapToInt(Edge::getWeight).sum();
    }
}

class KruskalMST {
    /**
     * 求最小生成树
     *
     * @param graph 邻接表图（无向连通图）
     * @return 最小生成树的边列表
     */
    public static List<Edge> kruskal(AdjacencyListGraph graph) {
        List<Edge> mstEdges = new ArrayList<>();
        List<List<Edge>> list = graph.getAdjList();
        int vcount = graph.getVertexCount();

        //1.收集所有边
        HashSet<Edge> set = new HashSet<>();
        for (int i = 0; i < vcount; i++) {
            for (Edge edge : list.get(i)) {
                if(edge.from < edge.to){
                    //避免无向边重复收集
                    set.add(edge);
                }
            }
        }

        //2.对边排序
        List<Edge> edgeList = new ArrayList<>(set);
        Collections.sort(edgeList);
        //并查集，用来检验
        DSU dsu = new DSU(vcount);

        //3.开始选边，每次选择权值最小的边直到选完
        for (Edge edge : edgeList) {
            int from = edge.from;
            int to = edge.to;

            if(!dsu.isConnected(from,to)){
                //没有冲突，可以选择
                mstEdges.add(edge);
                dsu.merge(from,to);
                if(mstEdges.size() == vcount-1){
                    break;
                }
            }

        }

        if(mstEdges.size() != vcount - 1){
            throw new RuntimeException("图未连通");
        }
        return mstEdges;
    }
}

class DSU{
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

class BFSShortestPath {
    /**
     * 求起点到所有顶点的最短路径
     * @param graph 邻接表图（无权重/等权重）
     * @param startIndex 起点索引
     * @return 数组：index=顶点索引，value=起点到该顶点的最短距离（-1表示不可达）
     */
    public static int[] bfsShortestPath(AdjacencyListGraph graph, int startIndex) {
        int vcount = graph.getVertexCount();
        int[] distance = new int[vcount];
        int[] parent = new int[vcount]; //用于记录路径
        List<List<Edge>> list = graph.getAdjList();

        Arrays.fill(parent,-1);
        Arrays.fill(distance,-1);
        distance[startIndex] = 0;
        Queue<Integer> queue = new LinkedList<>();
        queue.offer(startIndex);

        while(!queue.isEmpty()){
            int curr = queue.poll();
            for (Edge edge : list.get(curr)) {
                int next = edge.to;
                if(distance[next] == -1){
                    distance[next] = distance[curr]+1;
                    parent[next] = curr;
                    queue.offer(next);
                }
            }
        }
        return distance;
    }

    /**
     * 还原起点到目标顶点的最短路径
     */
    public static List<Integer> getPath(int[] parent, int startIndex, int targetIndex) {
        List<Integer> path = new ArrayList<>();
        // 目标顶点不可达
        if (parent[targetIndex] == -1 && startIndex != targetIndex) {
            return path;
        }

        // 从目标顶点回溯到起点
        for (int i = targetIndex; i != -1; i = parent[i]) {
            path.add(i);
        }
        // 反转路径
        Collections.reverse(path);
        return path;
    }
}

class DijkstraShortestPath {
    public static final int INF = Integer.MAX_VALUE;

    /**
     * 求起点到所有顶点的最短路径
     * @param graph 邻接表图（权重非负）
     * @param startIndex 起点索引
     * @return distance数组：index=顶点索引，value=最短距离（INF表示不可达）
     */
    public static int[] dijkstra(AdjacencyListGraph graph, int startIndex) {
        int vcount = graph.getVertexCount();
        List<List<Edge>> list = graph.getAdjList();
        boolean[] visited = new boolean[vcount];
        int[] parent = new int[vcount];
        int[] distance = new int[vcount];//记录原点到节点的距离

        Arrays.fill(distance,Integer.MAX_VALUE);
        Arrays.fill(parent,-1);
        distance[startIndex] = 0; //初始化初始顶点到自身的距离

        for (int i = 0; i < vcount-1; i++) {
            //剩余vcount-1个顶点的最短路径没记录

            //1.每次选择一个到源点最近的顶点
            int u = -1;
            int min = Integer.MAX_VALUE;
            for (int j = 0; j < vcount; j++) {
                if(!visited[j] && distance[j] < min){
                    min = distance[j];
                    u = j;
                }
            }

            //标记该节点为已访问节点
            visited[u] = true;
            //2.以该顶点继续更新distance数组
            for (Edge edge : list.get(u)) {
                int next = edge.to;
                int weight = edge.weight;

                if(!visited[next] && distance[u] + weight < distance[next]){
                    distance[next] = distance[u] + weight;
                }
            }

        }

        return distance;

    }

    /**
     * 还原起点到目标顶点的最短路径
     */
    public static List<Integer> getPath(int[] parent, int startIndex, int targetIndex) {
        List<Integer> path = new ArrayList<>();
        if (parent[targetIndex] == -1 && startIndex != targetIndex) {
            return path; // 不可达
        }

        for (int i = targetIndex; i != -1; i = parent[i]) {
            path.add(i);
        }
        Collections.reverse(path);
        return path;
    }
}

class FloydShortestPath {
    public static final int INF = Integer.MAX_VALUE;

    /**
     * 求所有顶点对之间的最短路径
     * @param graph 邻接矩阵图
     * @return dist二维数组：dist[i][j]表示i到j的最短距离（INF表示不可达）
     */
    public static int[][] floyd(AdjacencyMatrixGraph graph) {
        int vcount = graph.getVertexCount();
        int[][] dist = new int[vcount][vcount];
        int[][] adjMatrix = graph.getAdjMatrix();

        for (int i = 0; i < vcount; i++) {
            for (int j = 0; j < vcount; j++) {
                if(i == j){
                    dist[i][j] = 0;
                }else{
                    dist[i][j] = adjMatrix[i][j];
                }
            }
        }

        //枚举中间顶点k
        for (int k = 0; k < vcount; k++) {
            //枚举起点i
            for (int i = 0; i < vcount; i++) {
                //枚举终点j
                for (int j = 0; j < vcount; j++) {
                    if(dist[i][k] != INF && dist[k][j] != INF &&
                            dist[i][k] + dist[k][j] < dist[i][j]){
                        dist[i][j] = dist[i][k] + dist[k][j];
                    }
                }
            }
        }
        return dist;
    }

    /**
     * 打印多源最短路径矩阵
     */
    public static void printFloydResult(int[][] dist) {
        int vertexCount = dist.length;
        System.out.println("多源最短路径矩阵（INF表示不可达）：");
        // 打印顶点索引
        System.out.print("   ");
        for (int i = 0; i < vertexCount; i++) {
            System.out.printf("%4d", i);
        }
        System.out.println();
        // 打印每行数据
        for (int i = 0; i < vertexCount; i++) {
            System.out.printf("%3d", i);
            for (int j = 0; j < vertexCount; j++) {
                if (dist[i][j] == INF) {
                    System.out.printf("%4s", "INF");
                } else {
                    System.out.printf("%4d", dist[i][j]);
                }
            }
            System.out.println();
        }
    }
}

class TopologicalSortRecursive {
    private boolean[] visited;    // 标记是否已访问
    private boolean[] onPath;     // 标记当前递归路径（检测环）
    private Stack<Integer> stack; // 存储拓扑排序结果（逆序）
    private boolean hasCycle;     // 是否存在环（DAG无环）

    /**
     * 拓扑排序递归版实现
     * @param graph 邻接表图（有向图）
     * @return 拓扑排序的顶点索引列表（空列表表示有环）
     */
    public List<Integer> topologicalSort(AdjacencyListGraph graph) {
        List<Integer> res = new ArrayList<>();
        stack = new Stack<>();
        int vcount = graph.getVertexCount();
        onPath = new boolean[vcount];
        hasCycle = false;
        visited = new boolean[vcount];

        for (int i = 0; i < vcount; i++) {
            if(!visited[i] && !hasCycle){
                dfs(graph,i);
            }
        }
        if(hasCycle){
            throw new RuntimeException("图存在环，无法拓扑排序");
        }

        while(!stack.isEmpty()){
            res.add(stack.pop());
        }
        return res;
    }

    // 递归DFS辅助方法
    private void dfs(AdjacencyListGraph graph, int curr) {
        // 检测环：当前顶点已在递归路径上
        if (onPath[curr]) {
            hasCycle = true;
            return;
        }

        // 若已访问，直接返回
        if (visited[curr]) {
            return;
        }

        // 标记：加入递归路径和已访问
        onPath[curr] = true;
        visited[curr] = true;

        // 遍历当前顶点的所有邻接顶点
        for (Edge edge : graph.getAdjList().get(curr)) {
            int next = edge.to;
            dfs(graph, next);
            if (hasCycle) { // 发现环，提前返回
                return;
            }
        }

        // 回溯：移除递归路径标记
        onPath[curr] = false;
        // 所有邻接顶点遍历完毕，压入栈
        stack.push(curr);
    }

    public List<Integer> topoSort(AdjacencyListGraph graph){
        int vcount = graph.getVertexCount();
        List<List<Edge>> list = graph.getAdjList();
        int[] indgree = new int[vcount];
        List<Integer> res = new ArrayList<>();
        //1.统计所有节点入度
        for (int i = 0; i < vcount; i++) {
            for (Edge edge : list.get(i)) {
                indgree[edge.to]++;
            }
        }
        //2.将入度为0的节点加入队列中
        Queue<Integer> queue = new LinkedList<>();
        for (int i = 0; i < vcount; i++) {
            if(indgree[i] == 0){
                queue.offer(i);
            }
        }

        //3.从队列中取出入度为0的顶点，更新与其相连节点的入度
        while(!queue.isEmpty()){
            int curr = queue.poll();
            res.add(curr);

            for (Edge edge : list.get(curr)) {
                indgree[edge.to]--;
                if(indgree[edge.to] == 0){
                    queue.offer(edge.to);
                }
            }
        }

        if(res.size() < vcount){
            throw new RuntimeException("图存在环，无法拓扑排序");
        }

        return res;



    }
}






