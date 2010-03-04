(ns snmp4clj.session
  (:import [org.snmp4j Snmp]
           [org.snmp4j.transport DefaultUdpTransportMapping]))

(defmacro with-snmp-session
  [session-name & forms]
  `(let [~session-name (doto (Snmp. (DefaultUdpTransportMapping.))
                         (.listen))]
     ~@forms))
