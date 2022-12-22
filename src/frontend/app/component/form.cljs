(ns app.component.form (:require [helix.core :refer [defnc $]]
                       [helix.hooks :as hooks]
                       [helix.dom :as d]
                       [promesa.core :as p]))

(defnc form-component [{:keys [state set-state links set-links]}]
  
 (let [fetch-slug (fn []
                    (p/let [_response (js/fetch "/api/shorten" (clj->js {:headers {:Content-Type "application/json"} :method "POST" :body (js/JSON.stringify #js {:url (:url state)})}))
                            response (.json _response)
                            data (js->clj response :keywordize-keys true)]
                      (set-state assoc :slug (-> data :data :slug))
                      (set-links (conj links {:slug (-> data :data :slug) :url (-> data :data :url)}))))] 
   
   (d/form {:class-name "my-8 min-w-[90%] md:min-w-[60%]"} 
           (d/input {:class-name "px-4 py-4 min-w-[70%] outline-none rounded-l-xl shadow"
                     :value (:url state)
                     :on-change #(set-state assoc :url (.. % -target -value))
                     :placeholder "Enter an URL to shorten!"})
           (d/button {:class-name "py-4 min-w-[30%] bg-blue-200 hover:bg-blue-400 outline-none text-blue-500 hover:text-blue-50 transition-colors rounded-r-xl shadow"
                      :type "submit"
                      :on-click (fn [e] (.preventDefault e) (fetch-slug))}
                     "SHORTEN")))
  
  )