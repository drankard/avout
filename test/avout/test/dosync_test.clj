(ns avout.test.dosync-test
  (:use [clojure.test])
  (:require [avout.core :as avout]))

;; Just because im am curious as why you have the coll? check in core.deref!! ?
;; `(if (or (coll? '~client)
;;   (not (instance? org.apache.zookeeper.ZooKeeper ~client)))
;;   (throw (RuntimeException. "First argument to dosync!! must be a ZooKeeper client instance."))...
;; Is it to avoid sideeffects of unquoting ~client ?


(def zk-client (ref nil))

(defmacro my-dosync!!-1 [& body]
  `(let [a# @zk-client] ;; I have to deref before i call dosync!!
     (avout/dosync!! a# ~@body)))

(defmacro my-dosync!!-2 [& body]
  `(avout/dosync!! @zk-client ~@body))  ;; This will fail

(deftest dosync-test-1
  (dosync (ref-set zk-client (avout/connect "127.0.0.1")))
  (is (my-dosync!!-1 true))
  (is (my-dosync!!-2 true)))