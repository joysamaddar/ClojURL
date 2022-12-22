(ns clojurl.env 
  (:require [clojure.java.io :as jio]))

(def env-vars 
  (if (.exists (clojure.java.io/file "env.edn")) 
    (clojure.edn/read-string (slurp "env.edn"))
    {}))

(defn env [k]
  (or (k env-vars) (System/getenv (name k))))