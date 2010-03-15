(ns snmp4clj.core
  (:use [snmp4clj pdu target])
  (:use clojure.contrib.core
        funky)
  (:import [org.snmp4j Snmp PDU]
           [org.snmp4j.smi OID]
           [org.snmp4j.event ResponseListener]
           [org.snmp4j.util TableUtils TreeUtils DefaultPDUFactory])
  (:gen-class))

(defnk snmp-get
  [session
   :community "public"
   :address "udp:localhost/161"
   :version :v2c
   :async nil
   & oid]
  (let [pdu (create-pdu version PDU/GET oid)
        target (create-target version
              :community community
              :address address)]
    (if async
      (.send session pdu target nil async)
      (-?> (.send session pdu target)
           (.getResponse)
           (.getVariableBindings)
           (seq)))))

(defnk snmp-get-next
  [session
   :community "public"
   :address "udp:localhost/161"
   :version :v2c
   :async nil
   & oid]
  (let [pdu (apply create-pdu version PDU/GETNEXT oid)
        target (create-target version
              :community community
              :address address)]
    (if async
      (.send session pdu target nil async)
      (-?> (.send session pdu target)
           (.getResponse)
           (.getVariableBindings)
           (seq)))))

(defnk snmp-get-bulk
  [session
   :community "public"
   :address "udp:localhost/161"
   :version :v2c
   :async nil
   & oid]
  (let [pdu (create-pdu version PDU/GETBULK oid)
        target (create-target version
              :community community
              :address address)]
    (if async
      (.send session pdu target nil async)
      (-?> (.send session pdu target)
           (.getResponse)
           (.getVariableBindings)
           (seq)))))

(defnk snmp-table-walk
  [session
   :community "public"
   :address "udp:localhost/161"
   :version :v2c
   :max-rows-per-pdu 10
   :max-cols-per-pdu 10
   :lower-bound nil
   :upper-bound nil
   :async nil
   & oid]
  (let [target (create-target version
                 :community community
                 :address address)
        table (doto (TableUtils. session (DefaultPDUFactory.))
               (.setMaxNumRowsPerPDU max-rows-per-pdu)
               (.setMaxNumColumnsPerPDU max-cols-per-pdu))]
    (if async
      (.getTable table target async nil lower-bound upper-bound)
      ;; FIXME: ugly
      (let [tbl (.getTable table target
                           (into-array OID (map #(OID. %) oid))
                           lower-bound upper-bound)]
        (let [ret (map (comp seq (memfn getColumns)) tbl)]
          (if (first ret)
            ret
            '()))))))
