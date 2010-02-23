(ns snmp4clj.examples
  (:use snmp4clj))

(defn -main []
  (println (simple-snmp-get-next
             "public" "udp:localhost/161" "1.3.6.1.2.1.31.1.1.1"))
  (println (apply + (map #(count (.getVariableBindings %))
                         (simple-snmp-walk
                           "public" "udp:localhost/161"
                           "1.3.6.1.2.1.31.1.1.1")))))
