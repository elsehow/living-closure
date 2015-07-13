(ns async-tea-party.core
  (:require [clojure.core.async :as async]))

;; >!! blocking/synchronous put
;; <!! blocking/synchronous take

;; create a buffered tea-channel
(def tea-channel (async/chan 10))
(async/>!! tea-channel :cup-of-tea)

; !! always means a blocking call

;;get it off again
(async/<!! tea-channel)

(async/>!! tea-channel :cup-of-tea-1)
(async/>!! tea-channel :cup-of-tea-2)
(async/>!! tea-channel :cup-of-tea-3)
(async/>!! tea-channel :cup-of-tea-4)
;; close the channel
(async/close! tea-channel)
;; will read off the channel, reading nil when the channels done
(async/<!! tea-channel)


;async is signified by >!  <!

(def tea-channel (async/chan 10))

;; go loop will take from the channel when there's something, otherwise wait
(async/go-loop []
               (println "thanks for the " (async/<! tea-channel))
               (recur))

(async/>!! tea-channel :hot-cup-of-tea)


;; multiple channels
(def tea-channel (async/chan 10))
(def milk-channel (async/chan 10))
(def sugar-channel (async/chan 10))

;; whats the empty args up top for?
(async/go-loop []
               ;; i take it alts! returns value, chanel?
               (let [[v ch] (async/alts! [tea-channel
                                           milk-channel
                                           sugar-channel])]
                 (println "thanks for " v " from " ch)
                 (recur)))

(async/>!! sugar-channel :sugar)

;; these go blocks are not bounud to threads
