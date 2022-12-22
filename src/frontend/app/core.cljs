(ns app.core (:require [helix.core :refer [defnc $]]
                       [helix.hooks :as hooks]
                       [helix.dom :as d]
                       ["react-dom/client" :as rdom]
                       [app.component.form :refer [form-component]]
                       [app.component.links :refer [links-component]]))

(defnc app []
  (let [[state set-state] (hooks/use-state {:slug nil :url ""})
        [links set-links] (hooks/use-state [])]

    (d/div {:class-name " flex flex-col items-center justify-center bg-blue-50 min-h-screen"}
           (d/h1 {:class-name "mt-16 text-5xl font-bold text-blue-500"} "ClojURL")
           ($ form-component {:state state :set-state set-state :links links :set-links set-links})
           ($ links-component {:links links :set-links set-links}))))




(defn ^:export init []
  (let [root (rdom/createRoot (js/document.getElementById "app"))]
    (.render root ($ app))))

