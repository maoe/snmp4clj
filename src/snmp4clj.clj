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

(defn simple-test []
  (.getResponse
    (simple-snmp-get-next "public" "udp:localhost/161" "1.3.6")))
