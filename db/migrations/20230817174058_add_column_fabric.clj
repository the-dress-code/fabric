(ns migrations.20230817174058-add-column-fabric
  (:require [coast.db.migrations :refer :all]))

(defn change []
  (add-column :fabric :image :text))
