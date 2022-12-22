(ns clojurl.db
  (:require [clojure.java.jdbc :as j]
            [honey.sql :as sql]
            [honey.sql.helpers :refer :all]
            [clojurl.env :refer [env]]))


;; -------------
;; DB DETAILS
;; -------------

;; DB Provider
;; https://app.planetscale.com/

(def mysql-db {:dbtype "mysql"
               :host (env :HOST)
               :dbname (env :DBNAME)
               :user (env :USER)
               :password (env :PASSWORD)})

;; -------------
;; DB HELPER FUNCTIONS
;; -------------

( defn query [q]
 (j/query mysql-db q)) 

(defn statement! [q]
  (j/db-do-prepared mysql-db q))

;; -------------
;; DB FUNCTIONS
;; -------------

(defn get-all []
  (query (-> (select :*) (from :redirects) (sql/format))))

(defn get-url [slug]
  (-> (query (-> (select :*)
             (from :redirects)
             (where [:= :slug slug])
             (sql/format))) 
      first 
      :url))

(defn insert-redirect! [slug url]
  (statement! (-> (insert-into :redirects)
                    (columns :slug :url)
                    (values [[slug url]])
                    (sql/format))))

(defn delete-redirect! [slug]
  (statement! (-> (delete-from :redirects)
               (where [:= :slug slug])
               (sql/format))))