# sa-tsp-cljs

An application to demonstrate **simulated annealing** optimization via the **traveling salesman problem**.
Implemented in Clojurescript and [re-frame](https://github.com/Day8/re-frame).


## Status

WIP

<img src="https://raw.github.com/perrygeo/sa-tsp-cljs/master/screenshot.png"/>


## TODO

* Deploy a production build
* Performance optimization
* Experiment with different neighbor functions
* Graphs
* Expose acceptance, rejection and improvement rates
* Disentangle TSP and SA namespaces into src/cljc and fill out the clojure side

## Development

```
lein clean
lein figwheel dev
open "http://localhost:3449/"
```

## Production Build

```
lein clean
lein cljsbuild once min
```
