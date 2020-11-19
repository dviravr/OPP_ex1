package ex1.src;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class WGraph_Algo implements weighted_graph_algorithms {

   private weighted_graph _wg;
   private final HashMap<Integer, Integer> _path;
   private int _visitedCounter;

   public WGraph_Algo() {
      _wg = new WGraph_DS();
      _path = new HashMap<>();
      _visitedCounter = 0;
   }

   public WGraph_Algo(weighted_graph wg) {
      _wg = wg;
      _path = new HashMap<>();
      _visitedCounter = 0;
   }

   @Override
   public void init(weighted_graph g) {
      _wg = g;
   }

   @Override
   public weighted_graph getGraph() {
      return _wg;
   }

   @Override
   public weighted_graph copy() {
//      deep copy of a graph
      return new WGraph_DS(_wg);
   }

   @Override
   public boolean isConnected() {
      resetValues();
//      if the graph is empty or have just 1 node he is connected
      if (_wg.nodeSize() > 1) {
         /*
          making the connection path from one node
          if the path is on all nodes return true
          */
         dijkstra(_wg.getV().iterator().next());
         return _visitedCounter == _wg.nodeSize();
      }
      return true;
   }

   @Override
   public double shortestPathDist(int src, int dest) {
      resetValues();
//      checking both nodes are in the graph if not return -1
      if (_wg.getNode(src) != null && _wg.getNode(dest) != null) {
         /*
          making the connection path from src
          if src and dest are connected the distance is the tag on the dest
          if they arent connected the tag on dest is -1 (default value)
          */
         dijkstra(_wg.getNode(src));
         return _wg.getNode(dest).getTag();
      }
      return -1;
   }

   @Override
   public List<node_info> shortestPath(int src, int dest) {
      resetValues();
      node_info srcN = _wg.getNode(src);
      node_info destN = _wg.getNode(dest);
      List<node_info> path;
//      checking both nodes are in the graph if not return null
      if (srcN == null || destN == null) {
         path = null;
      } else {
//         making the connection path from src and call reconstructPath
         dijkstra(srcN);
         path = reconstructPath(src, dest);
      }
      return path;
   }

   private void dijkstra(node_info src) {
      PriorityQueue<node_info> pq = new PriorityQueue<>();
      pq.add(src);

//      set src to visited and at distance 0 from himself
      src.setTag(0);

//      looping while there is nodes in the queue
      while (!pq.isEmpty()) {
         node_info node = pq.remove();
         if (!node.getInfo().equals(WGraph_DS.Info.VISITED.name())) {
            node.setInfo(WGraph_DS.Info.VISITED.name());
            _visitedCounter++;
         }

//         looping on the node's (first node in the queue) neighbors
         for (node_info ni : _wg.getV(node.getKey())) {
            if (!ni.getInfo().equals(WGraph_DS.Info.VISITED.name())) {
               double dist = node.getTag()
                       + _wg.getEdge(node.getKey(), ni.getKey());
               if (ni.getTag() == -1 || dist < ni.getTag()) {
                  ni.setTag(dist);
                  pq.add(ni);
                  _path.put(ni.getKey(), node.getKey());
               }
            }
         }
      }
   }

   private List<node_info> reconstructPath(int src, int dest) {
//      checking that the dest is connected to src
      if (_path.get(dest) == null) return null;
//      setting i to the key of the parent of dest
      int i = _path.get(dest);
      List<node_info> shortestPath = new ArrayList<>();
//      adding dest to the shortest path
      shortestPath.add(_wg.getNode(dest));

//      looping while the parent isn't the src
      while (i != src) {
//         adding the parent to the head of the shortest path
         shortestPath.add(0, _wg.getNode(i));
//         setting i to the parent of parent
         i = _path.get(i);
      }
//      adding src to the head of the shortest path
      shortestPath.add(0, _wg.getNode(src));

      return shortestPath;
   }

   @Override
   public boolean save(String file) {
      if (_wg == null) {
         System.out.println("the graph is null, couldn't save the graph");
         return false;
      }
      try {
         FileWriter wg = new FileWriter(file);

         wg.write("nodes\n");
//         save all node's ids
         for (node_info n : _wg.getV()) {
            wg.write(n.getKey() + "," + n.getTag() + "," + n.getInfo() + "\n");
         }
         wg.write("edges\n");
//         save all edges
         for (node_info n : _wg.getV()) {
            for (node_info ni : _wg.getV(n.getKey())) {
               int key1 = n.getKey();
               int key2 = ni.getKey();
               wg.write(key1 + "," + key2 + "," + _wg.getEdge(key1, key2) + "\n");
            }
         }
         wg.close();
         System.out.println("Successfully wrote to the file.");
         return true;
      } catch (IOException e) {
         System.out.println("An error occurred.");
         return false;
      }
   }

   @Override
   public boolean load(String file) {
      try {
         weighted_graph wg = new WGraph_DS();
         File wgFile = new File(file);
         Scanner wgReader = new Scanner(wgFile);
         if (!wgReader.hasNext()) {
            System.out.println("can't read empty file");
            return false;
         }
         String data = wgReader.nextLine();
//         read all node's ids
         while (wgReader.hasNextLine() && !data.equals("edges")) {
            if (!data.equals("nodes")) {
               String[] a = data.split(",");
               int key = Integer.parseInt(a[0]);
               double tag = Double.parseDouble(a[1]);
               String info = a[1];
               wg.addNode(key);
               wg.getNode(key).setInfo(info);
               wg.getNode(key).setTag(tag);
            }
            data = wgReader.nextLine();
         }

//         read all edges
         while (wgReader.hasNextLine()) {
            data = wgReader.nextLine();
            if (!data.equals("edges")) {
               String[] a = data.split(",");
               int k1 = Integer.parseInt(a[0]);
               int k2 = Integer.parseInt(a[1]);
               double w = Double.parseDouble(a[2]);
               wg.connect(k1, k2, w);
            }
         }
         wgReader.close();
         System.out.println("Successfully load from file.");
         _wg = wg;
         return true;
      } catch (IOException e) {
         System.out.println("An error occurred.");
         return false;
      }
   }

   private void resetValues() {
//      reset all values to default values
      for (node_info node : _wg.getV()) {
         node.setTag(-1);
         node.setInfo(WGraph_DS.Info.UNVISITED.name());
      }
      _visitedCounter = 0;
      _path.clear();
   }
}
