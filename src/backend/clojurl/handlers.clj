(ns clojurl.handlers
  (:require [ring.util.response :as r]
            [clojurl.db :as db] 
            [clojurl.slug :refer [generate-slug]]))


;; -------------
;; HANDLERS
;; -------------


;; WELCOME HANDLER
;; Gives back reponse on the / page

(defn welcome-handler [_]
  (r/response {:status 200 :body "ClojURL API is up and running"}))


;; REDIRECT HANDLER
;; Redirects to the un-clojurl'ed url

(defn redirect-handler [req]
  (let [slug (-> req :path-params :slug)
        url (db/get-url slug)]
    (if url
      (r/redirect url 307)
      (r/not-found {:status 404 :data "Not found"}))))

;; GET ALL HANDLER
;; Gets data of all the shortened urls

(defn get-all-handler [_]
  (let [data (clojurl.db/get-all)]
    (r/response {:status 200 :data data})))


;; SHORTEN HANDLER
;; Shortens a urls

(defn shorten-handler [req]
  (let [url (-> req :body-params :url)
        slug (generate-slug)]
    (db/insert-redirect! slug url)
    (r/response {:status 200 :data {:url url :slug slug}})))

;; DELETE HANDLER
;; Deletes a url

(defn delete-handler [req] 
  (let [slug (-> req :path-params :slug)
        flag (db/delete-redirect! slug)]
    (if (= (first flag) 0)
      (r/not-found {:status 404 :data "Cannot find url"}) 
      (r/response {:status 200 :data (str "Deleted " slug)})
    )  ))