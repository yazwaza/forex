// requires khoury.jar
import khoury.CapturedResult
import khoury.EnabledTest
import khoury.captureResults
import khoury.input
import khoury.isAnInteger
import khoury.runEnabledTests
import khoury.testSame

data class ForexGraph(
    // creates a representation of a graph where nodes are represented as Strings for currency and Double for the exchange rate
    val graph: MutableMap<String, MutableList<Pair<String, Double>>> = mutableMapOf(),
) {
    // checks whether the original node exists, if not will create a new empty graph.
    // then with the either the new graph or existing graph, the function is used to add another node in order to create an edge.
    fun addEdge(
        nodeA: String,
        nodeB: String,
        edge: Double,
    ): MutableMap<String, MutableList<Pair<String, Double>>> {
        if (!graph.containsKey(nodeA)) {
            graph[nodeA] = mutableListOf()
        }
        graph[nodeA]?.add(Pair(nodeB, edge))
        return graph
    }

    // retrives all the neighbors of a currency, if there are not neighbors it returns an empty list
    fun getNeighbors(currency: String): List<Pair<String, Double>> = graph[currency] ?: emptyList()
}

// testing addEdge function
@EnabledTest
fun testAddEdge() {
    val forexGraph = ForexGraph()
    val result =
        mutableMapOf(
            "USD" to mutableListOf(Pair("EUR", 0.95)),
        )

    testSame(
        forexGraph.addEdge("USD", "EUR", 0.95),
        result,
    )
}

// testing getNeighbors function
@EnabledTest
fun testGetNeighbors() {
    val forexGraph = ForexGraph()
    forexGraph.addEdge("USD", "EUR", 0.95)
    forexGraph.addEdge("USD", "JPY", 151.8)
    val result = listOf(Pair("EUR", 0.95), Pair("JPY", 151.8))
    testSame(
        forexGraph.getNeighbors("USD"),
        result,
    )
    testSame(
        forexGraph.getNeighbors("GBP"),
        emptyList<Pair<String, Double>>(),
        "no neighbors",
    )
}

/*
the function creates a graph where:
USD has 2 edges connecting to EUR and JPY
EUR has 2 edges connecting to USD and GBP
JPY has 1 edge connecting to USD
GBP has 1 edge connecting to EUR
*/
fun createForexGraph(): ForexGraph {
    val forexGraph = ForexGraph()
    forexGraph.addEdge("USD", "EUR", 0.95) // 1 USD = 0.95 EUR
    forexGraph.addEdge("EUR", "USD", 1.06) // 1 EUR = 1.06 USD
    forexGraph.addEdge("USD", "JPY", 151.8) // 1 USD = 151.8 JPY
    forexGraph.addEdge("JPY", "USD", 0.0066) // 1 JPY = 0.0066 USD
    forexGraph.addEdge("EUR", "GBP", 0.83) // 1 EUR = 0.83 GBP
    forexGraph.addEdge("GBP", "EUR", 1.2) // 1 GBP = 1.2 EUR
    return forexGraph
}

// breadthFirst method finding one node from another node
fun breadthFirstSearch(
    graph: ForexGraph,
    startNode: String,
    endNode: String,
): Pair<List<String>, Double>? {
    val visitedNodes = mutableSetOf<String>()
    val path = mutableListOf<Pair<List<String>, Double>>()
    path.add(Pair(listOf(startNode), 1.0)) // adds the start node to the path

    while (path.isNotEmpty()) { // as long as there elements in the path
        val (currentPath, currentRate) = path.removeAt(0) // removes and retrieves the first rate and node
        val currentNode = currentPath.last()

        if (currentNode == endNode) { // if end node is found return the path
            return Pair(currentPath, currentRate)
        }

        if (currentNode !in visitedNodes) { // if current node is not visited mark as visited by adding into visitedNodes
            visitedNodes.add(currentNode)

            for (neighbor in graph.getNeighbors(currentNode)) { // checks all the neighors of the node and adds paths to the queue
                path.add(Pair(currentPath + neighbor.first, currentRate * neighbor.second)) // Add new path to the queue
            }
        }
    }

    return null
}

// test breadth first search
@EnabledTest
fun testBreadFirstSearch() {
    val forexGraph = createForexGraph()
    val result = Pair(listOf("USD", "EUR", "GBP"), 0.7885)
    testSame(
        breadthFirstSearch(forexGraph, "USD", "GBP"),
        result,
    )
}

fun depthFirstSearch(
    graph: ForexGraph,
    startNode: String,
    endNode: String,
    visited: MutableSet<String> = mutableSetOf(),
    path: MutableList<String> = mutableListOf(),
    currentRate: Double = 1.0,
): Pair<List<String>, Double>? {
    visited.add(startNode) // adds start node as visited and to the path
    path.add(startNode)

    if (startNode == endNode) { // returns empty path if start and end are the same
        return Pair(path.toList(), currentRate)
    }

    for (neighbor in graph.getNeighbors(startNode)) { // checks all neighbors in start node
        if (neighbor.first !in visited) { // if a neighbor is not visited will do recursion with the neighbor
            val result =
                depthFirstSearch(
                    graph,
                    neighbor.first,
                    endNode,
                    visited,
                    path,
                    currentRate * neighbor.second,
                )
            if (result != null) return result
        }
    }

    path.removeAt(path.size - 1) // Backtrack
    return null
}

// test depthfirstSearch
@EnabledTest
fun testDepthFirstSearch() {
    val forexGraph = createForexGraph()
    val result = Pair(listOf("USD", "EUR", "GBP"), 0.7885)
    testSame(
        depthFirstSearch(forexGraph, "USD", "GBP"),
        result,
    )
}

var forexGraph = createForexGraph()

//arbitrage strategy
fun detectArbitrage(forexGraph: ForexGraph): List<String>? {
    
}

// // the following functions make the functions above interactive
// fun interactiveAddEdge() {
//     println("Enter the edge in the format: FromNode|ToNode|Rate")
//     val input = input().split("|")
//     if (input.size == 3) {
//         val rate = input[2].toDoubleOrNull()
//         if (rate != null) {
//             forexGraph.addEdge(input[0], input[1], rate)
//             println("Edge added.")
//         } else {
//             println("Error invalid input, try again.")
//         }
//     } else {
//         println("Error invalid input, try again.")
//     }
// }

// @EnabledTest
// fun testInteractiveAddEdge() {
//     testSame(
//         captureResults(
//             ::interactiveAddEdge,
//             "USD|SGD|1.34",
//         ),
//         CapturedResult(
//             Unit,
//             "Enter the edge in the format: FromNode|ToNode|Rate",
//             "Edge added.",
//         ),
//     )
//     testSame(
//         captureResults(
//             ::interactiveAddEdge,
//             "USDSGD1.34",
//         ),
//         CapturedResult(
//             Unit,
//             "Enter the edge in the format: FromNode|ToNode|Rate",
//             "Error invalid input, try again.",
//         ),
//         "error",
//     )
// }

// fun interactiveViewNeighbors() {
//     println("Enter the node to view neighbors:")
//     val node = input()
//     val neighbors = forexGraph.getNeighbors(node)
//     if (neighbors.isNotEmpty()) {
//         println("Neighbors of $node: ${neighbors.joinToString { "(${it.first}, ${it.second})" }}")
//     } else {
//         println("No neighbors found for $node.")
//     }
// }

// @EnabledTest
// fun testInteractiveViewNeighbors() {
//     val cout = forexGraph.getNeighbors("USD")
//     testSame(
//         captureResults(
//             ::interactiveViewNeighbors,
//             "USD",
//         ),
//         CapturedResult(
//             Unit,
//             "Enter the node to view neighbors:",
//             "Neighbors of USD: ${cout.joinToString()}",
//         ),
//     )
//     testSame(
//         captureResults(
//             ::interactiveViewNeighbors,
//             "SGD",
//         ),
//         CapturedResult(
//             Unit,
//             "Enter the node to view neighbors:",
//             "No neighbors found for SGD.",
//         ),
//         "error",
//     )
// }

// fun interactiveBreadthFirstSearch() {
//     println("Enter the start and end nodes for Breadth-First Search (format: FromNode|ToNode):")
//     val input = input().split("|")
//     if (input.size == 2) {
//         val result = depthFirstSearch(forexGraph, input[0], input[1])
//         if (result != null) {
//             println("BFS Result: Path = ${result.first}, Rate = ${"%.4f".format(result.second)}")
//         } else {
//             println("No path found between ${input[0]} and ${input[1]}.")
//         }
//     } else {
//         println("Error invalid input, try again.")
//     }
// }

// @EnabledTest
// fun testInteractiveBreadthFirstSearch() {
//     val cout = breadthFirstSearch(forexGraph, "USD", "GBP")
//     testSame(
//         captureResults(
//             ::interactiveBreadthFirstSearch,
//             "USD|GBP",
//         ),
//         CapturedResult(
//             Unit,
//             "Enter the start and end nodes for Breadth-First Search (format: FromNode|ToNode):",
//             "BFS Result: Path = ${cout?.first}, Rate = ${"%.4f".format(cout?.second)}",
//         ),
//     )
//     testSame(
//         captureResults(
//             ::interactiveBreadthFirstSearch,
//             "SGD|GBP",
//         ),
//         CapturedResult(
//             Unit,
//             "Enter the start and end nodes for Breadth-First Search (format: FromNode|ToNode):",
//             "No path found between SGD and GBP.",
//         ),
//         "error no path",
//     )
//     testSame(
//         captureResults(
//             ::interactiveBreadthFirstSearch,
//             "SGDGBP",
//         ),
//         CapturedResult(
//             Unit,
//             "Enter the start and end nodes for Breadth-First Search (format: FromNode|ToNode):",
//             "Error invalid input, try again.",
//         ),
//         "error",
//     )
// }

// fun interactiveDepthFirstSearch() {
//     println("Enter the start and end nodes for Depth-First Search (format: FromNode|ToNode):")
//     val input = input().split("|")
//     if (input.size == 2) {
//         val result = depthFirstSearch(forexGraph, input[0], input[1])
//         if (result != null) {
//             println("DFS Result: Path = ${result.first}, Rate = ${"%.4f".format(result.second)}")
//         } else {
//             println("No path found between ${input[0]} and ${input[1]}.")
//         }
//     } else {
//         println("Error invalid input, try again.")
//     }
// }

// @EnabledTest
// fun testInteractiveDepthFirstSearch() {
//     val cout = depthFirstSearch(forexGraph, "USD", "GBP")
//     testSame(
//         captureResults(
//             ::interactiveDepthFirstSearch,
//             "USD|GBP",
//         ),
//         CapturedResult(
//             Unit,
//             "Enter the start and end nodes for Depth-First Search (format: FromNode|ToNode):",
//             "DFS Result: Path = ${cout?.first}, Rate = ${"%.4f".format(cout?.second)}",
//         ),
//     )
//     testSame(
//         captureResults(
//             ::interactiveDepthFirstSearch,
//             "SGD|GBP",
//         ),
//         CapturedResult(
//             Unit,
//             "Enter the start and end nodes for Depth-First Search (format: FromNode|ToNode):",
//             "No path found between SGD and GBP.",
//         ),
//         "error no path",
//     )
//     testSame(
//         captureResults(
//             ::interactiveDepthFirstSearch,
//             "SGDGBP",
//         ),
//         CapturedResult(
//             Unit,
//             "Enter the start and end nodes for Depth-First Search (format: FromNode|ToNode):",
//             "Error invalid input, try again.",
//         ),
//         "error",
//     )
// }

// fun chooseFunction() {
//     // Define functions for the menu
//     val functionList =
//         listOf(
//             Pair("Add an edge", ::interactiveAddEdge),
//             Pair("View neighbors", ::interactiveViewNeighbors),
//             Pair("Perform Breadth-First Search", ::interactiveBreadthFirstSearch),
//             Pair("Perform Depth-First Search", ::interactiveDepthFirstSearch),
//         )

//     while (true) {
//         println("Choose an option:")
//         println("Enter 0 to quit")
//         // Display the menu
//         functionList.forEachIndexed { index, (description, _) ->
//             println("${index + 1}: $description")
//         }

//         val userInput = input()

//         if (isAnInteger(userInput)) {
//             val selectedIndex = userInput.toInt() - 1
//             when {
//                 selectedIndex == -1 -> return // Exit the loop
//                 selectedIndex in functionList.indices -> {
//                     // Execute the selected function
//                     functionList[selectedIndex].second()
//                 }
//                 else -> println("That is not on the menu.")
//             }
//         } else {
//             println("Error, enter an integer.")
//         }
//     }
// }

// runEnabledTests(this)

// fun main() {
//     chooseFunction()
// }

// main()
