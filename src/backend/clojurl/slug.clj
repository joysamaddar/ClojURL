(ns clojurl.slug)

(def charset "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789")

;; -------------
;; SLUG GENERATOR
;; Generates a random 5 character slug from the charset above
;; -------------

(defn generate-slug []
  (->> (repeatedly #(rand-nth charset))
       (take 5)
       (apply str))
  )