(ns snmp4clj.target
  (:import (org.snmp4j CommunityTarget)
           (org.snmp4j.mp SnmpConstants)
           (org.snmp4j.smi GenericAddress OctetString)))

(defmulti create-target
  (fn [v _ & _]
    (if (contains? #{:v1 :v2c :v3} v)
      v
      :v2c)))

(defmethod create-target :default [v & opts]
  (let [opts (merge {:community "public" :max-pdu 65535 }
                    (apply hash-map opts))
        {:keys [community address timeout retries max-pdu]} opts]
    (doto (CommunityTarget.)
      (.setCommunity (OctetString. community))
      (.setVersion SnmpConstants/version2c)
      (.setAddress (GenericAddress/parse address)))))


