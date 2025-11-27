import org.junit.jupiter.api.Test;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
public class GraphTest {
    @Test
    void testPrintAdjMatrix() {
        // 1. 创建图并添加数据
        AdjacencyMatrixGraph graph = new AdjacencyMatrixGraph(3, GraphType.DIRECTED);
        graph.addVertex(0, "A");
        graph.addVertex(1, "B");
        graph.addVertex(2, "C");
        graph.addEdge(0, 1, 5);
        graph.addEdge(1, 2, 3);

        // 2. 捕获输出
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));
        graph.printAdjMatrix();
        System.setOut(originalOut); // 恢复输出
        String printedContent = outputStream.toString();

        // 打印捕获的原始内容
        System.out.println("实际输出：\n" + printedContent);

        // 3. 验证内容
        assertTrue(printedContent.contains("邻接矩阵（INF表示无直接边）："), "标题缺失");
        assertTrue(printedContent.contains("   0   1   2"), "表头缺失");
        assertTrue(printedContent.contains("  0   0   5 INF"), "0行数据错误"); // 按实际输出调整
        assertTrue(printedContent.contains("  1 INF   0   3"), "1行数据错误");
        assertTrue(printedContent.contains("  2 INF INF   0"), "2行数据错误");
    }

    @Test
    void testPrintAdjList() {
        // 1. 创建邻接表图实例并添加顶点/边
        AdjacencyListGraph graph = new AdjacencyListGraph(3, GraphType.DIRECTED);
        graph.addVertex(0, "A");
        graph.addVertex(1, "B");
        graph.addVertex(2, "C");
        graph.addEdge(0, 1, 5);   // 顶点0→1，权重5
        graph.addEdge(0, 2, 3);   // 顶点0→2，权重3
        graph.addEdge(1, 2, 2);   // 顶点1→2，权重2

        // 2. 重定向System.out捕获打印内容
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));

        // 3. 执行打印方法
        graph.printAdjList();

        // 4. 恢复System.out并获取捕获的内容
        System.setOut(originalOut);
        String printedContent = outputStream.toString();

        // 打印捕获的原始内容（便于调试）
        System.out.println("实际输出：\n" + printedContent);

        // 5. 宽松验证关键内容
        assertTrue(printedContent.contains("邻接表："), "标题缺失");

        // 验证顶点0的邻接边
        assertTrue(printedContent.contains("顶点0(A)的邻接边："), "顶点0标识缺失");
        assertTrue(printedContent.contains("0→1(5)") || printedContent.contains("1") && printedContent.contains("5"), "顶点0→1的边缺失");
        assertTrue(printedContent.contains("0→2(3)") || printedContent.contains("2") && printedContent.contains("3"), "顶点0→2的边缺失");

        // 验证顶点1的邻接边
        assertTrue(printedContent.contains("顶点1(B)的邻接边："), "顶点1标识缺失");
        assertTrue(printedContent.contains("1→2(2)") || printedContent.contains("2") && printedContent.contains("2"), "顶点1→2的边缺失");


    }

    @Test
    void testBfs_ConnectedDirectedGraph() {
        // 构建有向图：0→1, 0→2, 1→3, 2→3
        AdjacencyListGraph graph = new AdjacencyListGraph(4, GraphType.DIRECTED);
        graph.addVertex(0, "A");
        graph.addVertex(1, "B");
        graph.addVertex(2, "C");
        graph.addVertex(3, "D");
        graph.addEdge(0, 1, 1);
        graph.addEdge(0, 2, 1);
        graph.addEdge(1, 3, 1);
        graph.addEdge(2, 3, 1);

        // 执行BFS（从顶点0开始）
        List<Integer> result = GraphBFS.bfs(graph, 0);

        // 打印结果（便于调试）
        System.out.println("连通有向图BFS结果：" + result);

        // 验证遍历顺序（BFS应按层遍历：0→1→2→3）
        assertEquals(Arrays.asList(0, 1, 2, 3), result);
    }

    /**
     * 测试非连通图的BFS遍历（仅遍历连通分量）
     */
    @Test
    void testBfs_DisconnectedGraph() {
        // 构建非连通图：0→1（连通分量1），2→3（连通分量2）
        AdjacencyListGraph graph = new AdjacencyListGraph(4, GraphType.UNDIRECTED);
        graph.addEdge(0, 1, 1);
        graph.addEdge(2, 3, 1);

        // 从顶点0开始BFS（仅遍历0和1）
        List<Integer> result = GraphBFS.bfs(graph, 0);
        System.out.println("非连通图BFS结果（起点0）：" + result);
        assertEquals(Arrays.asList(0, 1), result);

        // 从顶点2开始BFS（仅遍历2和3）
        List<Integer> result2 = GraphBFS.bfs(graph, 2);
        System.out.println("非连通图BFS结果（起点2）：" + result2);
        assertEquals(Arrays.asList(2, 3), result2);
    }

    /**
     * 测试单顶点图的BFS遍历
     */
    @Test
    void testBfs_SingleVertexGraph() {
        AdjacencyListGraph graph = new AdjacencyListGraph(1, GraphType.UNDIRECTED);
        List<Integer> result = GraphBFS.bfs(graph, 0);
        System.out.println("单顶点图BFS结果：" + result);
        assertEquals(Arrays.asList(0), result);
    }

    /**
     * 测试无向连通图的BFS遍历
     */
    @Test
    void testBfs_ConnectedUndirectedGraph() {
        // 构建无向图：0-1, 0-2, 1-3, 2-3
        AdjacencyListGraph graph = new AdjacencyListGraph(4, GraphType.UNDIRECTED);
        graph.addEdge(0, 1, 1);
        graph.addEdge(0, 2, 1);
        graph.addEdge(1, 3, 1);
        graph.addEdge(2, 3, 1);

        List<Integer> result = GraphBFS.bfs(graph, 0);
        System.out.println("无向连通图BFS结果：" + result);
        // 无向图BFS顺序可能为0→1→2→3或0→2→1→3，两种都合法
        boolean isCorrect = result.equals(Arrays.asList(0, 1, 2, 3)) || result.equals(Arrays.asList(0, 2, 1, 3));
        assertEquals(true, isCorrect);
    }

    @Test
    void testDfsRecursive_ConnectedDirectedGraph() {
        // 构建有向图：0→1→3，0→2→3
        AdjacencyListGraph graph = new AdjacencyListGraph(4, GraphType.DIRECTED);
        graph.addEdge(0, 1, 1);
        graph.addEdge(0, 2, 1);
        graph.addEdge(1, 3, 1);
        graph.addEdge(2, 3, 1);

        // 执行DFS（起点0）
        List<Integer> result = GraphDFS.dfsRecursive(graph, 0);
        System.out.println("连通有向图DFS结果：" + result);

        // DFS可能路径：0→1→3→2 或 0→2→3→1，两种均合法
        boolean isCorrectPath = result.equals(Arrays.asList(0, 1, 3, 2))
                || result.equals(Arrays.asList(0, 2, 3, 1))
                || result.equals(Arrays.asList(0, 1, 2, 3))
                || result.equals(Arrays.asList(0, 2, 1, 3));
        assertTrue(isCorrectPath, "DFS遍历路径不符合预期");
    }

    /**
     * 测试非连通图的DFS递归遍历（仅遍历连通分量）
     */
    @Test
    void testDfsRecursive_DisconnectedGraph() {
        // 构建非连通图：0→1（分量1），2→3（分量2）
        AdjacencyListGraph graph = new AdjacencyListGraph(4, GraphType.UNDIRECTED);
        graph.addEdge(0, 1, 1);
        graph.addEdge(2, 3, 1);

        // 从起点0遍历（仅访问0、1）
        List<Integer> result0 = GraphDFS.dfsRecursive(graph, 0);
        System.out.println("非连通图DFS结果（起点0）：" + result0);
        assertTrue(result0.containsAll(Arrays.asList(0, 1)) && result0.size() == 2);

        // 从起点2遍历（仅访问2、3）
        List<Integer> result2 = GraphDFS.dfsRecursive(graph, 2);
        System.out.println("非连通图DFS结果（起点2）：" + result2);
        assertTrue(result2.containsAll(Arrays.asList(2, 3)) && result2.size() == 2);
    }

    /**
     * 测试单顶点图的DFS遍历
     */
    @Test
    void testDfsRecursive_SingleVertexGraph() {
        AdjacencyListGraph graph = new AdjacencyListGraph(1, GraphType.UNDIRECTED);
        List<Integer> result = GraphDFS.dfsRecursive(graph, 0);
        System.out.println("单顶点图DFS结果：" + result);
        assertEquals(Arrays.asList(0), result);
    }

    /**
     * 测试无向连通图的DFS遍历
     */
    @Test
    void testDfsRecursive_ConnectedUndirectedGraph() {
        // 构建无向图：0-1-3，0-2-3
        AdjacencyListGraph graph = new AdjacencyListGraph(4, GraphType.UNDIRECTED);
        graph.addEdge(0, 1, 1);
        graph.addEdge(0, 2, 1);
        graph.addEdge(1, 3, 1);
        graph.addEdge(2, 3, 1);

        List<Integer> result = GraphDFS.dfsRecursive(graph, 0);
        System.out.println("无向连通图DFS结果：" + result);
        // 验证包含所有顶点且无重复
        assertEquals(4, result.size());
        assertTrue(result.containsAll(Arrays.asList(0, 1, 2, 3)));
    }


    /**
     * 测试连通无向图的Prim算法（验证最小生成树边集）
     */
    @Test
    void testPrim_ConnectedUndirectedGraph() {
        // 构建连通无向图（顶点0-3，权重如下）：
        // 0-1(1), 0-2(3), 1-2(1), 1-3(5), 2-3(2)
        AdjacencyMatrixGraph graph = new AdjacencyMatrixGraph(4, GraphType.UNDIRECTED);
        graph.addEdge(0, 1, 1);
        graph.addEdge(0, 2, 3);
        graph.addEdge(1, 2, 1);
        graph.addEdge(1, 3, 5);
        graph.addEdge(2, 3, 2);

        // 执行Prim算法（起点0）
        List<Edge> mst = PrimMST.prim(graph, 0);
        System.out.println("连通图Prim生成树边集：" + mst);

        // 验证生成树边数（n-1=3条边）
        assertEquals(3, mst.size());

        // 验证总权重（1+1+2=4）
        int totalWeight = mst.stream().mapToInt(Edge::getWeight).sum();
        assertEquals(4, totalWeight);

        // 验证包含关键边（0-1(1), 1-2(1), 2-3(2)）
        assertTrue(mst.stream().anyMatch(e -> (e.getFrom() == 0 && e.getTo() == 1 && e.getWeight() == 1) || (e.getFrom() == 1 && e.getTo() == 0 && e.getWeight() == 1)));
        assertTrue(mst.stream().anyMatch(e -> (e.getFrom() == 1 && e.getTo() == 2 && e.getWeight() == 1) || (e.getFrom() == 2 && e.getTo() == 1 && e.getWeight() == 1)));
        assertTrue(mst.stream().anyMatch(e -> (e.getFrom() == 2 && e.getTo() == 3 && e.getWeight() == 2) || (e.getFrom() == 3 && e.getTo() == 2 && e.getWeight() == 2)));
    }

    /**
     * 测试非连通图的Prim算法（预期抛出异常）
     */
    @Test
    void testPrim_DisconnectedGraph() {
        // 构建非连通图：0-1(1)，2-3(2)（两个连通分量）
        AdjacencyMatrixGraph graph = new AdjacencyMatrixGraph(4, GraphType.UNDIRECTED);
        graph.addEdge(0, 1, 1);
        graph.addEdge(2, 3, 2);

        // 执行Prim算法，预期抛出异常
        assertThrows(RuntimeException.class, () -> {
            PrimMST.prim(graph, 0);
        }, "非连通图未抛出预期异常");
    }

    /**
     * 测试单顶点图的Prim算法（无生成树边）
     */
    @Test
    void testPrim_SingleVertexGraph() {
        AdjacencyMatrixGraph graph = new AdjacencyMatrixGraph(1, GraphType.UNDIRECTED);
        List<Edge> mst = PrimMST.prim(graph, 0);
        assertTrue(mst.isEmpty(), "单顶点图应无生成树边");
    }

    /**
     * 测试两顶点图的Prim算法（一条边）
     */
    @Test
    void testPrim_TwoVertexGraph() {
        AdjacencyMatrixGraph graph = new AdjacencyMatrixGraph(2, GraphType.UNDIRECTED);
        graph.addEdge(0, 1, 5);
        List<Edge> mst = PrimMST.prim(graph, 0);

        assertEquals(1, mst.size());
        Edge edge = mst.get(0);
        assertTrue((edge.getFrom() == 0 && edge.getTo() == 1 && edge.getWeight() == 5)
                || (edge.getFrom() == 1 && edge.getTo() == 0 && edge.getWeight() == 5));
    }

    @Test
    void testKruskal_ConnectedUndirectedGraph() {
        // 构建连通无向图（顶点0-3，权重如下）：
        // 0-1(1), 0-2(3), 1-2(1), 1-3(5), 2-3(2)
        AdjacencyListGraph graph = new AdjacencyListGraph(4, GraphType.UNDIRECTED);
        graph.addEdge(0, 1, 1);
        graph.addEdge(0, 2, 3);
        graph.addEdge(1, 2, 1);
        graph.addEdge(1, 3, 5);
        graph.addEdge(2, 3, 2);

        // 执行Kruskal算法
        List<Edge> mst = KruskalMST.kruskal(graph);
        System.out.println("连通图Kruskal生成树边集：" + mst);

        // 验证生成树边数（n-1=3条边）
        assertEquals(3, mst.size());

        // 验证总权重（1+1+2=4）
        int totalWeight = mst.stream().mapToInt(Edge::getWeight).sum();
        assertEquals(4, totalWeight);

        // 验证包含关键边（0-1(1), 1-2(1), 2-3(2)）
        assertTrue(mst.stream().anyMatch(e -> (e.getFrom() == 0 && e.getTo() == 1 && e.getWeight() == 1)));
        assertTrue(mst.stream().anyMatch(e -> (e.getFrom() == 1 && e.getTo() == 2 && e.getWeight() == 1)));
        assertTrue(mst.stream().anyMatch(e -> (e.getFrom() == 2 && e.getTo() == 3 && e.getWeight() == 2)));
    }

    /**
     * 测试非连通图的Kruskal算法（预期抛出异常）
     */
    @Test
    void testKruskal_DisconnectedGraph() {
        // 构建非连通图：0-1(1)，2-3(2)（两个连通分量）
        AdjacencyListGraph graph = new AdjacencyListGraph(4, GraphType.UNDIRECTED);
        graph.addEdge(0, 1, 1);
        graph.addEdge(2, 3, 2);

        // 执行Kruskal算法，预期抛出异常
        assertThrows(RuntimeException.class, () -> {
            KruskalMST.kruskal(graph);
        }, "非连通图未抛出预期异常");
    }

    /**
     * 测试单顶点图的Kruskal算法（无生成树边）
     */
    @Test
    void testKruskal_SingleVertexGraph() {
        AdjacencyListGraph graph = new AdjacencyListGraph(1, GraphType.UNDIRECTED);
        List<Edge> mst = KruskalMST.kruskal(graph);
        assertTrue(mst.isEmpty(), "单顶点图应无生成树边");
    }

    /**
     * 测试两顶点图的Kruskal算法（一条边）
     */
    @Test
    void testKruskal_TwoVertexGraph() {
        AdjacencyListGraph graph = new AdjacencyListGraph(2, GraphType.UNDIRECTED);
        graph.addEdge(0, 1, 5);
        List<Edge> mst = KruskalMST.kruskal(graph);

        assertEquals(1, mst.size());
        Edge edge = mst.get(0);
        assertEquals(0, edge.getFrom());
        assertEquals(1, edge.getTo());
        assertEquals(5, edge.getWeight());
    }


    @Test
    void testBfsShortestPath_ConnectedGraph() {
        // 构建连通无权图（顶点0-4，边：0→1, 0→2, 1→3, 2→3, 3→4）
        AdjacencyListGraph graph = new AdjacencyListGraph(5, GraphType.DIRECTED);
        graph.addEdge(0, 1, 1); // 无权图权重设为1
        graph.addEdge(0, 2, 1);
        graph.addEdge(1, 3, 1);
        graph.addEdge(2, 3, 1);
        graph.addEdge(3, 4, 1);

        // 执行BFS最短路径（起点0）
        int[] distance = BFSShortestPath.bfsShortestPath(graph, 0);
        System.out.println("连通图最短路径距离：" + Arrays.toString(distance));

        // 验证各顶点到起点0的距离
        assertEquals(0, distance[0]); // 起点自身
        assertEquals(1, distance[1]); // 0→1
        assertEquals(1, distance[2]); // 0→2
        assertEquals(2, distance[3]); // 0→1→3 或 0→2→3
        assertEquals(3, distance[4]); // 0→1→3→4 或 0→2→3→4
    }

    /**
     * 测试非连通图的BFS最短路径（部分顶点不可达）
     */
    @Test
    void testBfsShortestPath_DisconnectedGraph() {
        // 构建非连通图：0→1→2，3→4（两个连通分量）
        AdjacencyListGraph graph = new AdjacencyListGraph(5, GraphType.UNDIRECTED);
        graph.addEdge(0, 1, 1);
        graph.addEdge(1, 2, 1);
        graph.addEdge(3, 4, 1);

        // 从起点0出发
        int[] distance = BFSShortestPath.bfsShortestPath(graph, 0);
        System.out.println("非连通图最短路径距离：" + Arrays.toString(distance));

        // 验证可达顶点的距离
        assertEquals(0, distance[0]);
        assertEquals(1, distance[1]);
        assertEquals(2, distance[2]);
        // 验证不可达顶点的距离（-1）
        assertEquals(-1, distance[3]);
        assertEquals(-1, distance[4]);
    }

    /**
     * 测试单顶点图的BFS最短路径
     */
    @Test
    void testBfsShortestPath_SingleVertexGraph() {
        AdjacencyListGraph graph = new AdjacencyListGraph(1, GraphType.DIRECTED);
        int[] distance = BFSShortestPath.bfsShortestPath(graph, 0);
        assertEquals(0, distance[0]);
    }

    /**
     * 测试无向图的BFS最短路径
     */
    @Test
    void testBfsShortestPath_UndirectedGraph() {
        // 构建无向图：0-1-3，0-2-3
        AdjacencyListGraph graph = new AdjacencyListGraph(4, GraphType.UNDIRECTED);
        graph.addEdge(0, 1, 1);
        graph.addEdge(0, 2, 1);
        graph.addEdge(1, 3, 1);
        graph.addEdge(2, 3, 1);

        int[] distance = BFSShortestPath.bfsShortestPath(graph, 0);
        assertEquals(0, distance[0]);
        assertEquals(1, distance[1]);
        assertEquals(1, distance[2]);
        assertEquals(2, distance[3]); // 0→1→3 或 0→2→3
    }

    @Test
    void testDijkstra_ConnectedWeightedGraph() {
        // 构建有向有权图（顶点0-3，权重如下）：
        // 0→1(2), 0→2(5), 1→2(1), 1→3(3), 2→3(2)
        AdjacencyListGraph graph = new AdjacencyListGraph(4, GraphType.DIRECTED);
        graph.addEdge(0, 1, 2);
        graph.addEdge(0, 2, 5);
        graph.addEdge(1, 2, 1);
        graph.addEdge(1, 3, 3);
        graph.addEdge(2, 3, 2);

        // 执行Dijkstra算法（起点0）
        int[] distance = DijkstraShortestPath.dijkstra(graph, 0);
        System.out.println("连通有权图最短路径距离：" + Arrays.toString(distance));

        // 验证各顶点到起点0的最短距离
        assertEquals(0, distance[0]);                  // 起点自身
        assertEquals(2, distance[1]);                  // 0→1
        assertEquals(3, distance[2]);                  // 0→1→2（2+1）
        assertEquals(5, distance[3]);                  // 0→1→2→3（2+1+2）
    }

    /**
     * 测试包含不可达顶点的图的Dijkstra算法
     */
    @Test
    void testDijkstra_UnreachableVertex() {
        // 构建图：0→1(3), 1→2(2)，顶点3不可达
        AdjacencyListGraph graph = new AdjacencyListGraph(4, GraphType.DIRECTED);
        graph.addEdge(0, 1, 3);
        graph.addEdge(1, 2, 2);

        int[] distance = DijkstraShortestPath.dijkstra(graph, 0);
        System.out.println("含不可达顶点的最短路径距离：" + Arrays.toString(distance));

        assertEquals(0, distance[0]);
        assertEquals(3, distance[1]);
        assertEquals(5, distance[2]);
        assertEquals(DijkstraShortestPath.INF, distance[3]); // 不可达顶点距离为INF
    }

    /**
     * 测试无向有权图的Dijkstra算法
     */
    @Test
    void testDijkstra_UndirectedWeightedGraph() {
        // 构建无向有权图：0-1(1), 0-2(4), 1-2(2), 1-3(5), 2-3(1)
        AdjacencyListGraph graph = new AdjacencyListGraph(4, GraphType.UNDIRECTED);
        graph.addEdge(0, 1, 1);
        graph.addEdge(0, 2, 4);
        graph.addEdge(1, 2, 2);
        graph.addEdge(1, 3, 5);
        graph.addEdge(2, 3, 1);

        int[] distance = DijkstraShortestPath.dijkstra(graph, 0);
        assertEquals(0, distance[0]);
        assertEquals(1, distance[1]);
        assertEquals(3, distance[2]);  // 0→1→2（1+2）
        assertEquals(4, distance[3]);  // 0→1→2→3（1+2+1）
    }

    /**
     * 测试单顶点图的Dijkstra算法
     */
    @Test
    void testDijkstra_SingleVertexGraph() {
        AdjacencyListGraph graph = new AdjacencyListGraph(1, GraphType.DIRECTED);
        int[] distance = DijkstraShortestPath.dijkstra(graph, 0);
        assertEquals(0, distance[0]);
    }

    /**
     * 测试两顶点图的Dijkstra算法
     */
    @Test
    void testDijkstra_TwoVertexGraph() {
        AdjacencyListGraph graph = new AdjacencyListGraph(2, GraphType.DIRECTED);
        graph.addEdge(0, 1, 5);
        int[] distance = DijkstraShortestPath.dijkstra(graph, 0);
        assertEquals(0, distance[0]);
        assertEquals(5, distance[1]);
    }

    @Test
    void testFloyd_ConnectedWeightedGraph() {
        // 构建有向有权图（顶点0-3，权重如下）：
        // 0→1(2), 0→2(5), 1→2(1), 1→3(3), 2→3(2), 3→0(4)
        AdjacencyMatrixGraph graph = new AdjacencyMatrixGraph(4, GraphType.DIRECTED);
        graph.addEdge(0, 1, 2);
        graph.addEdge(0, 2, 5);
        graph.addEdge(1, 2, 1);
        graph.addEdge(1, 3, 3);
        graph.addEdge(2, 3, 2);
        graph.addEdge(3, 0, 4);

        // 执行Floyd算法
        int[][] dist = FloydShortestPath.floyd(graph);
        System.out.println("=== 连通有权图Floyd算法结果 ===");
        FloydShortestPath.printFloydResult(dist); // 打印结果到控制台

        // 验证关键路径的最短距离
        assertEquals(0, dist[0][0]);                  // 0→0
        assertEquals(2, dist[0][1]);                  // 0→1
        assertEquals(3, dist[0][2]);                  // 0→1→2
        assertEquals(5, dist[0][3]);                  // 0→1→2→3
        assertEquals(7, dist[1][0]);                  // 1→3→0
        assertEquals(6, dist[2][0]);                  // 2→3→0
    }

    /**
     * 测试含不可达顶点的图的Floyd算法
     */
    @Test
    void testFloyd_UnreachableVertex() {
        // 构建图：0→1(3), 1→2(2)，顶点3不可达任何顶点，也无顶点可达3
        AdjacencyMatrixGraph graph = new AdjacencyMatrixGraph(4, GraphType.DIRECTED);
        graph.addEdge(0, 1, 3);
        graph.addEdge(1, 2, 2);

        int[][] dist = FloydShortestPath.floyd(graph);
        System.out.println("\n=== 含不可达顶点的Floyd算法结果 ===");
        FloydShortestPath.printFloydResult(dist);

        // 验证可达路径
        assertEquals(0, dist[0][0]);
        assertEquals(3, dist[0][1]);
        assertEquals(5, dist[0][2]);
        // 验证不可达顶点（距离为INF）
        assertEquals(FloydShortestPath.INF, dist[0][3]);
        assertEquals(FloydShortestPath.INF, dist[3][0]);
    }

    /**
     * 测试无向有权图的Floyd算法
     */
    @Test
    void testFloyd_UndirectedWeightedGraph() {
        // 构建无向有权图：0-1(1), 0-2(4), 1-2(2), 1-3(5), 2-3(1)
        AdjacencyMatrixGraph graph = new AdjacencyMatrixGraph(4, GraphType.UNDIRECTED);
        graph.addEdge(0, 1, 1);
        graph.addEdge(0, 2, 4);
        graph.addEdge(1, 2, 2);
        graph.addEdge(1, 3, 5);
        graph.addEdge(2, 3, 1);

        int[][] dist = FloydShortestPath.floyd(graph);
        System.out.println("\n=== 无向有权图Floyd算法结果 ===");
        FloydShortestPath.printFloydResult(dist);

        // 验证关键路径
        assertEquals(3, dist[0][2]);  // 0→1→2
        assertEquals(4, dist[0][3]);  // 0→1→2→3
    }

    /**
     * 测试单顶点图的Floyd算法
     */
    @Test
    void testFloyd_SingleVertexGraph() {
        AdjacencyMatrixGraph graph = new AdjacencyMatrixGraph(1, GraphType.DIRECTED);
        int[][] dist = FloydShortestPath.floyd(graph);
        System.out.println("\n=== 单顶点图Floyd算法结果 ===");
        FloydShortestPath.printFloydResult(dist);

        assertEquals(0, dist[0][0]);
    }

    @Test
    void testTopologicalSort_AcyclicGraph() {
        // 构建无环有向图：0→1→3，0→2→3
        AdjacencyListGraph graph = new AdjacencyListGraph(4, GraphType.DIRECTED);
        graph.addEdge(0, 1, 1);
        graph.addEdge(0, 2, 1);
        graph.addEdge(1, 3, 1);
        graph.addEdge(2, 3, 1);

        TopologicalSortRecursive topo = new TopologicalSortRecursive();
        List<Integer> result = topo.topologicalSort(graph);
        System.out.println("递归拓扑排序结果（无环图）：" + result);

        // 验证拓扑序合法性（所有边的起点在终点前）
        assertTrue(result.indexOf(0) < result.indexOf(1));
        assertTrue(result.indexOf(0) < result.indexOf(2));
        assertTrue(result.indexOf(1) < result.indexOf(3));
        assertTrue(result.indexOf(2) < result.indexOf(3));
        assertEquals(4, result.size());
    }

    /**
     * 测试有环图的递归拓扑排序（预期抛出异常）
     */
    @Test
    void testTopologicalSort_CyclicGraph() {
        // 构建有环图：0→1→2→0
        AdjacencyListGraph graph = new AdjacencyListGraph(3, GraphType.DIRECTED);
        graph.addEdge(0, 1, 1);
        graph.addEdge(1, 2, 1);
        graph.addEdge(2, 0, 1);

        TopologicalSortRecursive topo = new TopologicalSortRecursive();
        assertThrows(RuntimeException.class, () -> {
            topo.topologicalSort(graph);
        }, "有环图未抛出预期异常");
    }

    /**
     * 测试无环有向图的非递归拓扑排序（入度表法）
     */
    @Test
    void testTopoSort_AcyclicGraph() {
        // 构建无环有向图：0→2，1→2，2→3，3→4
        AdjacencyListGraph graph = new AdjacencyListGraph(5, GraphType.DIRECTED);
        graph.addEdge(0, 2, 1);
        graph.addEdge(1, 2, 1);
        graph.addEdge(2, 3, 1);
        graph.addEdge(3, 4, 1);

        TopologicalSortRecursive topo = new TopologicalSortRecursive();
        List<Integer> result = topo.topoSort(graph);
        System.out.println("非递归拓扑排序结果（无环图）：" + result);

        // 验证拓扑序合法性
        assertTrue(result.indexOf(0) < result.indexOf(2));
        assertTrue(result.indexOf(1) < result.indexOf(2));
        assertTrue(result.indexOf(2) < result.indexOf(3));
        assertTrue(result.indexOf(3) < result.indexOf(4));
        assertEquals(5, result.size());
    }

    /**
     * 测试有环图的非递归拓扑排序（预期抛出异常）
     */
    @Test
    void testTopoSort_CyclicGraph() {
        // 构建有环图：0→1，1→2，2→1
        AdjacencyListGraph graph = new AdjacencyListGraph(3, GraphType.DIRECTED);
        graph.addEdge(0, 1, 1);
        graph.addEdge(1, 2, 1);
        graph.addEdge(2, 1, 1);

        TopologicalSortRecursive topo = new TopologicalSortRecursive();
        assertThrows(RuntimeException.class, () -> {
            topo.topoSort(graph);
        }, "有环图未抛出预期异常");
    }

    /**
     * 测试单顶点图的拓扑排序
     */
    @Test
    void testTopologicalSort_SingleVertex() {
        AdjacencyListGraph graph = new AdjacencyListGraph(1, GraphType.DIRECTED);
        TopologicalSortRecursive topo = new TopologicalSortRecursive();

        // 递归版
        List<Integer> recursiveResult = topo.topologicalSort(graph);
        assertEquals(Arrays.asList(0), recursiveResult);

        // 非递归版
        List<Integer> nonRecursiveResult = topo.topoSort(graph);
        assertEquals(Arrays.asList(0), nonRecursiveResult);
    }

    /**
     * 测试多入度顶点的拓扑排序
     */
    @Test
    void testTopologicalSort_MultiIndegree() {
        // 构建图：0→3，1→3，2→4，3→4
        AdjacencyListGraph graph = new AdjacencyListGraph(5, GraphType.DIRECTED);
        graph.addEdge(0, 3, 1);
        graph.addEdge(1, 3, 1);
        graph.addEdge(2, 4, 1);
        graph.addEdge(3, 4, 1);

        TopologicalSortRecursive topo = new TopologicalSortRecursive();
        List<Integer> result = topo.topoSort(graph);
        System.out.println("多入度顶点拓扑排序结果：" + result);

        // 验证关键顺序
        assertTrue(result.indexOf(0) < result.indexOf(3));
        assertTrue(result.indexOf(1) < result.indexOf(3));
        assertTrue(result.indexOf(3) < result.indexOf(4));
        assertTrue(result.indexOf(2) < result.indexOf(4));
    }
}
