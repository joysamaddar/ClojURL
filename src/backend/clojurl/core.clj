(ns clojurl.core
  (:require [ring.adapter.jetty :as ring-jetty]
            [reitit.ring :as ring]
            [muuntaja.core :as m]
            [reitit.ring.middleware.muuntaja :as muuntaja] 
            [clojurl.handlers :refer [welcome-handler redirect-handler get-all-handler shorten-handler delete-handler]]
            [clojure.java.io :as io])
  (:gen-class)
  )

;; -------------
;; APP INIT
;; -------------

(defn index []
  (slurp (io/resource "public/index.html")))

(def app 
  (ring/ring-handler
   (ring/router

    ;; -------------
    ;; ROUTES
    ;; -------------

    [["/" {:handler (fn [req] {:body (index) :status 200})}]
     ["/assets/*" (ring/create-resource-handler {:root "public/assets"})]
     ["/:slug" {:get redirect-handler}]
     ["/api"
      ["/" {:get welcome-handler}]
      ["/urls" {:get get-all-handler}]
      ["/shorten" {:post shorten-handler}]
      ["/delete" 
       ["/:slug" {:delete delete-handler}]]]]

    {:data {:muuntaja m/instance
            :middleware [muuntaja/format-middleware]}})))

(defn start []
  (ring-jetty/run-jetty #'app {:port 3000
                             :join? false}))

(defn -main []
  (start))

(comment 

  (def server (start))
  

  (.stop server)
  )