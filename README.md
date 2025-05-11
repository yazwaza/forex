# Forex Exchange Graph

A Kotlin implementation of a foreign exchange rate graph with various search algorithms.

## Overview

This project models currency exchange rates as a directed graph where:
- Nodes represent different currencies (USD, EUR, JPY, GBP, etc.)
- Edges represent exchange rates between currencies
- The weight of each edge is the conversion rate from one currency to another

## Features

- **Graph Implementation**: Custom `ForexGraph` class to represent currency exchange rates
- **Search Algorithms**:
  - Breadth-First Search (BFS) to find conversion paths between currencies
  - Depth-First Search (DFS) to find conversion paths between currencies
  - Arbitrage detection (work in progress)
- **Interactive Console Interface** (currently commented out)

## How It Works

### ForexGraph Class

The core data structure is a `ForexGraph` that stores currency exchange rates as a directed graph:

```kotlin
data class ForexGraph(
    val graph: MutableMap<String, MutableList<Pair<String, Double>>> = mutableMapOf()
)
```

### Key Functions

- `addEdge(nodeA, nodeB, edge)`: Add an exchange rate between two currencies
- `getNeighbors(currency)`: Get all currency conversions available from a specific currency
- `breadthFirstSearch(graph, startNode, endNode)`: Find a currency conversion path using BFS
- `depthFirstSearch(graph, startNode, endNode)`: Find a currency conversion path using DFS
- `detectArbitrage(forexGraph)`: Detect arbitrage opportunities (not yet implemented)

### Example Graph

The default graph created by `createForexGraph()` contains these exchange rates:
- 1 USD = 0.95 EUR
- 1 EUR = 1.06 USD
- 1 USD = 151.8 JPY
- 1 JPY = 0.0066 USD
- 1 EUR = 0.83 GBP
- 1 GBP = 1.2 EUR

## Requirements

- Kotlin
- khoury.jar (for testing functionality)

## Getting Started

1. Ensure you have the khoury.jar library in your classpath
2. Run the forex.kts file using a Kotlin script runner
3. Uncomment the interactive interface functions to use the command-line interface

## Usage

When the interactive interface is enabled, you can:
1. Add new currency exchange rates
2. View all available conversions for a particular currency
3. Find the best conversion path between two currencies using BFS
4. Find a conversion path between two currencies using DFS

## Future Development

- Complete the `detectArbitrage()` function to identify profitable currency conversion cycles
- Add visualization of the currency graph
- Implement more sophisticated path-finding algorithms
