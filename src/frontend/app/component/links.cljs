(ns app.component.links (:require [helix.core :refer [defnc $]]
                                  [helix.hooks :as hooks]
                                  [helix.dom :as d]
                                  [promesa.core :as p]))


(defnc links-component [{:keys [links set-links]}]
  (let [
        get-links (fn [] (p/let [_response (js/fetch "/api/urls" (clj->js {:headers {:Content-Type "application/json"} :method "GET"}))
                                 response (.json _response)
                                 data (js->clj response :keywordize-keys "true")]
                           (set-links (-> data :data)))) 
        
        delete-link (fn [endpoint] (p/let [_response (js/fetch (str "/api/delete/" endpoint) (clj->js {:headers {:Content-Type "application/json"} :method "DELETE"}))
                                           response (.json _response)
                                           data (js->clj response :keywordize-keys "true")]
                                     (set-links (filter (fn [el] (not= (:slug el) endpoint)) links))))
        
        click-handler (fn [e] (let [parentNode (.. e -target -parentNode -parentNode)
                                    clicked-slug (first (js/parentNode.getElementsByClassName "slug"))
                                    clicked-slug-value (.. clicked-slug -innerText)]
                                (delete-link clicked-slug-value)))
        ]
    
    (hooks/use-effect
     :once
     (get-links)) 
    
    (if (not-empty links)
      (d/div {:class-name "mb-16 min-w-full flex flex-col align-items justify-center"}
       (for [link links]

         (d/div {:class-name "min-w-[100%] md:min-w-[80%] mx-auto my-2 flex align-items justify-center" :key (:slug link)}
                (d/div {:class-name "bg-white py-4 min-w-[65%] md:min-w-[70%] text-center truncate"} (d/a {:href (:url link)} (:url link)))
                (d/div {:class-name "slug bg-blue-400 text-white py-4 min-w-[20%] text-center"} (d/a {:href (str (:slug link))} (:slug link)))
                (d/div {:class-name "bg-rose-500 text-red-900 py-4 min-w-[15%] md:min-w-[10%] text-center cursor-pointer" :on-click click-handler} (d/p {:class-name "cursor-pointer"} "X")))))
      
      (d/p "No links present!"))
   
    
    ))

