(defproject ring-image-crop "0.0.1"
  :description "ring middleware that crops images accordingt to query params"
  :main ring-image-crop.core
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :repositories [["releases" {:url "https://clojars.org/repo" :creds :gpg}]]
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [ring "1.4.0"]]
  :profiles {:dev {:dependencies [[ring/ring-mock "0.3.0"]
                                  [ring "1.4.0"]]}})
