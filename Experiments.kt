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

fun gamblersRuin() {
    val markovChain = MarkovChainBuilder()
        .addState("$0")
        .addState("$2")
        .addState("$4")
        .addState("$6")
        .addState("$8")
        .addTransitionProbability("$2", "$4", 0.5)
        .addTransitionProbability("$4", "$6", 0.5)
        .addTransitionProbability("$6", "$8", 0.5)
        .addTransitionProbability("$8", "$8", 1.0)
        .addTransitionProbability("$6", "$4", 0.5)
        .addTransitionProbability("$4", "$2", 0.5)
        .addTransitionProbability("$2", "$0", 0.5)
        .addTransitionProbability("$0", "$0", 1.0)
        .build()

    val transitionsCount = mutableMapOf<Int, Int>()
    val finalVertexCount = mutableMapOf<String, Int>()
    for (i in 1..10000) {
        var state = markovChain.nameToState["$4"]!!
        var transitions = 0
        while (!markovChain.isTerminalState(state)) {
            state = markovChain.transition(state)
            transitions += 1
        }

        if (!transitionsCount.contains(transitions)) {
            transitionsCount[transitions] = 0
        }
        transitionsCount[transitions] = transitionsCount[transitions]!! + 1

        val name = state.name
        if (!finalVertexCount.contains(name)) {
            finalVertexCount[name] = 0
        }
        finalVertexCount[name] = finalVertexCount[name]!! + 1
    }

    println(transitionsCount.toSortedMap())
    println(finalVertexCount)
}