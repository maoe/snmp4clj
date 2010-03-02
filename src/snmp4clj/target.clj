(ns snmp4clj.target
  (:import (org.snmp4j CommunityTarget)
           (org.snmp4j.mp SnmpConstants)
           (org.snmp4j.smi GenericAddress OctetString))
  (:use org.clojars.maoe.funky))

(defmulti create-target
  (fn [v & _]
    (if (contains? #{:v1 :v2c :v3} v)
      v
      :v2c)))

(defmethodk create-target :default
  [_
   :community "public"
   :address "udp:localhost/161"
   :timeout 10
   :retries 3
   :max-pdu 65535
   & extras]
  (doto (CommunityTarget.)
    (.setCommunity (OctetString. community))
    (.setVersion SnmpConstants/version2c)
    (.setAddress (GenericAddress/parse address))))

(defmethodk create-target :v3
  [version
   :community "public"
   :address "udp:localhost/161"
   :timeout 10
   :retries 3
   :max-pdu 65535
   & extras]
  (println "create-target: not implemented yet"))
