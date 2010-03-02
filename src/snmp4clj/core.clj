(ns snmp4clj.core
  (:use snmp4clj.pdu
        snmp4clj.target)
  (:import [org.snmp4j Snmp]
           [org.snmp4j.transport DefaultUdpTransportMapping]))

(defmacro with-session
  [session-name & forms]
  `(let [~session-name (doto (Snmp. (DefaultUdpTransportMapping.))
                         (.listen))]
     ~@forms))

