(ns snmp4clj.examples
  (:use snmp4clj
        snmp4clj.session
        snmp4clj.util)
  (:use [clojure.contrib core pprint])
  (:import [org.snmp4j.event ResponseListener])
  (:gen-class))

(defn -main []
  (with-snmp-session session
    (println "retrieve a name of the first network interface")
    (->> (snmp-get-next session "1.3.6.1.2.1.31.1.1.1.1")
         (.getResponse)
         (pprint))

    (println "retrieve asynchronously")
    (let [listener (proxy [ResponseListener] []
                     (onResponse [event]
                                 (print (.getResponse event))
                                 (println " (async)")))]
      (dotimes [_ 100]
        (snmp-get-next session
                       :async listener
                       "1.3.6.1.2.1.31.1.1.1.1")))

    (println "retrieve a status map of all network interfaces")
    (-?> (snmp-table-walk session
           "1.3.6.1.2.1.31.1.1.1.1"
           "1.3.6.1.2.1.31.1.1.1.2"
           "1.3.6.1.2.1.31.1.1.1.3")
         (pprint))

    (println "table-walk with for-table-* utilities")
    (let [table (snmp-table-walk session
                  "1.3.6.1.2.1.31.1.1.1.1"
                  "1.3.6.1.2.1.31.1.1.1.2"
                  "1.3.6.1.2.1.31.1.1.1.3")]
      (println
       (apply +
              (for-table-with-row r table
                (count r))))
      (println
       (for-table-with-column c table
         (.getVariable c))))
    ))
