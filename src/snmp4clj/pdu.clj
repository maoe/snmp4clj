(ns snmp4clj.pdu
  (:import (org.snmp4j Snmp PDUv1 PDU ScopedPDU)
           (org.snmp4j.smi OID VariableBinding)))

(defmulti create-pdu
  (fn [v _ _]
    (if (contains? #{:v1 :v2c :v3} v)
      v
      :v2c)))

(defmethod create-pdu :v1 [_ type oid]
  (doto (PDUv1.)
    (.setType type)
    (.add (VariableBinding. (OID. oid)))))

(defmethod create-pdu :default [_ type oid]
  (doto (PDU.)
    (.setType type)
    (.add (VariableBinding. (OID. oid)))))

(defmethod create-pdu :v3 [_ type oid]
  (doto (ScopedPDU.)
    (.setType type)
    (.add (VariableBinding. (OID. oid)))))
