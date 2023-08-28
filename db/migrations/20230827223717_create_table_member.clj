(ns migrations.20230827223717-create-table-member
  (:require [coast.db.migrations :refer :all]))

(defn change []
  (create-table :member
    (text :email)
    (text :password)
    (timestamps)))