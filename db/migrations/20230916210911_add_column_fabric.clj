(ns migrations.20230916210911-add-column-fabric
  (:require [coast.db.migrations :refer :all]))

(defn change []
  (add-column :fabric :user-id :integer))
