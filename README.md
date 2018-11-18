# simanneal

A [re-frame](https://github.com/Day8/re-frame) application designed to demonstrate simulated
annealing optimization using the travelling salesman problem.

## Status

WIP

## TODO

* Experiment with different neighbor functions
* Performance optimization
* Graphs
* Deploy a production build
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
