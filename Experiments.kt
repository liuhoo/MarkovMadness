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

fun binaryPity() {
    val markovChain = MarkovChainBuilder()
        .addState("Hit")
        .addState("Miss")
        .addTransitionProbability("Hit", "Miss", 0.5)
        .addTransitionProbability("Hit", "Hit", 0.5)
        .addTransitionProbability("Miss", "Hit", 1.0)
}

fun fourStarBanner() {
    val layer1 =
        MarkovChainBuilder()
            .apply {
                this.addState("pass")

                // Each state represents the number of successive losses, capping at 9.
                for (i in 0..9)
                    this.addState("$i")

                // If you pass, you start again
                this.addTransitionProbability("pass", "0", 1.0 / 1.0)

                // After 9 failures, you are guaranteed a pass.
                this.addTransitionProbability("9", "pass", 1.0 / 1.0)

                // For 0-8 failures, there is a 10% chance to pass.
                for (i in 0..8) {
                    this.addTransitionProbability("$i", "pass", 1.0 / 10.0)
                        .addTransitionProbability("$i", "${i + 1}", 9.0 / 10.0)
                }
            }
            .build()

    val layer2 =
        MarkovChainBuilder()
            // if you pass, you either hit or miss
            .addState("hit")
            .addState("miss")
            // you have a 50% chance to hit
            .addTransitionProbability("hit", "hit", 1.0 / 2.0)
            .addTransitionProbability("hit", "miss", 1.0 / 2.0)
            // unless you failed last time
            .addTransitionProbability("miss", "hit", 1.0 / 1.0)
            .build()

    val layer3 =
        MarkovChainBuilder()
            // 1/3 chance to get +, 2/3 chance to get -
            .addState("+")
            .addState("-")
            .addTransitionProbability("+", "+", 1.0 / 3.0)
            .addTransitionProbability("+", "-", 2.0 / 3.0)
            .addTransitionProbability("-", "+", 1.0 / 3.0)
            .addTransitionProbability("-", "-", 2.0 / 3.0)
            .build()

    val count1 = mutableMapOf<String, Int>() // counts layer1 states
    val count2 = mutableMapOf<String, Int>() // counts layer2 states
    val count3 = mutableMapOf<String, Int>() // counts layer3 states
    var successes = 0 // how many times was 40 enough?
    val trials = 1_000_000 // 1 mil trials
    repeat (trials) {
        var got = false
        var state1 = layer1.nameToState["0"]!!
        var state2 = layer2.nameToState["hit"]!!
        var state3 = layer3.nameToState["-"]!!

        for (i in 1..40) {
            state1 = layer1.transition(state1)
            val name1 = state1.name
            if (name1 != "pass") { // we did not pass
                count1[name1] = count1.getOrDefault(name1, 0) + 1
                continue // did you know you can't use continue inside a repeat?
            } // we did pass
            state1 = layer1.transition(state1) // pass is just an auxiliary state

            state2 = layer2.transition(state2)
            val name2 = state2.name
            count2[name2] = count2.getOrDefault(name2, 0) + 1

            if (state2.name != "hit") continue // we did not hit, contiue

            state3 = layer3.transition(state3) // we hit, see if + or -
            val name3 = state3.name
            count3[name3] = count3.getOrDefault(name3, 0) + 1
            if (name3 == "+")
                got = true
        }
        if (got) successes += 1

    }
    println(count1)
    println(count2)
    println(count3)
    println(successes.toFloat() / trials.toFloat())
}

fun coinFlips() {
    val markovChain = MarkovChainBuilder()
        .addState("0")
        .addState("1")
        .addState("2")
        .addState("3")
        .addState("4")
        .addTransitionProbability("0", "0", 0.5)
        .addTransitionProbability("0", "1", 0.5)
        .addTransitionProbability("1", "1", 0.5)
        .addTransitionProbability("1", "2", 0.5)
        .addTransitionProbability("2", "2", 0.5)
        .addTransitionProbability("2", "3", 0.5)
        .addTransitionProbability("3", "3", 0.5)
        .addTransitionProbability("3", "4", 0.5)
        .addTransitionProbability("4", "4", 1.0)
        .build()

    //TODO: run the simulation
}