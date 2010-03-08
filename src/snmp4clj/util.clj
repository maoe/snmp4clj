(ns snmp4clj.util
  (:import [org.snmp4j.util TableUtils]))

(defmacro for-table-with-row [row-name table & body]
  `(for [~row-name ~table]
     (let [~row-name (seq (.getColumns ~row-name))]
       ~@body)))

(defmacro for-table-with-column [column-name table & body]
  `(for-table-with-row row# ~table
     (for [~column-name row#] ~@body)))

