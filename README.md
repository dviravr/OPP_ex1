# Weighted Graph

An implementation of weighted graph in java

### Weighted Graph class

* NodeInfo is inner class inside WGraph_DS,
 the node have a key, a tag and an info
 the nodes have a default values for the thg and the info of -1 and UNVISITED
 for use in the Algo class.

* WGraph_DS holds two hashmaps.
the first is for the nodes - the key is the key of the node, 
and the value is the node himself.
the second is for the edges - the key is a node key,
and the value is a hashmap of all his neighbors.

* `addNode()` add a new node to the graph with a given key
`getNode()` return the node with given key

* `getEdge()` return the weight of an edge between two givens nodes.
the method uses `hasEdge()` to check if there is an edge between them.

* `connect()` make a new edge between two nodes with a given weight.
if there is an edge already the method simply update the weight.

* `removeEdge()` simply delete the edge between two givens nodes.
`removeNode()` delete the node from the graph and delete all the edges that connected to him 
by using `removeEdge()`.

* `getV()` the method simply return a collection of all the nodes in the graph.
`getV(key)` return a collection of all the neighbors of a specific node.

* `nodeSize()` return the number of nodes in the graph

* `edgeSize()` return the number of the edges in the graph

### Weighted Graph Algorithm class

* WGraph_Algo holds a graph, all the algorithms works on the graph.

* there are two constructors, one without a given graph and one with a given graph.

* the method `init()` change the class's graph to a given graph.
`getGraph()` return the class's graph.

* `copy()` make a deep copy of the class's graph and return him.

* `isConnected()` return true if the graph is connected and false if not.

* the method `shortestPathDist()` return the shortest path distance between two givens nodes.
 the method `shortestPath()` return a list of the shortest path between two givens nodes.
 
* the method `save()` simply save the class's graph to given file.
 the method `load()` simply load a graph to the class's graph from a given file.
  

