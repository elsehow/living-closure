;;
;; THE MEAT OF IT
;;
;; now it is getting interesting


;; PARTIALS
;; a way of currying

(defn grow [name direction]
  (if (= direction :small)
    (str name " is growing smaller")
    (str name " is growing bigger")))

(grow "Alice" :small)

(def grow-alice (partial grow "Alice"))

(grow-alice :big)

;; another nice example
(defn adder [x y] (+ x y))
(adder 3 4)
(def adder-five (partial adder 5))
(adder-five 10)


;; COMP
;; combining multiple fns

(defn toggle-grow [direction]
  (if (= direction :small) :big :small))

(defn oh-my [direction]
  (str "oh my! u are growing " direction))

(defn surprise [direction]
  ((comp oh-my toggle-grow) direction))

(surprise :small)

;; ????? how are the return values of the functions being combined?



;; DESTRUCTURING
;; name elements of collections

(defn door-size [color size]
  (str "the " color " door is " size))

;; [color size] ["blue" "small"]
(let [[color size] ["blue" "small"]]
  (door-size color size))

;; handles nesting well too
(let [[color [size]] ["blue" ["v small"]]]
  (door-size color size))


;; maps, missing values
(let [{flower1 :flower1 flower2 :flower2 :or {flower2 "missing"}}
      {:flower1 "red"}]
  (str "the flowers are " flower1 " and " flower2))

;; ??? :as keyword

;; :keys shortcut
(let [{:keys [flower1 flower2]}
      {:flower1 "red" :flower2 "blue"}]
  (str "the flowers are " flower1 " and " flower2))


;; here is a nice way to document what the incoming structure looks like ...
;; ???? using :keys here instead .. whats the advantage?
(defn flower-colors [colors]
  (str "the flowers are "
       (:flower1 colors)
       " and "
       (:flower2 colors)))

(flower-colors {:flower1 "red" :flower2 "blue"})




;; INFINITE LISTS
(take 5 (range))
(class (range 5))
(class (repeat "rabbit"))

;; repeatedly versus repeat
(repeat 5 (rand-int 10))
(repeatedly 5 #(rand-int 10))
;; repeat takes a to give
;; repeatedly takes a function to execute

;; cycle...
;; takes a sequence and cycles thru them lazily forever
(take 6 (cycle [1 2 3]))

;; other sequence functions work on lazy sequences...
(take 3 (rest (cycle ["big" "small"])))



;; RECURSION
(def adjs ["normal" "too small" "too big" "swimming"])

(defn alice-is [in out]
  (if (empty? in) out ;; if in is empty, we're ready to return
  (alice-is ;; start over and call this fn with diff inputs:
   (rest in) ;; input 1: the rest of the original input
   (conj out ;; input 2: apply string to elment and append to output vector
         (str "Alice is " (first in))))))

(alice-is adjs [])

;; the RECURS fn makes things simpler:
(defn alice-is [input]
  (loop [in input
         out []]
    (if (empty? in)
      out
      (recur (rest in) ;; recur jumps back to the recursion point - the beginning of loop - and rebinds the paramaters to the new paramaters
             (conj out
                  (str "alice is " (first in)))))))

;; now we can call this fn with only one paramater
(alice-is adjs)


;; always use recur when doing recursive calls -
;; it only needs one stack at a time. ;; compare:

(defn countdown [n]
  (if (= n 0)
    n
    (countdown (- n 1))))
(countdown 10000)

;; stack overflow!
;; versus:

(defn countdown [n]
  (if (= n 0) n
    (recur (- n 1))))
(countdown 10000)



;;; "functional programming is all about shaping data. it is all about transforming the incoming collection to a new data structure"
;; "with side effects and mutable state stripped a way, the beauty of these pure transformations .... "


;; MAP AND REDUCE
(def animals [:mouse :duck :dodo :lory :eaglet])
(def colors ["brown" "black" "blue"])

;; map preserves the shape of the incoming collection:
(map #(str %) animals)

(defn gen-animal-string [animal color]
  (str color " " animal))
(map gen-animal-string animals (cycle colors))

;; reduce can change it:
(reduce + [1 2 3 4 5])
;; filter is a kind of reduce....
(reduce (fn [r x] (if (nil? x) r (conj r x)))
        []
        [:mouse nil :duck nil nil :lory])

;; look - map is lazy!
(class (map #(str %) animals))

;; reduce is not - it will not take lazy lists
;; can you think of why? ;)



;; COMPLEMENT
; there IS a filter - it takes a predicate and a collection
; as a predicate, we will use complement - takes the function and returns a function that takes the same arguments, but returns an opposite truth value...
((complement nil?) nil)
((complement nil?) 1)

(filter (complement nil?) [:mouse nil :duck nil])

;; other ways --
(filter keyword? [:mouse nil nil :duck])
(remove nil? [:mouse nil nil :duck])


;; for is also nice
(for [animal [:mouse :duck :lory]
      color [:red :blue]]
  (str (name color) (name animal)))

;; LET and WHEN are cool modifiers for FOR
(for [animal [:mouse :duck :lory]
      color [:red :blue]
      :let [animal-str (str "animal-" (name animal))
            color-str (str "color-" (name color))
            display-str (str animal-str " " color-str)]
      :when (= color :blue)]
  display-str)


;; flatten
(flatten [ [:duck [:mouse] :lory]])

;; into
(into [] '(1 2 3))
;; sorted maps! its own type!?
(into (sorted-map) {:b 2 :a 3 :c 7})
;; vector pairs...
(into [] {:a 1 :b 2 :c 3})

;; partition
(partition 3 [1 2 3 4 5 6 7])
(partition-all 3 [1 2 3 4 5 6 7])

;; partition by
;; (partition-by fn coll)
;; makes a new partition every time the result to fn changes,,,,,,,
(partition-by #(= 6 %) [1 2 3 4 5 6 7 8 9 10])


