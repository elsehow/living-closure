(ns cheshire-cat.core
  ;; http calls use core/async, so we need go
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [clojure.browser.repl :as repl]
            [cljs-http.client :as http]
            [cljs.core.async :refer [<!]]
            [enfocus.core :as ef]
            [enfocus.events :as ev]
            [enfocus.effects :as ee]))

(defn say-goodbye []
  (ef/at
   "#cat-name" (ee/fade-out 300)
   "#status" (ee/fade-out 5000)
   "#button1" (ee/fade-out 1000)))

;; ^:export exposes this as a function that js acn call from the global context
;; this is to prevent google closure compiler from mucking it up
(defn ^:export init[]
  ;; connect to browser repl
  (repl/connect "http://localhost:9000/repl")
  ;; http/get returns a core.async channel
  ;; thats why this is in a go block
  (go
   ;; async take to get the result of the get...
   (let [response (<! (http/get "/cheshire-cat"))
         body (:body response)]
     (ef/at "#cat-name" (ef/content (:name body))
            "#status" (ef/do->
                       (ef/content (:status body))
                       (ef/set-style :font-size "500%")))
     (ef/at "#button1" (ev/listen
                        :click
                        #(say-goodbye))))))
