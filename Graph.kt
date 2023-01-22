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

class Vertex(
    val name: String,
    val adjacencyList: MutableList<Vertex>,
    val weightList: MutableList<Float>
) {
    constructor(name: String) : this(name, mutableListOf<Vertex>(), mutableListOf<Float>())
}

class Graph(
    val vertices: MutableList<Vertex>,
    val nameToVertex: MutableMap<String, Vertex>
) {
    constructor() : this(mutableListOf<Vertex>(), mutableMapOf<String, Vertex>())

    fun addVertex(name: String? = null): Graph {
        val vertexName = name ?: "vertex ${vertices.size - 1}"
        val vertex = Vertex(vertexName)
        this.nameToVertex[vertexName] = vertex

        vertices.add(vertex)

        return this
    }

    fun addEdge(index1: Int, index2: Int, weight: Float): Graph {
        vertices[index1].adjacencyList.add(vertices[index2])
        vertices[index2].weightList.add(weight)

        return this
    }

    fun addEdge(name1: String, name2: String, weight: Float): Graph {
        val vertex1 = nameToVertex[name1]!!
        val vertex2 = nameToVertex[name2]!!

        vertex1.adjacencyList.add(vertex2)
        vertex1.weightList.add(weight)

        return this
    }
}