.PHONY: build

all: clean build

build:
	lein cljsbuild once min
	mkdir -p docs/js/compiled
	cp resources/public/index.html docs/index.html
	cp resources/public/js/compiled/app.js docs/js/compiled/app.js

clean:
	lein clean
