(ns migrations.20230721105821-add-column-fabric
  (:require [coast.db.migrations :refer :all]))

(defn change []
  (add-column :fabric :image-url :text))
