(defproject cyberia "0.1.0"
  :description "Pleroma CLI client"
  :url "http://example.com/FIXME"
  :license {:name "The MIT License"
            :url "http://opensource.org/licenses/MIT"}
  :dependencies [[org.clojure/clojure "1.10.0"]
                 [cli-matic "0.4.3"]
                 [clj-http "3.12.3"]
                 [cheshire "5.10.2"]]
  :main ^:skip-aot cyberia.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
