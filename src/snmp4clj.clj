(ns snmp4clj
  (:import (org.snmp4j Snmp PDUv1 PDU ScopedPDU CommunityTarget)
           (org.snmp4j.mp SnmpConstants)
           (org.snmp4j.smi OID
                           VariableBinding
                           GenericAddress
                           OctetString)
           (org.snmp4j.transport DefaultUdpTransportMapping)))

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

(defn snmp-get [session community address version oid]
  (let [pdu (create-pdu version PDU/GET oid)
        tgt (create-target version
              :community community
              :address address)]
    (.send session pdu tgt)))

(defn snmp-get-next [session community address version oid]
  (let [pdu (create-pdu version PDU/GETNEXT oid)
        tgt (create-target version
              :community community
              :address address)]
    (.send session pdu tgt)))

(defn snmp-get-bulk [oid target version]
  (create-pdu version PDU/GETBULK (map oid oid)))

(defn snmp-walk [indice oids target version])

;; Simple wrapper functions
(defn- simple-send [f community address oid]
  (let [session (doto (Snmp. (DefaultUdpTransportMapping.))
                  (.listen))]
    (f session community address :v2c oid)))

(def simple-snmp-get
  (partial simple-send snmp-get))

(def simple-snmp-get-next
  (partial simple-send snmp-get-next))

;;
;; (with-session [session & body])
;;
;; (with-session (create-session ...)
;;   (get-next ...)
;;   (walk ...))
;;
;; =>
;;
;; (let [s (create-session ...)]
;;   (get-next ... s)
;;   (walk ... s))
;;

(defmacro with-session [session & body]
  (let [s (gensym "session-")]
    `(let [~s session]
     ~@(map #(list s %) body))))

(defn simple-test []
  (.getResponse
    (simple-snmp-get-next "public" "udp:localhost/161" "1.3.6")))

