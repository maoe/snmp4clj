(ns snmp4clj
  (:use snmp4clj.core)
  (:import [org.snmp4j Snmp PDU]
           [org.snmp4j.transport DefaultUdpTransportMapping]))

;; Simple wrapper functions
(defn- simple-send [f community address oid]
  (let [session (doto (Snmp. (DefaultUdpTransportMapping.))
                  (.listen))]
    (f session community address :v2c oid)))

(def simple-snmp-get
  (partial simple-send snmp-get))

(def simple-snmp-get-next
  (partial simple-send snmp-get-next))

(def simple-snmp-walk
  (partial simple-send snmp-walk))

(defn simple-test []
  (println (simple-snmp-get-next
             "public" "udp:localhost/161" "1.3.6.1.2.1.31.1.1.1"))
  (println (apply + (map #(count (.getVariableBindings %))
                         (simple-snmp-walk
                           "public" "udp:localhost/161"
                           "1.3.6.1.2.1.31.1.1.1")))))
