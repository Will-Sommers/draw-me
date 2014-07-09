(defproject draw-me "0.1.0-SNAPSHOT"
  :description "FIXME: write this!"
  :url "http://example.com/FIXME"

  :dependencies [[org.clojure/clojure "1.6.0"]
                 [org.clojure/clojurescript "0.0-2234"]
                 [org.clojure/core.async "0.1.267.0-0d7780-alpha"]
                 [om "0.6.4"]
                 [ankha "0.1.3"]
                 [prismatic/om-tools "0.2.2"]
                 [com.cemerick/piggieback "0.1.3"]
                 [weasel "0.3.0"]]

  :plugins [[lein-cljsbuild "1.0.3"]]

  :profiles {:dev {:source-paths ["src" "dev"]}}

  :repl-options {:nrepl-middleware [cemerick.piggieback/wrap-cljs-repl]}

  :cljsbuild {
    :builds [{:id "dev"
              :source-paths ["src" "dev"]
              :compiler {
                :output-to "draw_me.js"
                :output-dir "out"
                :optimizations :none
                :source-map true}}]})
