(ns snmp4clj.examples
  (:use snmp4clj
        snmp4clj.session)
  (:use clojure.contrib.pprint)
  (:gen-class))

(defn -main []
  (with-snmp-session session
    ;; retrieve a name of the first network interface
    (->> (snmp-get-next session
           :address "udp:localhost/161"
           "1.3.6.1.2.1.31.1.1.1")
         (.getResponse)
         (pprint)
         (println))

    ;; retrieve a status map of all network interfaces
    (->> (snmp-table-walk session
           :address "udp:localhost/161" 
           "1.3.6.1.2.1.31.1.1.1.1"
           "1.3.6.1.2.1.31.1.1.1.2"
           "1.3.6.1.2.1.31.1.1.1.3")
         (pprint)
         (println))))

(defn table []
  (with-snmp-session s
    (snmp-table-walk s
      "1.3.6.1.2.1.31.1.1.1.1"
      "1.3.6.1.2.1.31.1.1.1.2"
      "1.3.6.1.2.1.31.1.1.1.3")))
