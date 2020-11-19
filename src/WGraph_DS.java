package ex1.src;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Objects;

public class WGraph_DS implements weighted_graph {

   private final HashMap<Integer, node_info> _graphNodes;
   private final HashMap<Integer, HashMap<Integer, Double>> _graphEdges;
   private int edgeSize;
   private int modeCount = 0;

   public WGraph_DS() {
      _graphNodes = new HashMap<>();
      _graphEdges = new HashMap<>();
      edgeSize = 0;
   }

   public WGraph_DS(weighted_graph wg) {
//      copy constructor
      this();
//      deep copy for every node
      for (node_info node : wg.getV()) {
         addNode(node.getKey());
      }
//      connect every node to his neighbors
      for (node_info node : getV()) {
         for (node_info ni : wg.getV(node.getKey())) {
            connect(node.getKey(), ni.getKey(), wg.getEdge(node.getKey(), ni.getKey()));
         }
      }
   }

   @Override
   public node_info getNode(int key) {
      return _graphNodes.get(key);
   }

   @Override
   public boolean hasEdge(int node1, int node2) {
      if (getNode(node1) == null) return false;
      return _graphEdges.get(node1).get(node2) != null;
   }

   @Override
   public double getEdge(int node1, int node2) {
      if (!hasEdge(node1, node2)) return -1;
      return _graphEdges.get(node1).get(node2);
   }

   @Override
   public void addNode(int key) {
//      if the node exist don't do nothing
      if (getNode(key) == null) {
         _graphNodes.put(key, new NodeInfo(key));
         _graphEdges.put(key, new HashMap<>());
         modeCount++;
      }
   }

   @Override
   public void connect(int node1, int node2, double w) {
      if (w >= 0 && node1 != node2 && getNode(node1) != null && getNode(node2) != null) {
         if (!hasEdge(node1, node2)) {
//            increase edge size just if there wasn't edge
            edgeSize++;
         }
         _graphEdges.get(node1).put(node2, w);
         _graphEdges.get(node2).put(node1, w);
         modeCount++;
      }
   }

   @Override
   public Collection<node_info> getV() {
      return _graphNodes.values();
   }

   @Override
   public Collection<node_info> getV(int node_id) {
      Collection<node_info> ni = new ArrayList<>();
      if (_graphNodes.get(node_id) != null) {
         for (Integer i : _graphEdges.get(node_id).keySet()) {
            ni.add(_graphNodes.get(i));
         }
      }
      return ni;
   }

   @Override
   public node_info removeNode(int key) {
      node_info node = getNode(key);
      if (node != null) {
//         if the node exist in the graph and removing all his edges
         ArrayList<node_info> nis = new ArrayList<>(getV(key));
         for (node_info ni : nis) {
            removeEdge(key, ni.getKey());
         }
//         after he isn't connect to other nodes removing him from the graph
         _graphEdges.remove(key);
         _graphNodes.remove(key);
         modeCount++;
      }
      return node;
   }

   @Override
   public void removeEdge(int node1, int node2) {
      if (hasEdge(node1, node2)) {
//         removing n2 from n1's neighbors
//         and n1 from n2's neighbors
         _graphEdges.get(node1).remove(node2);
         _graphEdges.get(node2).remove(node1);
         edgeSize--;
         modeCount++;
      }
   }

   @Override
   public int nodeSize() {
      return _graphNodes.size();
   }

   @Override
   public int edgeSize() {
      return edgeSize;
   }

   @Override
   public int getMC() {
      return modeCount;
   }

   public enum Info {
      VISITED,
      UNVISITED
   }

   @Override
   public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      WGraph_DS wGraph_ds = (WGraph_DS) o;
      return edgeSize == wGraph_ds.edgeSize &&
              Objects.equals(_graphNodes, wGraph_ds._graphNodes) &&
              Objects.equals(_graphEdges, wGraph_ds._graphEdges);
   }

   @Override
   public int hashCode() {
      return Objects.hash(_graphNodes, _graphEdges, edgeSize);
   }

   @Override
   public String toString() {
      return "WGraph_DS{" +
              "_graphNodes=" + _graphNodes +
              ", _graphEdges=" + _graphEdges +
              '}';
   }

   private class NodeInfo implements node_info, Comparable<node_info> {

      final int _id;
      double _tag;
      String _info;

      NodeInfo(int id) {
         _id = id;

//      default values
         _tag = -1;
         _info = Info.UNVISITED.name();
      }

      public NodeInfo(node_info n) {
//      copy constructor
         _id = n.getKey();
         _tag = n.getTag();
         _info = n.getInfo();
      }

      @Override
      public int getKey() {
         return _id;
      }

      @Override
      public String getInfo() {
         return _info;
      }

      @Override
      public void setInfo(String s) {
         _info = s;
      }

      @Override
      public double getTag() {
         return _tag;
      }

      @Override
      public void setTag(double t) {
         _tag = t;
      }

      @Override
      public int compareTo(node_info o) {
         return Double.compare(getTag(), o.getTag());
      }

      @Override
      public boolean equals(Object o) {
         if (this == o) return true;
         if (o == null || getClass() != o.getClass()) return false;
         NodeInfo nodeInfo = (NodeInfo) o;
         return _id == nodeInfo._id;
      }

      @Override
      public int hashCode() {
         return Objects.hash(_id);
      }

      @Override
      public String toString() {
         return "{" +
                 "id=" + _id +
                 '}';
      }
   }
}
