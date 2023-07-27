(ns migrations.20230726213212-add-column-fabric
  (:require [coast.db.migrations :refer :all]))

(defn change []
  (add-column :fabric :item-number :text))
