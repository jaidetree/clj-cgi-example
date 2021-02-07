(ns metal.style)

(defn rems
  [px]
  (str (float (/ px 16)) "rem"))
