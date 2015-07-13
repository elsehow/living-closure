(ns async-tea-party.core
  (:gen-class) ;;  generate a class for it to run as a standalone
  (:require [clojure.core.async :as async]))

;; >!! blocking/synchronous put
;; <!! blocking/synchronous take

(def google-tea-service-chan (async/chan 10))
(def yahoo-tea-service-chan (async/chan 10))

;; sum a vector filled with 1s of random length 0 to 100000
(defn random-add []
  (reduce + (conj [] (repeat 1 (rand-int 100000)))))

(defn request-google-tea-service []
  (async/go
   (random-add)
   ;; async/non-blocking put
   (async/>! google-tea-service-chan
             "tea compliments of google")))

(defn request-yahoo-tea-service []
  (async/go
   (random-add)
   (async/>! yahoo-tea-service-chan
             "tea compliments of yahoo")))


(def result-chan (async/chan 10))

(defn request-tea []
  (request-google-tea-service)
  (request-yahoo-tea-service)
  ;; i think i get it ,,  alts! .. like alternate channels that deliver the same thing ?? ...
  (async/go (let [[v] (async/alts!
                       [google-tea-service-chan
                        yahoo-tea-service-chan])]
              (async/>! result-chan v))))


;; entrypoint
(defn -main [& args]
  (println "requesting tea!")
  (request-tea)
  ;; do a blocking take to wait for result
  (println (async/<!! result-chan)))
