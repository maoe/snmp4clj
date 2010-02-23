(ns snmp4clj.core
  (:use [snmp4clj.pdu]
        [snmp4clj.target])
  (:import [org.snmp4j PDU]
           [org.snmp4j.smi OID]
           [org.snmp4j.util TreeUtils DefaultPDUFactory]))

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

(defn snmp-walk [session community address version oid]
  (let [tgt (create-target version
               :community community
               :address address)
        util (doto (TreeUtils. session (DefaultPDUFactory.))
               (.setMaxRepetitions 10))]
    (.getSubtree util tgt (OID. oid))))

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

;; (defmacro with-session [session & body]
;;  (let [s (gensym "session-")]
;;    `(let [~s session]
;;     ~@(map #(list s %) body))))

