(ns snmp4clj.pdu
  (:import [org.snmp4j Snmp PDUv1 PDU ScopedPDU]
           [org.snmp4j.smi OID VariableBinding]))

(defn init-pdu [pdu type & oid]
  (doto pdu
    (.setType type)
    (.addAll
      (into-array VariableBinding
        (map #(VariableBinding. (OID. %)) oid)))))

(defmulti create-pdu
  (fn [v & _]
    (if (contains? #{:v1 :v2c :v3} v) v :v2c)))

(defmethod create-pdu :v1
  [_ type & oid]
  (apply init-pdu (PDUv1.) type oid))

(defmethod create-pdu :default
  [_ type & oid]
  (apply init-pdu (PDU.) type oid))

(defmethod create-pdu :v3
  [_ type & oid]
  (apply init-pdu (ScopedPDU.) type oid))
