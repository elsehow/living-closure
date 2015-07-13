(defproject wonderland "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.6.0"]])


;;
;; COLLECTIONS
;;
;; lists, vectors, maps, sets


;; lists / vectors
'(1 2 3)
`[1 2 3]
;; conj appends to end of vector, beginning of list
(conj [1 2 3] 4)
(conj '(1 2 3) 4)
;; use vectors if you want to refer to indeces
(nth [1 2 3] 2)


;; maps
{:jam1 "strawberry", :jam2 "blackberry"}
(get {:jam1 "strawberry", :jam2 "blackberry"} :jam1 "not found")
(:jam1 {:jam1 "strawberry", :jam2 "blackberry"} "not found")
(:jam3 {:jam1 "strawberry", :jam2 "blackberry"} "not found")
(assoc {:jam1 "strawberry", :jam2 "blackberry"} :jam3 "orange")
(dissoc {:jam1 "strawberry", :jam2 "blackberry"} :jam2)
;; merge
(merge  {:jam1 "red" :jam2 "black"}
        {:jam1 "orange" :jam3 "pink"}
        {:jam4 "blue"})


;; sets
#{:red :blue :white :pink}
;; #{:red :blue :white :pink :pink}

(clojure.set/union #{:r :b :w} #{:w :p :y})
(clojure.set/difference #{:r :b :w} #{:w :p :y})
(clojure.set/intersection #{:r :b :w} #{:w :p :y})
;; lookups in sets
(:rabbit #{:rabbit :door})
(#{:rabbit :door} :rabbit)
;; convert to sets
(set {:a 1 :b 2})
;;contains?
(contains? #{:rabbit :door :watch} :watch)
;; conj/disj
(conj #{:rabbit :door} :jam)
(conj #{:rabbit :door} :door)
(disj #{:rabbit :door} :door)

;; why do we need '() befor ethe list?
;; the first elementin an expression is taken to be an operator otherwise
'(+ 1 1)
(first '(+ 1 1))
;; get it? all clojure is a list of data.


;;
;; SYMBOLS / BINDING
;;
;; def => global bindings
;; let => local bindings

; global vars with def
(def developer "Nick")
developer
wonderland/developer

;; we dont want global vars for all the things
;; to `let` something take a value for, say, a calculation,
(let [developer "Alice in Wonderland"]
  developer)
developer
;; let is bound in a vector.
;; what heppsn in a let, statys in a let.
(let [developer "Alice"
      rabbit "white rabbit"]
   [developer rabbit])
;; outhere, rabbit -> unable to resolve symbol


;; defn binds functions
(defn follow-the-rabbit [] "off we go")
(follow-the-rabbit)
(defn shop-for-jams [jam1 jam2]
  {:name "jam-basket"
   :jam1 jam1
   :jam2 jam2})
(shop-for-jams "strawberry" "marmalade")

;;anonymous functions
(fn [] (str "off we go" "!"))
;; invoke it with parens
((fn [] (str "off we go" "!")))

;; PROTIP: defn is short for (def name (fn .. ))

;; shorthand anonymous function
(#(str "of we go" "!"))
;; use % to represent a parameter
(#(str "we go" "!" "-" %) "again")
;; number them for multiple params
(#(str %1 " we go" "!" "-" %2) "off" "again?")


;; namespaces
;; create a namespace to keep things organized
(ns alice.favfoods)
*ns* ;; asteriks are called earmuffs - convention for things that are intended for rebinding (change)
;; showing here that we've switched the namespace
(def fav-food "strawberry jam")
fav-food
alice.favfoods/fav-food

(ns rabbit.favfoods)
(def fav-food "lettuce")
fav-food

;;libs are namespaces...of stuff...
;; you can require them
(require 'clojure.set)
;; or require with an alaias
(require '[alice.favfoods :as af])
af/fav-food
; idiomatic way
(ns wonderland
  (:require [alice.favfoods :as af]))

;; PROTIP - you can (ns wonderland (:require [alice.favfoods :refer :all]))
;; to load shit into your namespace
;; this is mostly used for tests

;; example --

(ns wonderland
  (:require [clojure.set :as s]))

(defn common-fav-foods [foods1 foods2]
  (let [food-set1 (set foods1)
        food-set2 (set foods2)
        common-foods (s/intersection food-set1 food-set2)]
    (str "common foods: " common-foods)))

(common-fav-foods [:jam :brownies :toast :jam]
                  [:lettuce :carrots :jam :lettuce])


;; nil is logically false
(true? nil)
(false? nil)
(not nil)
;; nil and false are the ONLY logically false values
;; not of some other value, like a string, is false
(not "some string")
;; remember - this is NOT javascript:
(true? "string")
(false? "string")


;; EQUALITY
(= :drinkme :drinkme)
(not= :drinkme 4)
;; list equality is special
(= '(1 2 3) [1 2 3])

;; TESTS FOR COLLECTIONS
(empty? [])
(empty? #{})

;; IF / WHEN / COND / CASE

(if (= :bottle :bottle)
  "it is"
  "it isnt")

(when (= :bottle :watch)
  "it is")

(let [bottle "drinkme"]
  (cond
   (= bottle "poison") "dont touch"
   (= bottle "drinkme") "grow smaller"
   (= bottle "empty") "dont touch"
   :else "unknown"))

(let [bottle "mystery"]
  (case bottle
    "poison" "don't touch"
    "drinkme" "grow smaller"
    "empty" "all gone"
    "unknown"))
