(ns snmp4clj
  (:use snmp4clj.pdu
        snmp4clj.target
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
      (.send session pdu target))))

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
      (.send session pdu target))))

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
      (.send session pdu target))))

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
        tree (doto (TableUtils. session (DefaultPDUFactory.))
               (.setMaxNumRowsPerPDU max-rows-per-pdu)
               (.setMaxNumColumnsPerPDU max-cols-per-pdu))]
    (seq
     (.getTable tree target
       (into-array OID (map #(OID. %) oid))
       lower-bound
       upper-bound))))
