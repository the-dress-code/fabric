(ns migrations.20230718115633-rename-column-fabric
  (:require [coast.db.migrations :refer :all]))

(defn change []
  (rename-column :fabric :color-intensity :shade))
