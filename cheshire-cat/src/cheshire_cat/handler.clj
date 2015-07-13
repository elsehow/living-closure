(ns cheshire-cat.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
            [ring.middleware.json :as ring-json]
            [ring.util.response :as rr]))

;; make some http routes called app-routes
(defroutes app-routes
  (GET "/" [] "Hello Worl'")
  (GET "/cheshire-cat" []
        ;;  turns clojure data structure into json
        ;; and handles response headers etc
       (rr/response
               {:name "cheshire cat"
                :status "grinning"}))
  (route/not-found "Not Found"))

;; tons of options here
;; plenty of good default setups
;; github.com/ring-clojure/ring-defaults
(def app
  (-> app-routes
    (ring-json/wrap-json-response)
    (wrap-defaults site-defaults)))

