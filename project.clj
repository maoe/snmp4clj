(defproject org.clojars.maoe/snmp4clj "0.0.1-SNAPSHOT"
  :description "SNMP API for Clojure"
  :dependencies [[org.clojure/clojure "1.1.0-master-SNAPSHOT"]
                 [org.clojure/clojure-contrib "1.1.0-master-SNAPSHOT"]
                 [org.snmp4j/snmp4j "1.11"]
                 [org.clojars.maoe/funky "0.0.6"]]
  :dev-dependencies [[leiningen/lein-swank "1.2.0-SNAPSHOT"]
                     [lein-clojars "0.5.0-SNAPSHOT"]
                     [autodoc "0.7.0"]]
  :namespaces [snmp4j]
  :repositories {"oosnmp" "https://server.oosnmp.net/dist/release"}
  :main snmp4clj.examples)
