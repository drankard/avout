(ns avout.refs.zk
  (:use avout.state)
  (:require [zookeeper :as zk]
            [zookeeper.data :as data]
            [avout.config :as cfg]
            [avout.util :as util]))

;; ZK data implementation

(deftype ZKVersionedStateContainer [client name]

  Identity
  (init [this] nil)

  (getName [this] name)

  (destroy [this] nil)

  VersionedStateContainer
  (getStateAt [this version]
    (let [{:keys [data stat]} (zk/data client (str name cfg/HISTORY cfg/NODE-DELIM version))]
      (util/deserialize-form data)))

  (setStateAt [this value version]
    (zk/set-data client (str name cfg/HISTORY cfg/NODE-DELIM version) (util/serialize-form value) -1)))
