(defproject snmp4clj "0.0.1-SNAPSHOT"
  :description "SNMP API for Clojure"
  :dependencies [[org.clojure/clojure "1.1.0-master-SNAPSHOT"]
                 [org.clojure/clojure-contrib "1.1.0-master-SNAPSHOT"]
                 [org.snmp4j/snmp4j "1.8.1"]
                 [org.clojars.maoe/funky "0.0.5"]]
  :dev-dependencies [[leiningen/lein-swank "1.1.0"]]
  :namespaces [snmp4j]
  :main snmp4clj.examples)
