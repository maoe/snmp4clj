(ns snmp4clj.examples
  (:use snmp4clj
        snmp4clj.session
        snmp4clj.util)
  (:use clojure.contrib.pprint)
  (:gen-class))

(defn -main []
  (with-snmp-session session
    ;; retrieve a name of the first network interface
    (->> (snmp-get-next session
           :address "udp:localhost/161"
           "1.3.6.1.2.1.31.1.1.1")
         (.getResponse)
         (pprint))

    ;; retrieve a status map of all network interfaces
    (->> (snmp-table-walk session
           "1.3.6.1.2.1.31.1.1.1.1"
           "1.3.6.1.2.1.31.1.1.1.2"
           "1.3.6.1.2.1.31.1.1.1.3")
         (pprint))

    ;; table-walk with for-table-* utilities
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
         (.getVariable c)))
    )))
