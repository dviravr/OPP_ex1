package ex1.tests;

import ex1.src.*;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class WGraph_AlgoTest {
   private static Random _rnd = null;

   @Test
   void copy() {
      weighted_graph wg = graph_creator_with_start_index(50, 100, 0, 2);
      weighted_graph_algorithms wga = new WGraph_Algo();
      wga.init(wg);
      weighted_graph wg1 = wga.copy();
      assertEquals(wg, wg1);
   }

   @Test
   void bigGraphCreator() {
      bigGraphTest(graph_creator_with_start_index(10, 9, 15, 1), 1, 1);
      bigGraphTest(graph_creator_with_start_index(100, 235, 25, 1), 47, 1);
      bigGraphTest(graph_creator_with_start_index(1000, 3400, 125, 2), 19, 1);
      bigGraphTest(graph_creator_with_start_index(50000, 302650, 1125, 3), 9, 1000);
   }

   void bigGraphTest(weighted_graph wg, int tt, int jump) {
//      test taken from "Graph_EX0_Test2"
      weighted_graph_algorithms wga = new WGraph_Algo();
      wga.init(wg);
      int[] nodes = nodes(wg);
      int i = 0;
      boolean b = true;
      while (i < tt) {
         b &= wga.isConnected();
         for (int x = 0; x < jump; x++) {
            int s = wg.nodeSize();
            int r = nextRnd(0, s);
            int key = nodes[r];
            wg.removeNode(key);
         }
         i++;
      }
      assertTrue(b);
      b = wga.isConnected();
      //  System.out.println(g);
      assertFalse(b);
   }

   @Test
   void isConnected1() {
      weighted_graph wg = graph_creator_with_start_index(6, 0, 0, 1);
      weighted_graph_algorithms wga = new WGraph_Algo(wg);
      wg.connect(0, 1, 1.5);
      wg.connect(0, 2, 5.5);
      wg.connect(0, 3, 2.5);
      wg.connect(0, 4, 1.5);
      wg.connect(0, 5, 0.5);
      assertTrue(wga.isConnected());
      wg.connect(1, 2, 10.4);
      wg.connect(1, 3, 4);
      wg.connect(1, 4, 3);
      wg.connect(1, 5, 2);
      wg.connect(3, 4, 2.8);
      assertTrue(wga.isConnected());
      wg.removeNode(2);
      assertTrue(wga.isConnected());
      wg.removeNode(1);
      assertTrue(wga.isConnected());
      wg.removeNode(0);
      assertFalse(wga.isConnected());
      wg.connect(3, 5, 3.5);
      assertTrue(wga.isConnected());
   }

   @Test
   void isConnected2() {
      weighted_graph wg = new WGraph_DS();
      weighted_graph_algorithms wga = new WGraph_Algo(wg);
      assertTrue(wga.isConnected());
      wg.addNode(0);
      assertTrue(wga.isConnected());
      wg.addNode(1);
      assertFalse(wga.isConnected());
      wg.connect(0, 1, 3.5);
      assertTrue(wga.isConnected());
      wg.addNode(2);
      assertFalse(wga.isConnected());
      wg.connect(2, 1, 3.5);
      assertTrue(wga.isConnected());
      wg.removeNode(1);
      assertFalse(wga.isConnected());
   }

   @Test
   void shortestPathDist() {
      weighted_graph wg = graph_creator_with_start_index(5, 0, 0, 1);
      weighted_graph_algorithms wga = new WGraph_Algo(wg);
      wg.connect(0, 1, 1);
      wg.connect(0, 2, 10);
      wg.connect(1, 3, 4.5);
      wg.connect(1, 4, 1.5);
      wg.connect(4, 3, 2.5);
      wg.connect(2, 3, 3.5);
      assertEquals(8.5, wga.shortestPathDist(0, 2));
      List<node_info> l = wga.shortestPath(0, 2);
      assertEquals(5, l.size());
      wg.connect(1, 3, 2.5);
      assertEquals(7, wga.shortestPathDist(0, 2));
      l = wga.shortestPath(0, 2);
      assertEquals(4, l.size());
      wg.connect(2, 0, 6.9);
      assertEquals(6.9, wga.shortestPathDist(0, 2));
      l = wga.shortestPath(0, 2);
      assertEquals(2, l.size());
   }

   @Test
   void shortestPath() {
      weighted_graph wg = graph_creator_with_start_index(5, 0, 0, 1);
      weighted_graph_algorithms wga = new WGraph_Algo(wg);
      wg.connect(0, 1, 0);
      wg.connect(1, 2, 0);
      wg.connect(2, 3, 0);
      wg.connect(3, 4, 0);
      wg.connect(0, 4, 1);
      wg.connect(0, 3, 1);
      assertEquals(0, wga.shortestPathDist(0, 4));
      List<node_info> l = wga.shortestPath(0, 4);
      for (int i = 0; i < l.size(); i++) {
         assertEquals(i, l.get(i).getKey());
      }
      wg.connect(0, 1, 3);
      assertEquals(1, wga.shortestPathDist(0, 2));
      l = wga.shortestPath(0, 2);
      assertEquals(3, l.size());
      assertEquals(0, l.get(0).getKey());
      assertEquals(3, l.get(1).getKey());
      assertEquals(2, l.get(2).getKey());
      l = wga.shortestPath(0, 5);
      assertNull(l);
   }

   @Test
   void save() {
      weighted_graph wg = graph_creator_with_start_index(50, 100, 0, 2);
      weighted_graph_algorithms wga = new WGraph_Algo(wg);
      assertTrue(wga.save("./wg.txt"));
      wga.init(new WGraph_DS());
      assertNotEquals(wg, wga.getGraph());
      assertTrue(wga.load("./wg.txt"));
      assertEquals(wg, wga.getGraph());
   }

   public static weighted_graph graph_creator_with_start_index(int v_size, int e_size, int startIndex, int seed) {
      weighted_graph wg = new WGraph_DS();
      _rnd = new Random(seed);
      for (int i = 0; i < v_size; i++) {
         wg.addNode(i + startIndex);
      }
      // Iterator<node_data> itr = V.iterator(); // Iterator is a more elegant and generic way, but KIS is more important
      int[] nodes = nodes(wg);
      while (wg.edgeSize() < e_size) {
         int a = nextRnd(0, v_size);
         int b = nextRnd(0, v_size);
         int i = nodes[a];
         int j = nodes[b];
         wg.connect(i, j, 1);
      }
      return wg;
   }

   private static int[] nodes(weighted_graph g) {
      int size = g.nodeSize();
      Collection<node_info> V = g.getV();
      node_info[] nodes = new node_info[size];
      V.toArray(nodes); // O(n) operation
      int[] ans = new int[size];
      for (int i = 0; i < size; i++) {
         ans[i] = nodes[i].getKey();
      }
      Arrays.sort(ans);
      return ans;
   }

   public static int nextRnd(int min, int max) {
      double v = nextRnd(0.0 + min, (double) max);
      int ans = (int) v;
      return ans;
   }

   public static double nextRnd(double min, double max) {
      double d = _rnd.nextDouble();
      double dx = max - min;
      double ans = d * dx + min;
      return ans;
   }
}