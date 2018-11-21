# sa-tsp-cljs

An application to demonstrate **simulated annealing** optimization via the **traveling salesman problem**.
Implemented in Clojurescript and [re-frame](https://github.com/Day8/re-frame).

Try it out:

https://perrygeo.github.io/sa-tsp-cljs/

## Status

First draft

<img src="https://raw.github.com/perrygeo/sa-tsp-cljs/master/screenshot.png"/>


## TODO

* Animated graphs to visualize sa metrics
* Experiment with different neighbor functions
* Track previous best states
* Expose acceptance, rejection and improvement rates
* Disentangle TSP and SA namespaces into src/cljc and fill out the clojure side

## Development

```
lein clean
lein figwheel dev
open "http://localhost:3449/"
```

## Production Build

TODO wrap in Makefile

```
lein clean
lein cljsbuild once min
```
