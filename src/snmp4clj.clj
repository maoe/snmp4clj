(ns snmp4clj
  (:use snmp4clj.core
        snmp4clj.pdu
        snmp4clj.target
        org.clojars.maoe.funky)
  (:import [org.snmp4j Snmp PDU]
           [org.snmp4j.smi OID]
           [org.snmp4j.util TreeUtils DefaultPDUFactory])
  (:gen-class))

(defnk snmp-get
  [session
   :community "public"
   :address "udp:localhost/161"
   :version :v2c
   & oid]
  (let [pdu (create-pdu version PDU/GET oid)
        tgt (create-target version community address)]
    (.send session pdu tgt)))

(defnk snmp-get-next
  [session
   :community "public"
   :address "udp:localhost/161"
   :version :v2c
   & oid]
  (let [pdu (apply create-pdu version PDU/GETNEXT oid)
        tgt (create-target version community address)]
    (.send session pdu tgt)))

(defnk snmp-get-bulk
  [session
   :community "public"
   :address "udp:localhost/161"
   :version :v2c
   & oid]
  (let [pdu (create-pdu version PDU/GETBULK oid)
        tgt (create-target version community address)]
    (.send session pdu tgt)))

(defnk snmp-walk
  [session
   :community "public"
   :address "udp:localhost/161"
   :version :v2c
   & oid]
  (let [tgt (create-target version community address)
        util (doto (TreeUtils. session (DefaultPDUFactory.))
               (.setMaxRepetitions 10))]
    (.getSubtree util tgt (OID. oid))))

;; debug
(defn -main []
  (with-session session
    (->> (snmp-get-next session "1.3.6.1.2.1.31.1.1.1")
         (.getResponse)
         (println))))
