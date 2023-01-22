/*
Copyright (c) 2023 Google LLC

Permission is hereby granted, free of charge, to any person obtaining a copy of
this software and associated documentation files (the "Software"), to deal in
the Software without restriction, including without limitation the rights to
use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
the Software, and to permit persons to whom the Software is furnished to do so,
subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/

import kotlin.random.Random

class MarkovChain(graph: Graph) {
    val state: Array<Vertex>
    val nameToState: Map<String, Vertex>
    val transitionMatrix: Array<FloatArray>

    init {
        state = graph.vertices.toTypedArray()
        nameToState = graph.nameToVertex.toMap()
        transitionMatrix = Array(state.size) { FloatArray(state.size) }

        val vertexToIndex = mutableMapOf<Vertex, Int>()
        state.forEachIndexed { index, vertex ->
            vertexToIndex[vertex] = index
        }

        state.forEachIndexed { index1, srcVertex ->
            srcVertex.adjacencyList
                .zip(srcVertex.weightList)
                .forEach {
                    val dstVertex = it.first
                    val weight = it.second

                    val index2 = vertexToIndex[dstVertex] ?: throw Exception("vertex not in graph")

                    if ((weight < 0.0) or (weight >= 1.0)) {
                        throw Exception("weight is not a probability")
                    }

                    transitionMatrix[index1][index2] = weight
                }
        }
    }

    fun transition(state: Vertex): Vertex {
        val p = Random.nextFloat()
        var sumP = 0.0
        state.adjacencyList.zip(state.weightList).forEach {
            val vertex = it.first
            val probability = it.second

            sumP += probability
            if (sumP >= p) {
                return vertex
            }
        }
        throw ArithmeticException("sum of probabilities does not add to 1.0")
    }

    fun transitionSteps(state: Vertex, transitions: Int): Vertex {
        var currentState = state
        repeat(transitions) { currentState = this.transition(state) }
        return currentState
    }

    fun traceTransitionSteps(state: Vertex, transitions: Int): List<String> {
        val transitionTrace = mutableListOf<String>()
        var currentState = state
        repeat(transitions) {
            currentState = this.transition(state)
            transitionTrace.add(state.name)
        }
        return transitionTrace.toList()
    }
}

class MarkovChainBuilder() {
    val graph = Graph()

    fun addVertex(name: String? = null): MarkovChainBuilder {
        graph.addVertex(name)
        return this
    }

    fun addEdge(index1: Int, index2: Int, weight: Float): MarkovChainBuilder {
        graph.addEdge(index1, index2, weight)
        return this
    }

    fun addEdge(name1: String, name2: String, weight: Float): MarkovChainBuilder {
        graph.addEdge(name1, name2, weight)
        return this
    }

    fun build(): MarkovChain {
        return MarkovChain(graph)
    }
}