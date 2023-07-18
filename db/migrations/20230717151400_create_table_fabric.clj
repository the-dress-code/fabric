(ns migrations.20230717151400-create-table-fabric
  (:require [coast.db.migrations :refer :all]))

(defn change []
  (create-table :fabric
    (text :color-intensity)
    (text :color)
    (text :weight)
    (text :content)
    (text :structure)
    (integer :width)
    (integer :yards)
    (timestamps)))