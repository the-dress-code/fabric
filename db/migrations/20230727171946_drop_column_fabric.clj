(ns migrations.20230727171946-drop-column-fabric
  (:require [coast.db.migrations :refer :all]))

(defn down []
  (drop-column :fabric :image-url))
