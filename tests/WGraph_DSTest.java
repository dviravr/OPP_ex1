package ex1.tests;

import ex1.src.WGraph_DS;
import ex1.src.node_info;
import ex1.src.weighted_graph;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class WGraph_DSTest {
   private static Random _rnd = null;
   private weighted_graph wg;

   public static weighted_graph graph_creator(int v_size, int e_size, int seed) {
      weighted_graph wg = new WGraph_DS();
      _rnd = new Random(seed);
      for (int i = 0; i < v_size; i++) {
         wg.addNode(i);
      }
      // Iterator<node_data> itr = V.iterator(); // Iterator is a more elegant and generic way, but KIS is more important
      int[] nodes = nodes(wg);
      while (wg.edgeSize() < e_size) {
         int a = nextRnd(0, v_size);
         int b = nextRnd(0, v_size);
         int i = nodes[a];
         int j = nodes[b];
         wg.connect(i, j, _rnd.nextDouble());
      }
      return wg;
   }

   @BeforeEach
   void generateGraph() {
      wg = graph_creator(10, 0,1);
   }

   @Test
   void getNode() {
      assertEquals(wg.getNode(1).getKey(), 1);
   }

   @Test
   void hasEdge() {
      wg.connect(0, 1, 1);
      wg.connect(1, 1, 1);
      wg.connect(2, 1, 1);
      wg.connect(0, 2, 1);
      wg.connect(31,62,4);
      assertTrue(wg.hasEdge(0, 1));
      assertTrue(wg.hasEdge(2, 1));
      assertTrue(wg.hasEdge(0, 2));
      assertTrue(wg.hasEdge(1, 0));
      assertTrue(wg.hasEdge(1, 2));
      assertFalse(wg.hasEdge(1, 1));
      assertFalse(wg.hasEdge(31, 62));
      assertFalse(wg.hasEdge(1, 3));
      assertFalse(wg.hasEdge(5, 3));
   }

   @Test
   void getEdge() {
      wg.connect(0, 1, 1);
      wg.connect(1, 1, 2);
      wg.connect(2, 1, 4);
      wg.connect(0, 2, 3);
      assertEquals(wg.getEdge(0, 1), 1);
      assertEquals(wg.getEdge(1, 0), wg.getEdge(0, 1));
      assertEquals(wg.getEdge(2, 1), 4);
      assertEquals(wg.getEdge(0, 2), 3);
      assertEquals(wg.getEdge(0, 2), wg.getEdge(2, 0));
      assertEquals(wg.getEdge(1, 2), 4);
      assertEquals(wg.getEdge(1, 1), -1);
      assertEquals(wg.getEdge(1, 5), -1);
      assertEquals(wg.getEdge(4, 5), -1);
   }

   @Test
   void addNode() {
      assertEquals(wg.nodeSize(), 10);
      wg = graph_creator(100, 50, 1);
      assertEquals(wg.nodeSize(), 100);
      assertEquals(wg.edgeSize(), 50);
   }

   @Test
   void connect() {
      assertEquals(wg.edgeSize(), 0);
      wg.connect(0, 0, 3.5);
      assertEquals(wg.edgeSize(), 0);
      wg.connect(0, 1, 2.5);
      assertEquals(wg.edgeSize(), 1);
      wg.connect(0, 2, 0.5);
      assertEquals(wg.edgeSize(), 2);
      wg.connect(2, 0, 3.5);
      assertEquals(wg.edgeSize(), 2);
   }

   @Test
   void getV() {
      wg.connect(0,1,2.5);
      wg.connect(0,2,2.5);
      wg.connect(0,3,2.5);
      wg.connect(0,6,2.5);
      wg.connect(3,1,2.5);
      assertEquals(wg.getV(0).size(), 4);
      assertEquals(wg.getV(1).size(), 2);
      wg.removeEdge(6, 0);
      assertEquals(wg.getV(0).size(), 3);
      wg.removeNode(3);
      assertEquals(wg.getV(0).size(), 2);
      wg.removeNode(0);
      assertEquals(wg.getV(0).size(), 0);
   }

   @Test
   void removeNode() {
      wg.removeNode(9);
      assertNull(wg.getNode(9));
      assertNull(wg.getNode(10));
      wg.connect(0, 1, 1);
      wg.connect(0, 2, 1);
      wg.connect(0, 3, 1);
      assertEquals(wg.edgeSize(), 3);
      wg.removeNode(0);
      assertFalse(wg.hasEdge(0, 1));
      assertFalse(wg.hasEdge(0, 2));
      assertEquals(wg.edgeSize(), 0);
   }

   @Test
   void removeEdge() {
      wg.connect(0, 0, 3.5);
      wg.connect(0, 1, 2.5);
      wg.connect(0, 2, 0.5);
      wg.connect(2, 0, 3.5);
      assertEquals(wg.edgeSize(), 2);
      wg.removeEdge(0, 0);
      assertEquals(wg.edgeSize(), 2);
      wg.removeEdge(0, 1);
      assertEquals(wg.edgeSize(), 1);
      wg.removeEdge(2, 0);
      assertEquals(wg.edgeSize(), 0);
      wg.removeEdge(2, 0);
      assertEquals(wg.edgeSize(), 0);
      wg.connect(0, 1, 2.5);
      wg.removeEdge(1, 0);
      assertEquals(wg.edgeSize(), 0);
   }

   @Test
   void getMC() {
      System.out.println(wg.getMC());
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

   private static int nextRnd(int min, int max) {
      double v = nextRnd(0.0 + min, (double) max);
      int ans = (int) v;
      return ans;
   }

   private static double nextRnd(double min, double max) {
      double d = _rnd.nextDouble();
      double dx = max - min;
      double ans = d * dx + min;
      return ans;
   }
}